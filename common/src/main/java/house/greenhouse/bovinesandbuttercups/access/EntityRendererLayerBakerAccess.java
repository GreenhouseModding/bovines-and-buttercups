package house.greenhouse.bovinesandbuttercups.access;

import net.minecraft.client.model.geom.ModelLayerLocation;

import java.util.function.Function;

public interface EntityRendererLayerBakerAccess<M> {
    Function<ModelLayerLocation, M> bovinesandbuttercups$getLayerBakeFunction();
}
