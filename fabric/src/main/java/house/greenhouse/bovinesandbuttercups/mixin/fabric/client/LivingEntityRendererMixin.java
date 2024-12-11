package house.greenhouse.bovinesandbuttercups.mixin.fabric.client;

import house.greenhouse.bovinesandbuttercups.client.access.FlowerCrownRenderStateAccess;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.FlowerCrownLayer;
import house.greenhouse.bovinesandbuttercups.mixin.client.EntityRendererAccessor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState,  M extends EntityModel<S>> extends EntityRenderer<T, S> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Shadow protected abstract boolean addLayer(RenderLayer<S, M> layer);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bovinesandbuttercups$allowEquippingFlowerCrown(EntityRendererProvider.Context context, M model, float shadowRadius, CallbackInfo ci) {
        if (!((LivingEntityRenderer)(Object)this instanceof PlayerRenderer) && ((EntityRendererAccessor)this).bovinesandbuttercups$getReusedState() instanceof FlowerCrownRenderStateAccess)
            addLayer(new FlowerCrownLayer<>((RenderLayerParent) this, context::bakeLayer, context.getModelManager()));
    }
}
