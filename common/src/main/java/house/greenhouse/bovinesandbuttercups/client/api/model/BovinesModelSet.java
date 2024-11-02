package house.greenhouse.bovinesandbuttercups.client.api.model;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.BovinesModelSetType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class BovinesModelSet {
    private final BovinesModelSetType type;
    private final Map<ResourceLocation, ResourceLocation> modelMap;
    private final ResourceLocation id;
    private final Map<Object, ResourceLocation> lookup;
    private final Set<ResourceLocation> warnedKeys = new HashSet<>();

    public BovinesModelSet(ResourceLocation id, BovinesModelSetType type, Map<ResourceLocation, ResourceLocation> modelMap, Map<Object, ResourceLocation> lookup) {
        this.id = id;
        this.type = type;
        this.modelMap = modelMap;
        this.lookup = lookup;
    }

    public BakedModel getModel() {
        return getModel(id);
    }

    public BakedModel getModel(Object lookupObj) {
        return getModel(lookupObj, () -> "Bovines Model Set \"" + id + "\" could not find model from object: " + lookupObj);
    }

    public BakedModel getModel(ResourceLocation modelId) {
        return getModel(modelId, () -> "Bovines Model Set \"" + id + "\" does not contain model with path \"" + modelId + "\"");
    }

    public BakedModel getModel(Object lookupObj, Supplier<String> errorMessage) {
        return getModel(lookup.get(lookupObj), errorMessage);
    }

    public BakedModel getModel(ResourceLocation modelId, Supplier<String> errorMessage) {
        if (!modelMap.containsKey(modelId)) {
            if (!warnedKeys.contains(modelId))
                BovinesAndButtercups.LOG.warn(errorMessage.get());
            warnedKeys.add(modelId);
            return Minecraft.getInstance().getModelManager().getMissingModel();
        }
        return BovinesAndButtercupsClient.getHelper().getModel(modelMap.get(modelId));
    }

    public List<Object> lookupKeys() {
        return List.copyOf(lookup.keySet());
    }

    public List<ResourceLocation> resolvedModelPaths() {
        return List.copyOf(modelMap.values());
    }

    public BovinesModelSetType type() {
        return type;
    }

    public ResourceLocation id() {
        return id;
    }
}
