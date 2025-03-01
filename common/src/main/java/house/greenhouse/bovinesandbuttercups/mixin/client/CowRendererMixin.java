package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.access.EntityRendererLayerBakerAccess;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(CowRenderer.class)
public class CowRendererMixin implements EntityRendererLayerBakerAccess<CowModel<Cow>> {
    @Unique
    private Function<ModelLayerLocation, CowModel<Cow>> bovinesandbuttercups$layerBakeFunction;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bovinesandbuttercups$storeMooshroomRendererLayerBaker(EntityRendererProvider.Context context, CallbackInfo ci) {
        bovinesandbuttercups$layerBakeFunction = modelLayerLocation -> new CowModel<>(context.bakeLayer(modelLayerLocation));
    }

    @Override
    public Function<ModelLayerLocation, CowModel<Cow>> bovinesandbuttercups$getLayerBakeFunction() {
        return bovinesandbuttercups$layerBakeFunction;
    }
}