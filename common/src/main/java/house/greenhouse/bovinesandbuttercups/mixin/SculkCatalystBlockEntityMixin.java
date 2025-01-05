package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.ConvertMoobloomFromSculkTrigger;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
                var compatibleList = moobloom.getCowType().value().configuration().filterSculkConverts();

                if (compatibleList.isEmpty())
                    return;

                if (compatibleList.size() == 1) {
                    CowTypeAttachment.setCowType(moobloom, (Holder) compatibleList.getFirst().data(), (Holder) moobloom.getCowType());
                    CowTypeAttachment.sync(moobloom);
                } else {
                    int totalWeight = moobloom.getRandom().nextInt(compatibleList.stream().map(holderWrapper -> holderWrapper.weight().asInt()).reduce(Integer::sum).orElse(0));
                    for (var cct : compatibleList) {
                        totalWeight -= cct.weight().asInt();
                        if (totalWeight < 0) {
                            CowTypeAttachment.setCowType(moobloom, (Holder) cct.data(), (Holder) moobloom.getCowType());
                            CowTypeAttachment.sync(moobloom);
                            break;
                        }
                    }
                }

                if (context.sourceEntity() != null && context.sourceEntity() instanceof LivingEntity living && living.getLastHurtByMob() instanceof ServerPlayer source)
                    ConvertMoobloomFromSculkTrigger.INSTANCE.trigger(source);
                moobloom.playSound(BovinesSoundEvents.MOOBLOOM_CONVERT);
                serverLevel.sendParticles(ParticleTypes.SCULK_SOUL, moobloom.getX(), moobloom.getY(0.5), moobloom.getZ(), 8, 0.8F, 0.25F, 0.8F, 0.0F);
            });
        }
    }
}
