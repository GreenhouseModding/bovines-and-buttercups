package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.ConvertMoobloomFromSculkTrigger;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParamSets;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.util.ConversionUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SculkCatalystBlockEntity.CatalystListener.class)
public class SculkCatalystBlockEntityMixin {
    @Inject(method = "handleGameEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/SculkCatalystBlockEntity$CatalystListener;tryAwardItSpreadsAdvancement(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void bovinesandbuttercups$convertToSombercup(ServerLevel serverLevel, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) int i) {
        if (i > 4) {
            serverLevel.getEntitiesOfClass(Moobloom.class, new AABB(-8, -4, -8, 8, 4, 8).move(pos)).forEach(moobloom -> {
                LootParams params = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, moobloom.position())
                        .withParameter(LootContextParams.THIS_ENTITY, moobloom)
                        .create(BovinesLootContextParamSets.ENTITY);
                LootContext lootContext = new LootContext.Builder(params).create(Optional.empty());

                var compatibleList = moobloom.getCowVariant().value().configuration().sculkConverts().unwrap().stream().filter(data -> data.data().conditions().isEmpty() || data.data().conditions().stream().allMatch(condition -> condition.test(lootContext))).toList();

                if (!ConversionUtil.convert(moobloom, serverLevel, compatibleList))
                    return;

                if (context.sourceEntity() != null && context.sourceEntity() instanceof LivingEntity living && living.getLastHurtByMob() instanceof ServerPlayer source)
                    ConvertMoobloomFromSculkTrigger.INSTANCE.trigger(source);
                moobloom.playSound(BovinesSoundEvents.MOOBLOOM_CONVERT);
                serverLevel.sendParticles(ParticleTypes.SCULK_SOUL, moobloom.getX(), moobloom.getY(0.5), moobloom.getZ(), 8, 0.8F, 0.25F, 0.8F, 0.0F);
            });
        }
    }
}