package house.greenhouse.bovinesandbuttercups.client.api.model;

import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryAccessor;
import net.minecraft.client.resources.model.UnbakedModel;

public class BovinesModelUtil {
    public static final UnbakedModel MISSING_MODEL = ((ModelBakeryAccessor) BovinesAndButtercupsClient.getModelBakery()).bovinesandbuttercups$getMissingModel();
}
