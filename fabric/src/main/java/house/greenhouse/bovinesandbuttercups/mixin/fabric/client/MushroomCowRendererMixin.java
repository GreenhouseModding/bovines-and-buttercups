package house.greenhouse.bovinesandbuttercups.mixin.fabric.client;

import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.CowLayersLayer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.MooshroomDatapackMushroomLayer;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin extends MobRenderer<MushroomCow, MushroomCowRenderState, CowModel> {
    public MushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bovinesandbuttercups$initRenderLayers(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new CowLayersLayer(this));
        this.addLayer(new MooshroomDatapackMushroomLayer<>(this, context.getBlockRenderDispatcher()));
    }
}
