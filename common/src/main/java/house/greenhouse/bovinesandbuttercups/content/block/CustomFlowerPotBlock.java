package house.greenhouse.bovinesandbuttercups.content.block;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CustomFlowerPotBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    private static final MapCodec<CustomFlowerPotBlock> CODEC = simpleCodec(CustomFlowerPotBlock::new);

    public CustomFlowerPotBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ItemStack stack = new ItemStack(BovinesItems.CUSTOM_FLOWER);
        stack.set(BovinesDataComponents.CUSTOM_FLOWER, ((CustomFlowerPotBlockEntity)level.getBlockEntity(pos)).getFlowerType());

        if (!player.addItem(stack)) {
            player.drop(stack, false);
        }

        level.setBlock(pos, Blocks.FLOWER_POT.defaultBlockState(), Block.UPDATE_ALL);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return this.getContent(level, pos);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    public ItemStack getContent(BlockGetter level, BlockPos pos) {
        ItemStack stack = new ItemStack(BovinesItems.CUSTOM_FLOWER);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CustomFlowerPotBlockEntity cfbe)
            if (cfbe.getFlowerType() != null)
                stack.set(BovinesDataComponents.CUSTOM_FLOWER, cfbe.getFlowerType());
        return stack;
    }

    @Override
    public boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BovinesBlockEntityTypes.POTTED_CUSTOM_FLOWER.create(pos, state);
    }
}
