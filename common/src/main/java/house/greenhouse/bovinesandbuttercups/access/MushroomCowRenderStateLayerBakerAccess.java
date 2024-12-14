package house.greenhouse.bovinesandbuttercups.access;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;

import java.util.function.Function;

public interface MushroomCowRenderStateLayerBakerAccess {
    void bovinesandbuttercups$setLayerBakeFunction(Function<ModelLayerLocation, CowModel> bakeFunction);
}
