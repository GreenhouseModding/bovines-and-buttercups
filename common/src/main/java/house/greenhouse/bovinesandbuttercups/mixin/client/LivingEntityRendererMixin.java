package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.access.FlowerCrownRenderStateAccess;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @ModifyVariable(method = "getRenderType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation bovinesandbuttercups$modifyTextureLocation(ResourceLocation value, @Local(argsOnly = true) LivingEntityRenderState state) {
        if (state instanceof CowVariantRenderState<?, ?, ?> cowTypeRenderState && cowTypeRenderState.getCowVariant().isBound())
            return BovinesAndButtercupsClient.getCachedTextures((Holder) cowTypeRenderState.getCowVariant(), value);
        return value;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void bovinesandbuttercups$extractFlowerCrownRenderState(T entity, S state, float partialTick, CallbackInfo ci) {
        if (state instanceof FlowerCrownRenderStateAccess access)
            access.bovinesandbuttercups$setFlowerCrown(BovinesAndButtercupsClient.getHelper().getEquippedFlowerCrownForRendering(entity).get(BovinesDataComponents.FLOWER_CROWN));
    }
}
