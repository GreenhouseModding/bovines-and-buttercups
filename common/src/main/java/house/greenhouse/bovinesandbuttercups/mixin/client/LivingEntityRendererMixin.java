package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @ModifyVariable(method = "getRenderType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation bovinesandbuttercups$modifyTextureLocation(ResourceLocation value, T living) {
        CowTypeAttachment attachment = BovinesAndButtercups.getHelper().getCowTypeAttachment(living);
        if (attachment != null && attachment.cowType().isBound() && attachment.cowType().value().type().isApplicable(living))
            return BovinesAndButtercupsClient.getCachedTextures(attachment.cowType(), value);
        return value;
    }
}
