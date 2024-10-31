package house.greenhouse.bovinesandbuttercups.client.api.model.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StateDefinitionBovinesModelSetType extends InventoryBovinesModelSetType {
    private static final Map<ResourceLocation, JsonElement> LOADED_JSON = new HashMap<>();
    private final BlockModelDefinition.Context context = new BlockModelDefinition.Context();

    public StateDefinitionBovinesModelSetType(StateDefinition<Block, BlockState> definition) {
        context.setDefinition(definition);
    }

    public static BakedModel getItemModel(BovinesModelSet modelSet) {
        return modelSet.getModel(modelSet.id().withPath(s -> s + "/inventory"));
    }

    public static BakedModel getBlockModel(BovinesModelSet modelSet, BlockState state) {
        return modelSet.getModel(modelSet.id().withPath(s -> s + "/" + acceptedStateProperties(BlockModelShaper.statePropertiesToString(state.getValues()))));
    }

    @Override
    public BovinesModelSet createReference(ResourceLocation fileId, JsonObject json) {
        Map<ResourceLocation, ResourceLocation> modelIds = new HashMap<>();

        if (json.has("item_model")) {
            ResourceLocation itemModelLocation = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, json.get("item_model")).getOrThrow().getFirst();
            modelIds.put(fileId.withPath(s -> s + "/inventory"), itemModelLocation.withPath(s -> "bovinesandbuttercups/item/" + s + "/inventory"));
        }

        for (BlockState state : context.getDefinition().getPossibleStates()) {
            ResourceLocation stateResource = fileId.withPath(s ->
                    s + "/" + acceptedStateProperties(BlockModelShaper.statePropertiesToString(state.getValues()))
            );
            ResourceLocation resolvedResource = stateResource.withPath(s -> "bovinesandbuttercups/" + s);
            LOADED_JSON.put(resolvedResource, json);
            modelIds.put(stateResource, resolvedResource);
        }
        return new BovinesModelSet(fileId, this, modelIds);
    }

    @Override
    public UnbakedModel createUnbaked(ResourceLocation modelId, Function<ResourceLocation, UnbakedModel> itemModelLoader) {
        if (modelId.getPath().endsWith("/inventory"))
            return super.createUnbaked(modelId, itemModelLoader);

        JsonElement json = LOADED_JSON.get(modelId);
        LOADED_JSON.remove(modelId);

        BlockModelDefinition definition = BlockModelDefinition.fromJsonElement(context, json);

        if (definition.isMultiPart())
            return definition.getMultiPart();

        String variant = getVariant(modelId);
        if (definition.getVariants().containsKey(variant))
            return definition.getVariants().get(variant);

        return definition.getVariants().get("");
    }

    private static String acceptedStateProperties(String stateProperties) {
        return stateProperties.replaceAll("=", ".").replaceAll(",", "-");
    }

    private static String getVariant(ResourceLocation modelId) {
        String path = modelId.getPath();
        if (path.lastIndexOf("/") == path.length() - 1)
            return "";
        return path.substring(path.lastIndexOf("/") + 1).replaceAll("\\.", "=").replaceAll("-", ",");
    }
}
