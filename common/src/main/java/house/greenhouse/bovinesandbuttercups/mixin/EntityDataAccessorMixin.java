package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDataAccessor.class)
public class EntityDataAccessorMixin {
    @Shadow @Final private Entity entity;

    @Inject(method = "setData", at = @At("TAIL"))
    private void bovinesandbuttercups$setData(CompoundTag other, CallbackInfo ci) {
        if (this.entity instanceof LivingEntity living) {
            if (BovinesAndButtercups.getHelper().getCowVariantAttachment(living) != null)
                CowVariantAttachment.sync(living);
            if (BovinesAndButtercups.getHelper().getLockdownAttachment(living) != null)
                LockdownAttachment.sync(living);
            if (BovinesAndButtercups.getHelper().hasMooshroomExtrasAttachment(living))
                MooshroomExtrasAttachment.sync(living);
            if (BovinesAndButtercups.getHelper().hasCowExtrasAttachment(living))
                CowExtrasAttachment.sync(living);
        }
    }
}
