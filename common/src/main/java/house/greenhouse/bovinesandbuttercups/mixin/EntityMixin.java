package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.util.ConversionUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
    private void bovinesandbuttercups$convertCowThroughLightning(ServerLevel level, LightningBolt lightning, CallbackInfo ci) {
        if (!((Entity)(Object)this instanceof LivingEntity living) || BovinesAndButtercups.getHelper().getCowVariantAttachment(living) == null || !ConversionUtil.CONVERTED_BY_BOVINES.contains(living))
            return;
        ConversionUtil.CONVERTED_BY_BOVINES.remove(living);
        ci.cancel();
    }
}
