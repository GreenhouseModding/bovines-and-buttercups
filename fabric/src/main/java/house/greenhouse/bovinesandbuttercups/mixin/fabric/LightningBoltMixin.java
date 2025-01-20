package house.greenhouse.bovinesandbuttercups.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
    @Shadow @Final private Set<Entity> hitEntities;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"))
    private void bovinesandbuttercups$thunderHit(CallbackInfo ci, @Local Entity entity) {
        if (hitEntities.contains(entity) || !(entity instanceof LivingEntity living) || (entity instanceof Moobloom) || !entity.hasAttached(BovinesAttachments.COW_VARIANT))
            return;
        CowTypeAttachment attachment = entity.getAttached(BovinesAttachments.COW_VARIANT);
        if (!attachment.cowVariant().isBound() || !attachment.cowVariant().value().configuration().allowsConversion(entity))
            return;
        if (attachment.previousCowVariant().isEmpty()) {
            if (attachment.cowVariant().value().configuration().settings().thunderConverts().isEmpty())
                return;

            var compatibleList = attachment.cowVariant().value().configuration().settings().filterThunderConverts(attachment.cowVariant().value().type());

            if (compatibleList.size() == 1) {
                CowTypeAttachment.setCowVariant(living, (Holder) compatibleList.getFirst().data(), (Holder) attachment.cowVariant());
                CowTypeAttachment.sync(living);
                BovinesAndButtercups.convertedByBovines = true;
            } else if (!compatibleList.isEmpty()) {
                int totalWeight = entity.getRandom().nextInt(compatibleList.stream().map(holderWrapper -> holderWrapper.weight().asInt()).reduce(Integer::sum).orElse(0));
                for (var cct : compatibleList) {
                    totalWeight -= cct.weight().asInt();
                    if (totalWeight < 0) {
                        CowTypeAttachment.setCowVariant(living, (Holder) cct.data(), (Holder) attachment.cowVariant());
                        CowTypeAttachment.sync(living);
                        BovinesAndButtercups.convertedByBovines = true;
                        break;
                    }
                }
            }
        } else {
            CowTypeAttachment.setCowVariant(living, (Holder) attachment.previousCowVariant().get());
            CowTypeAttachment.sync(living);
            BovinesAndButtercups.convertedByBovines = true;
        }
    }

}
