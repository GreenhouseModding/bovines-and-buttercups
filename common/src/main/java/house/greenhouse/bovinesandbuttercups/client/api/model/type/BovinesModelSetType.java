package house.greenhouse.bovinesandbuttercups.client.api.model.type;

import com.google.gson.JsonObject;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface BovinesModelSetType {
    BovinesModelSet createReference(ResourceLocation fileId, JsonObject json);

    UnbakedModel createUnbaked(ResourceLocation modelId, Function<ResourceLocation, UnbakedModel> itemModelLoader);
}
