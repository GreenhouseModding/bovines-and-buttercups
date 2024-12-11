package house.greenhouse.bovinesandbuttercups.client.api.model.type;

import com.google.gson.JsonObject;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryModelBakerImplInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MissingBlockModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.HashMap;
import java.util.Map;

public class StateDefinitionBovinesModelSetType implements BovinesModelSetType {
    private static final Map<ResourceLocation, UnbakedBlockStateModel> LOADED = new HashMap<>();
    private final StateDefinition<Block, BlockState> definition;

    public StateDefinitionBovinesModelSetType(StateDefinition<Block, BlockState> definition) {
        this.definition = definition;
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

        Map<BlockState, UnbakedBlockStateModel> def = BlockModelDefinition.fromJsonElement(json).instantiate(definition, fileId.toString());

        for (Map.Entry<BlockState, UnbakedBlockStateModel> state : def.entrySet()) {
            ResourceLocation stateResource = fileId.withPath(s ->
                    s + "/" + acceptedProperties(BlockModelShaper.statePropertiesToString(state.getKey().getValues()))
            );
            ResourceLocation resolvedResource = stateResource.withPath(s -> "bovinesandbuttercups/" + s);
            modelIds.put(stateResource, resolvedResource);
            lookup.put(state, stateResource);
            LOADED.put(resolvedResource, state.getValue());
        }
        return new BovinesModelSet(fileId, this, modelIds, lookup);
    }

    @Override
    public UnbakedModel createUnbaked(ResourceLocation modelId) {
        UnbakedBlockStateModel model = LOADED.get(modelId);
        LOADED.remove(modelId);

        if (model == null || definition == null) {
            BovinesAndButtercups.LOG.warn("Failed to load model {} defaulting to missing model.", modelId);
            return MissingBlockModel.missingModel();
        }

        return new UnbakedModel() {
            @Override
            public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
                return model.bake(baker);
            }

            @Override
            public void resolveDependencies(Resolver resolver) {
                model.resolveDependencies(resolver);
            }
        };
    }

    private static String acceptedProperties(String stateProperties) {
        return stateProperties.replaceAll("=", ".").replaceAll(",", "-");
    }
}
