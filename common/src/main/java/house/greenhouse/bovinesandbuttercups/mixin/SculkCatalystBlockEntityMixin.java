package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkCatalystBlockEntity.CatalystListener.class)
public class SculkCatalystBlockEntityMixin {
    @Inject(method = "handleGameEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/SculkCatalystBlockEntity$CatalystListener;tryAwardItSpreadsAdvancement(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void bovinesandbuttercups$convertToSombercup(ServerLevel serverLevel, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) int i) {
        if (i > 4) {
            serverLevel.getEntitiesOfClass(Moobloom.class, new AABB(-8, -4, -8, 8, 4, 8).move(pos)).forEach(moobloom -> {
                moobloom.playSound(BovinesSoundEvents.MOOBLOOM_CONVERT);
                serverLevel.sendParticles(ParticleTypes.SCULK_SOUL, moobloom.getX(), moobloom.getY(0.4), moobloom.getZ(), 8, 0.2, 0.2F, 0.2, 0.0F);
                CowTypeAttachment.<MoobloomConfiguration>setCowType(moobloom, (Holder) serverLevel.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).getOrThrow(BovinesCowTypes.MoobloomKeys.SOMBERCUP));
                CowTypeAttachment.sync(moobloom);
            });
        }
    }
}
