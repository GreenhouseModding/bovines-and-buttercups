package house.greenhouse.bovinesandbuttercups.access;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.animal.MushroomCow;

import java.util.function.Function;

public interface EntityRendererLayerBakerAccess {
    Function<ModelLayerLocation, CowModel<MushroomCow>> bovinesandbuttercups$getMooshroomLayerBakeFunction();
}
