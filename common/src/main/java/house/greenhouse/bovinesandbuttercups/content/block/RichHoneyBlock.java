package house.greenhouse.bovinesandbuttercups.content.block;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.mixin.HoneyBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public class RichHoneyBlock extends HoneyBlock {
    public RichHoneyBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
        if (level.isClientSide)
            showJumpParticles(entity);

        if (entity.causeFallDamage(fallDistance, 0.2F, level.damageSources().fall()))
            entity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
    }

    @Override
    public void maybeDoSlideEffects(Level level, Entity entity) {
        if (HoneyBlockAccessor.doesEntityDoHoneyBlockSlideEffects(entity)) {
            if (level.random.nextInt(5) == 0) {
                entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }

            if (level.isClientSide && level.random.nextInt(5) == 0) {
                showSlideParticles(entity);
            }
        }
    }

    public static void showSlideParticles(Entity entity) {
        showParticles(entity, 5);
    }

    public static void showJumpParticles(Entity entity) {
        showParticles(entity, 10);
    }

    private static void showParticles(Entity entity, int particleCount) {
        if (entity.level().isClientSide) {
            BlockState blockstate = BovinesBlocks.RICH_HONEY_BLOCK.defaultBlockState();

            for (int i = 0; i < particleCount; i++) {
                entity.level()
                        .addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    public boolean isStickyBlock(BlockState state) {
        return true;
    }

    public boolean canStickTo(BlockState state, BlockState other) {
        return BovinesAndButtercups.getHelper().canStickToRichHoney(state, other);
    }

    public PathType getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return PathType.STICKY_HONEY;
    }
}
