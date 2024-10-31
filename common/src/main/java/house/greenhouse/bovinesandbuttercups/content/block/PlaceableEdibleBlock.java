package house.greenhouse.bovinesandbuttercups.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PlaceableEdibleBlock extends BaseEntityBlock {
    public static final MapCodec<PlaceableEdibleBlock> CODEC = simpleCodec(PlaceableEdibleBlock::new);
    public static final int MIN_CUPCAKES = 1;
    public static final int MAX_CUPCAKES = 4;
    public static final IntegerProperty COUNT = IntegerProperty.create("count", MIN_CUPCAKES, MAX_CUPCAKES);
    protected static final VoxelShape[] SHAPE_BY_COUNT = new VoxelShape[] {
            Block.box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
            Block.box(5.0, 0.0, 5.0, 11.0, 5.0, 11.0),
            Block.box(1.0, 0.0, 5.0, 15.0, 5.0, 11.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0)
    };

    public PlaceableEdibleBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(COUNT, 1));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_COUNT[state.getValue(COUNT)];
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            if (eat(level, pos, state, player).consumesAction())
                return InteractionResult.SUCCESS;

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
                return InteractionResult.CONSUME;
        }

        return eat(level, pos, state, player);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Item item = stack.getItem();
        int i = state.getValue(COUNT);
        if (stack.is(asItem())) {
            if (i > 3)
                return ItemInteractionResult.CONSUME;
            stack.consume(1, player);
            level.setBlock(pos, state.setValue(COUNT, i + 1), 3);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            player.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.SUCCESS;
        }
        // TODO: Modify me!
        if (stack.is(ItemTags.CANDLES)) {
            stack.consume(1, player);
            level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.blockEntityChanged(pos);
            player.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static InteractionResult eat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false))
            return InteractionResult.PASS;

        player.awardStat(Stats.EAT_CAKE_SLICE);
        player.getFoodData().eat(2, 0.1F);
        int i = state.getValue(COUNT);
        level.gameEvent(player, GameEvent.EAT, pos);
        if (i > MIN_CUPCAKES)
            level.setBlock(pos, state.setValue(COUNT, i - 1), 3);
        else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }

        return InteractionResult.SUCCESS;
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
        builder.add(COUNT);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return Math.min(MAX_CUPCAKES - blockState.getValue(COUNT) * 4, 15);
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
        return null;
    }
}
