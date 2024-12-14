package house.greenhouse.bovinesandbuttercups.content.block;

import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NightshadeBlock extends TagPlaceableFlowerBlock {
    public NightshadeBlock(Holder<MobEffect> effect, float seconds, Properties properties) {
        super(effect, seconds, BovinesTags.BlockTags.NIGHTSHADE_PLACEABLE, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide() && level.getDifficulty() != Difficulty.PEACEFUL && entity instanceof Bee bee) {
            if (Bee.attractsBees(state) && !bee.hasEffect(MobEffects.POISON)) {
                bee.addEffect(this.getBeeInteractionEffect());
            }
        }
    }

    @Override
    public MobEffectInstance getBeeInteractionEffect() {
        return new MobEffectInstance(MobEffects.POISON, 25);
    }
}
