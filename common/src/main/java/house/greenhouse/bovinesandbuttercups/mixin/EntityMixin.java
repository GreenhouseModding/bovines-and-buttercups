package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.util.LegacyAttachmentLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class, priority = 1500) // Prioritise after Fabric.
public abstract class EntityMixin {
    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void bovinesandbuttercups$loadFromLegacyAttachments(CompoundTag tag, CallbackInfo ci) {
        // DFU at home!!!
        if ((Entity)(Object)this instanceof LivingEntity living && BovinesRegistries.COW_TYPE.stream().anyMatch(type -> type.isApplicable(living)) && tag.contains(BovinesAndButtercups.getHelper().getAttachmentKey())) {
            CompoundTag attachmentTag = tag.getCompound(BovinesAndButtercups.getHelper().getAttachmentKey());
            if (attachmentTag.contains("bovinesandbuttercups:cow_type"))
                LegacyAttachmentLoader.loadFromLegacyAttachment(living, attachmentTag.get("bovinesandbuttercups:cow_type"));
        }
    }
}