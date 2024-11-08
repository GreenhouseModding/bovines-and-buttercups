package house.greenhouse.bovinesandbuttercups.client.api.model.type;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelUtil;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryAccessor;
import net.minecraft.client.Minecraft;
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
    private static final Map<ResourceLocation, BlockModelDefinition> LOADED = new HashMap<>();
    private final BlockModelDefinition.Context context = new BlockModelDefinition.Context();

    public StateDefinitionBovinesModelSetType(StateDefinition<Block, BlockState> definition) {
        context.setDefinition(definition);
    }

    public static BakedModel getItemModel(BovinesModelSet modelSet) {
        if (modelSet == null)
            return Minecraft.getInstance().getModelManager().getMissingModel();
        return modelSet.getModel(modelSet.id().withPath(s -> s + "/inventory"));
    }

    public static BakedModel getBlockModel(BovinesModelSet modelSet, BlockState state) {
        if (modelSet == null)
            return Minecraft.getInstance().getModelManager().getMissingModel();
        return modelSet.getModel(state, null, () -> "Could not get blockstate bovines model set for block \"" + state.getBlockHolder().unwrapKey().orElseThrow().location() + "\" and for type \"" + modelSet.id() + "\" with properties \"" + acceptedProperties(BlockModelShaper.stateToModelLocation(state).getVariant()) + "\".");
    }

    @Override
    public BovinesModelSet createReference(ResourceLocation fileId, JsonObject json) {
        Map<ResourceLocation, ResourceLocation> modelIds = new HashMap<>();
        Map<Object, ResourceLocation> lookup = new HashMap<>();

        if (json.has("item_model")) {
            ResourceLocation itemModelLocation = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, json.get("item_model")).getOrThrow().getFirst();
            modelIds.put(fileId.withPath(s -> s + "/inventory"), itemModelLocation.withPath(s -> "bovinesandbuttercups/item/" + s + "/inventory"));
        }

        BlockModelDefinition definition = BlockModelDefinition.fromJsonElement(context, json);

        for (BlockState state : context.getDefinition().getPossibleStates()) {
            ResourceLocation stateResource = fileId.withPath(s ->
                    s + "/" + acceptedProperties(BlockModelShaper.statePropertiesToString(state.getValues()))
            );
            ResourceLocation resolvedResource = stateResource.withPath(s -> "bovinesandbuttercups/" + s);
            modelIds.put(stateResource, resolvedResource);
            lookup.put(state, stateResource);
            LOADED.put(resolvedResource, definition);
        }
        return new BovinesModelSet(fileId, this, modelIds, lookup);
    }

    @Override
    public UnbakedModel createUnbaked(ResourceLocation modelId, Function<ResourceLocation, UnbakedModel> itemModelLoader) {
        if (modelId.getPath().endsWith("/inventory"))
            return super.createUnbaked(modelId, itemModelLoader);

        BlockModelDefinition definition = LOADED.get(modelId);
        LOADED.remove(modelId);

        if (definition == null) {
            BovinesAndButtercups.LOG.warn("Failed to load model {} defaulting to missing model.", modelId);
            return BovinesModelUtil.MISSING_MODEL;
        }

        if (definition.isMultiPart())
            return definition.getMultiPart();

        String variant = getVariant(modelId);
        if (definition.getVariants().containsKey(variant))
            return definition.getVariants().get(variant);

        return definition.getVariants().get("");
    }

    private static String acceptedProperties(String stateProperties) {
        return stateProperties.replaceAll("=", ".").replaceAll(",", "-");
    }

    private static String getVariant(ResourceLocation modelId) {
        String path = modelId.getPath();
        if (path.lastIndexOf("/") == path.length() - 1)
            return "";
        return path.substring(path.lastIndexOf("/") + 1).replaceAll("\\.", "=").replaceAll("-", ",");
    }
}
