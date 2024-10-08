package house.greenhouse.bovinesandbuttercups.content.block;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.registry.BovinesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CustomHugeMushroomBlock extends BaseEntityBlock {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;

    private static final MapCodec<CustomHugeMushroomBlock> CODEC = simpleCodec(CustomHugeMushroomBlock::new);

    public CustomHugeMushroomBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(BovinesItems.CUSTOM_MUSHROOM);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CustomMushroomPotBlockEntity cmpbe)
            stack.set(BovinesDataComponents.CUSTOM_MUSHROOM, cmpbe.getMushroomType());
        return stack;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        if (level.getBlockEntity(pos) != null)
            ((CustomHugeMushroomBlockEntity)level.getBlockEntity(pos)).updateState();
        return super.updateShape(state, direction, state2, level, pos, pos2);
    }

    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return $$0.setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.NORTH)), $$0.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.SOUTH)), $$0.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.EAST)), $$0.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.WEST)), $$0.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.UP)), $$0.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.DOWN)), $$0.getValue(DOWN));
    }

    public BlockState mirror(BlockState $$0, Mirror $$1) {
        return $$0.setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.NORTH)), $$0.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.SOUTH)), $$0.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.EAST)), $$0.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.WEST)), $$0.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.UP)), $$0.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.DOWN)), $$0.getValue(DOWN));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BovinesBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK.create(pos, state);
    }
}
