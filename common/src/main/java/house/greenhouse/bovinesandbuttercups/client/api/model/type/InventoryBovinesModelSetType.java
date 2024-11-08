package house.greenhouse.bovinesandbuttercups.client.api.model.type;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelUtil;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Function;

public class InventoryBovinesModelSetType implements BovinesModelSetType {
    public static final InventoryBovinesModelSetType INSTANCE = new InventoryBovinesModelSetType();

    protected InventoryBovinesModelSetType() {

    }

    @Override
    public BovinesModelSet createReference(ResourceLocation fileId, JsonObject json) {
        ResourceLocation itemModelLocation = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, json.get("item_model")).getOrThrow().getFirst();
        return new BovinesModelSet(fileId, this, Map.of(fileId, itemModelLocation.withPath(s -> "bovinesandbuttercups/item/" + s + "/inventory")), Map.of());
    }

    @Override
    public UnbakedModel createUnbaked(ResourceLocation modelId, Function<ResourceLocation, UnbakedModel> itemModelLoader) {
        ResourceLocation itemModelId = modelId.withPath(s ->
                s.substring(21, s.length() - 10));
        UnbakedModel model = itemModelLoader.apply(itemModelId);
        if (model == null) {
            BovinesAndButtercups.LOG.warn("Failed to load item model {} defaulting to missing model.", itemModelId);
            return BovinesModelUtil.MISSING_MODEL;
        }
        return model;
    }

}
