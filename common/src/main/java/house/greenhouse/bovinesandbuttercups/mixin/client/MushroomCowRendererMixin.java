package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.access.EntityRendererLayerBakerAccess;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(MushroomCowRenderer.class)
public class MushroomCowRendererMixin implements EntityRendererLayerBakerAccess {
    private Function<ModelLayerLocation, CowModel<MushroomCow>> bovinesandbuttercups$layerBakeFunction;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bovinesandbuttercups$storeMooshroomRendererLayerBaker(EntityRendererProvider.Context context, CallbackInfo ci) {
        bovinesandbuttercups$layerBakeFunction = modelLayerLocation -> new CowModel<>(context.bakeLayer(modelLayerLocation));
    }

    @Override
    public Function<ModelLayerLocation, CowModel<MushroomCow>> bovinesandbuttercups$getMooshroomLayerBakeFunction() {
        return bovinesandbuttercups$layerBakeFunction;
    }
}