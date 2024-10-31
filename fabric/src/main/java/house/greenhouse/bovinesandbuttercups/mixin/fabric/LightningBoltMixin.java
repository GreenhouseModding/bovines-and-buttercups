package house.greenhouse.bovinesandbuttercups.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
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
        if (hitEntities.contains(entity) || !(entity instanceof LivingEntity living) || (entity instanceof Moobloom) || !entity.hasAttached(BovinesAttachments.COW_TYPE) || !living.getAttachedOrElse(BovinesAttachments.MOOSHROOM_EXTRAS, MooshroomExtrasAttachment.DEFAULT).allowConversion())
            return;
        CowTypeAttachment attachment = entity.getAttached(BovinesAttachments.COW_TYPE);
        if (attachment.previousCowType().isEmpty()) {
            if (attachment.cowType().value().configuration().settings().thunderConverts().isEmpty())
                return;

            var compatibleList = attachment.cowType().value().configuration().settings().filterThunderConverts(attachment.cowType().value().type());

            if (compatibleList.size() == 1) {
                CowTypeAttachment.setCowType(living, (Holder) compatibleList.getFirst().data(), (Holder) attachment.cowType());
                CowTypeAttachment.sync(living);
                BovinesAndButtercups.convertedByBovines = true;
            } else if (!compatibleList.isEmpty()) {
                int totalWeight = entity.getRandom().nextInt(compatibleList.stream().map(holderWrapper -> holderWrapper.weight().asInt()).reduce(Integer::sum).orElse(0));
                for (var cct : compatibleList) {
                    totalWeight -= cct.weight().asInt();
                    if (totalWeight < 0) {
                        CowTypeAttachment.setCowType(living, (Holder) cct.data(), (Holder) attachment.cowType());
                        CowTypeAttachment.sync(living);
                        BovinesAndButtercups.convertedByBovines = true;
                        break;
                    }
                }
            }
        } else {
            CowTypeAttachment.setCowType(living, (Holder) attachment.previousCowType().get());
            CowTypeAttachment.sync(living);
            BovinesAndButtercups.convertedByBovines = true;
        }
    }

}
