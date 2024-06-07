package net.merchantpug.bovinesandbuttercups.content.block;

import com.mojang.serialization.MapCodec;
import net.merchantpug.bovinesandbuttercups.api.block.CustomMushroomType;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.registry.BovinesBlockEntityTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovinesBlocks;
import net.merchantpug.bovinesandbuttercups.registry.BovinesDataComponents;
import net.merchantpug.bovinesandbuttercups.registry.BovinesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CustomMushroomBlock extends BaseEntityBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    private static final MapCodec<CustomMushroomBlock> CODEC = simpleCodec(CustomMushroomBlock::new);


    public CustomMushroomBlock(Properties properties) {
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(25) == 0) {
            int i = 5;

            for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (level.getBlockState(blockpos).is(this)) {
                    --i;
                    if (i <= 0) {
                        return;
                    }
                }
            }

            BlockPos blockpos1 = pos.offset(randomSource.nextInt(3) - 1, randomSource.nextInt(2) - randomSource.nextInt(2), randomSource.nextInt(3) - 1);

            for(int k = 0; k < 4; ++k) {
                if (level.isEmptyBlock(blockpos1) && state.canSurvive(level, blockpos1)) {
                    pos = blockpos1;
                }

                blockpos1 = pos.offset(randomSource.nextInt(3) - 1, randomSource.nextInt(2) - randomSource.nextInt(2), randomSource.nextInt(3) - 1);
            }

            if (level.isEmptyBlock(blockpos1) && state.canSurvive(level, blockpos1)) {
                level.setBlock(blockpos1, BovinesBlocks.CUSTOM_MUSHROOM_BLOCK.defaultBlockState(), 2);
                ((CustomMushroomBlockEntity)level.getBlockEntity(blockpos1)).setMushroomType(((CustomMushroomBlockEntity)level.getBlockEntity(pos)).getMushroomType());
                level.getBlockEntity(blockpos1).setChanged();
                level.sendBlockUpdated(blockpos1, level.getBlockState(blockpos1), level.getBlockState(pos), Block.UPDATE_ALL);
            }
        }

    }

    protected boolean mayPlaceOn(BlockState p_54894_, BlockGetter p_54895_, BlockPos p_54896_) {
        return p_54894_.isSolidRender(p_54895_, p_54896_);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        if (belowState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return true;
        } else {
            return level.getRawBrightness(pos, 0) < 13 && this.mayPlaceOn(belowState, level, belowPos);
        }
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof CustomMushroomBlockEntity mushroomBlockEntity) {
            return mushroomBlockEntity.getMushroomType().holder().isBound() && mushroomBlockEntity.getMushroomType().holder().value().hugeMushroomStructurePool().isPresent();
        }
        return false;
    }

    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos pos, BlockState state) {
        return (double)randomSource.nextFloat() < 0.4D;
    }

    public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos pos, BlockState state) {
        if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof CustomMushroomBlockEntity mushroomBlockEntity) {
            StructureTemplateManager structureTemplateManager = level.getStructureManager();

            Holder<CustomMushroomType> customMushroom = mushroomBlockEntity.getMushroomType().holder();
            if (customMushroom.isBound() && customMushroom.value().hugeMushroomStructurePool().isPresent()) {
                StructurePoolElement structurePoolElement = customMushroom.value().hugeMushroomStructurePool().get().value().getRandomTemplate(level.random);

                level.removeBlock(pos, false);
                Rotation rotation = customMushroom.value().randomlyRotateHugeStructure() ? Rotation.getRandom(level.random) : Rotation.NONE;
                BlockPos centeredPos = new BlockPos(pos.getX() - structurePoolElement.getSize(structureTemplateManager, rotation).getX() / 2, pos.getY(), pos.getZ() - structurePoolElement.getSize(structureTemplateManager, rotation).getZ() / 2);
                if (ChunkPos.rangeClosed(new ChunkPos(centeredPos), new ChunkPos(centeredPos.offset(structurePoolElement.getSize(structureTemplateManager, rotation)))).allMatch((chunkPos) -> level.isLoaded(chunkPos.getWorldPosition()))) {
                    BoundingBox structureBox = structurePoolElement.getBoundingBox(structureTemplateManager, centeredPos, rotation);
                    if (level.getBlockStates(AABB.of(structureBox)).allMatch(bs -> bs.isAir() || bs.is(BlockTags.LEAVES))) {
                        structurePoolElement.place(structureTemplateManager, level, level.structureManager(), level.getChunkSource().getGenerator(), centeredPos, centeredPos, rotation, structureBox, randomSource, LiquidSettings.APPLY_WATERLOGGING, false);
                        return;
                    }
                }

                level.setBlock(pos, state, Block.UPDATE_ALL);
                ((CustomMushroomBlockEntity)level.getBlockEntity(pos)).setMushroomType(mushroomBlockEntity.getMushroomType());
                level.getBlockEntity(pos).setChanged();
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BovinesBlockEntityTypes.CUSTOM_MUSHROOM.create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getFluidState().isEmpty();
    }

    public boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(blockState, pathComputationType);
    }
}