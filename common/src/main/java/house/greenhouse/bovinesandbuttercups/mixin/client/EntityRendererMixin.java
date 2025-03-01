package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.access.EntityRendererLayerBakerAccess;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin implements EntityRendererLayerBakerAccess {
    @Unique
    private Function<ModelLayerLocation, CowModel> bovinesandbuttercups$cowBakeFunction;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;createRenderState()Lnet/minecraft/client/renderer/entity/state/EntityRenderState;"))
    private void bovinesandbuttercups$setBakeFunction(EntityRendererProvider.Context context, CallbackInfo ci) {
        if ((EntityRenderer)(Object)this instanceof CowRenderer || (EntityRenderer)(Object)this instanceof MushroomCowRenderer)
            bovinesandbuttercups$cowBakeFunction = layer -> new CowModel(context.bakeLayer(layer));
    }

    @Override
    public Function<ModelLayerLocation, CowModel> bovinesandbuttercups$getMooshroomLayerBakeFunction() {
        return bovinesandbuttercups$cowBakeFunction;
    }
}
