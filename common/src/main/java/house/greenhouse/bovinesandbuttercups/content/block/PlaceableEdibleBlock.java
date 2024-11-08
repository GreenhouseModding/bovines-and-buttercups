package house.greenhouse.bovinesandbuttercups.content.block;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.content.block.entity.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdibleType;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PlaceableEdibleBlock extends BaseEntityBlock {
    public static final MapCodec<PlaceableEdibleBlock> CODEC = simpleCodec(PlaceableEdibleBlock::new);
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 1, 16);
    public static final IntegerProperty LIGHT = IntegerProperty.create("light", 0, 15);

    public PlaceableEdibleBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(BITES, 1).setValue(LIGHT, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (level.getBlockEntity(pos) instanceof PlaceableEdibleBlockEntity blockEntity) {
            ItemEdibleType edible = blockEntity.getEdibleType();
            if (edible != null && edible.holder().isBound()) {
                var shape = edible.holder().value().shapes().entrySet().stream().filter(entry -> entry.getKey().test(blockEntity)).findFirst();
                return shape.map(Map.Entry::getValue).orElse(Shapes.block());
            }
        }
        return Shapes.block();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide)
            return InteractionResult.CONSUME;

        return eat(level, pos, state, player);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide)
            return ItemInteractionResult.CONSUME;

        Item item = stack.getItem();
        if (!(level.getBlockEntity(pos) instanceof PlaceableEdibleBlockEntity be) || be.getEdibleType() == null || !be.getEdibleType().holder().isBound())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        int i = state.getValue(BITES);
        if (stack.is(BovinesItems.PLACEABLE_EDIBLE) && be.getEdibleType().equals(stack.getOrDefault(BovinesDataComponents.EDIBLE_TYPE, new ItemEdibleType(level.registryAccess().registryOrThrow(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE).getHolderOrThrow(EdibleBlockType.MISSING_KEY))))) {
            if (i >= be.getEdibleType().holder().value().bites())
                return ItemInteractionResult.CONSUME;
            stack.consume(1, player);
            level.setBlock(pos, state.setValue(BITES, i + 1), Block.UPDATE_ALL);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            level.playSound(null, pos, getSoundType(state).getPlaceSound(), SoundSource.BLOCKS);
            player.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.SUCCESS;
        }

        var activateResult = be.activate(stack, player, hand, hitResult);
        if (activateResult.consumesAction())
            return activateResult;

        var addAttachmentResult = be.addAttachmentItem(stack, player);
        if (addAttachmentResult.consumesAction())
            return addAttachmentResult;

        var removeAttachmentResult = be.removeAttachmentItem(stack);
        if (removeAttachmentResult.consumesAction())
            return removeAttachmentResult;

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos blockPos, BlockState blockState) {
        ItemStack itemStack = new ItemStack(this);
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof PlaceableEdibleBlockEntity pebe)
            if (pebe.getEdibleType() != null)
                ItemEdibleType.apply(itemStack, pebe.getEdibleType().holder());

        return itemStack;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof PlaceableEdibleBlockEntity edibleBlockEntity) || edibleBlockEntity.getParticles() == null)
            return;
        edibleBlockEntity.getParticles()
                    .forEach(particleEntry -> addParticlesAndSound(
                                    particleEntry, level, particleEntry.position().add(pos.getX(), pos.getY(), pos.getZ()), random
                            )
                    );
    }

    private static void addParticlesAndSound(EdibleBlockType.ParticleEntry particleEntry, Level level, Vec3 offset, RandomSource random) {
        float f = random.nextFloat();
        if (f < particleEntry.chance()) {
            level.addParticle(particleEntry.particle(), offset.x, offset.y, offset.z, particleEntry.speed().x, particleEntry.speed().y, particleEntry.speed().z);
            if (particleEntry.sound().isPresent()) {
                EdibleBlockType.SoundSettings soundSettings = particleEntry.sound().get();
                if (f < soundSettings.chance()) {
                    level.playLocalSound(
                            offset.x + 0.5,
                            offset.y + 0.5,
                            offset.z + 0.5,
                            soundSettings.sound().value(),
                            SoundSource.BLOCKS,
                            soundSettings.volume().randomise(level.random),
                            soundSettings.pitch().randomise(level.random),
                            false
                    );
                }
            }
        }
    }

    public static InteractionResult eat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false))
            return InteractionResult.CONSUME;

        player.awardStat(Stats.EAT_CAKE_SLICE);
        player.getFoodData().eat(2, 0.1F);
        int i = state.getValue(BITES);
        level.gameEvent(player, GameEvent.EAT, pos);
        if (i > 1)
            level.setBlock(pos, state.setValue(BITES, i - 1), Block.UPDATE_ALL);
        else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }

        return InteractionResult.SUCCESS;
    }

    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
        BlockEntity blockEntity = lootParams.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof PlaceableEdibleBlockEntity placeableEdibleBlockEntity))
            return super.getDrops(state, params);
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        builder.addAll(super.getDrops(state, params));
        builder.addAll(placeableEdibleBlockEntity.getAttachments().values().stream().flatMap(attachmentState -> attachmentState.items().stream()).toList());
        return builder.build();
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !state.canSurvive(level, currentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
        builder.add(LIGHT);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof PlaceableEdibleBlockEntity be) || be.getEdibleType() == null || !be.getEdibleType().holder().isBound())
            return 0;
        return (int) Math.min(((float)(blockState.getValue(BITES) / be.getEdibleType().holder().value().bites())) * 15, 15);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType computationType) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BovinesBlockEntityTypes.PLACEABLE_EDIBLE.create(pos, state);
    }
}
