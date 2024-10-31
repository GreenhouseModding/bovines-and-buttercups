package house.greenhouse.bovinesandbuttercups.client.api.model;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.BovinesModelSetType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BovinesModelSet {
    private final BovinesModelSetType type;
    private final Map<ResourceLocation, ResourceLocation> modelMap;
    private final ResourceLocation id;
    private final Set<ResourceLocation> warnedKeys = new HashSet<>();

    public BovinesModelSet(ResourceLocation id, BovinesModelSetType type, Map<ResourceLocation, ResourceLocation> modelMap) {
        this.id = id;
        this.type = type;
        this.modelMap = modelMap;
    }

    public BakedModel getModel() {
        return getModel(id);
    }

    public BakedModel getModel(ResourceLocation modelId) {
        if (!modelMap.containsKey(modelId)) {
            if (!warnedKeys.contains(modelId))
                BovinesAndButtercups.LOG.error("Bovines Model Set \"{}\" does not contain model with path \"{}\"", id, modelId);
            warnedKeys.add(modelId);
            return Minecraft.getInstance().getModelManager().getMissingModel();
        }
        return BovinesAndButtercupsClient.getHelper().getModel(modelMap.get(modelId));
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
