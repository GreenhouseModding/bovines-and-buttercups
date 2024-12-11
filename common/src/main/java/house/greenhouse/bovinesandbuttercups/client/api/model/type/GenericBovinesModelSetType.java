package house.greenhouse.bovinesandbuttercups.client.api.model.type;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryModelBakerImplInvoker;
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
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GenericBovinesModelSetType implements BovinesModelSetType {
    public static final GenericBovinesModelSetType INSTANCE = new GenericBovinesModelSetType();

    private static final StateDefinition<Block, BlockState> EMPTY_STATE = (new StateDefinition.Builder<Block, BlockState>(Blocks.AIR)).create(Block::defaultBlockState, BlockState::new);
    private static final Map<ResourceLocation, JsonObject> LOADED_JSON = new HashMap<>();

    protected GenericBovinesModelSetType() {}

    @Override
    public BovinesModelSet createReference(ResourceLocation fileId, JsonObject json) {
        Map<ResourceLocation, ResourceLocation> modelIds = Map.of(fileId, fileId.withPath(s -> "bovinesandbuttercups/" + s));

        LOADED_JSON.put(modelIds.get(fileId), json);

        return new BovinesModelSet(fileId, this, modelIds, Map.of());
    }

    @Override
    public UnbakedModel createUnbaked(ResourceLocation modelId) {
        JsonObject json = LOADED_JSON.get(modelId);
        LOADED_JSON.remove(modelId);

        var blockStateJson = remapToBlockStateJson(json);
        if (blockStateJson == null)
            return MissingBlockModel.missingModel();

        Map<BlockState, UnbakedBlockStateModel> definition = BlockModelDefinition.fromJsonElement(blockStateJson).instantiate(EMPTY_STATE, modelId.toString());

        Optional<UnbakedBlockStateModel> variant = definition.values().stream().findFirst();
        if (variant.isEmpty())
            return MissingBlockModel.missingModel();

        return new UnbakedModel() {
            @Override
            public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
                return variant.get().bake(baker);
            }

            @Override
            public void resolveDependencies(Resolver resolver) {
                variant.get().resolveDependencies(resolver);
            }
        };
    }

    @Nullable
    private static JsonObject remapToBlockStateJson(JsonObject json) {
        if (json == null)
            return null;

        if (!json.has("model")) {
            BovinesAndButtercups.LOG.warn("\"bovinesandbuttercups:generic\" bovines model set does not have a \"model\" field. This field must be either a model's location or a multipart with \"state_type\" set to 'multipart'.");
            return null;
        }

        JsonObject root = new JsonObject();
        StateType stateType = StateType.VARIANT;
        if (json.has("state_type")) {
            var dataResult = StateType.CODEC.decode(JsonOps.INSTANCE, json.get("state_type"));
            if (dataResult.isError())
                BovinesAndButtercups.LOG.warn("Could not decode \"state_type\" field ({}) in \"bovinesandbuttercups:generic\" bovines model set. Must be either \"variant\" or \"multipart\". (Ignoring).", json.get("state_type"));
            else if (dataResult.getOrThrow().getFirst() == StateType.MULTIPART)
                stateType = StateType.MULTIPART;
        }
        if (stateType == StateType.MULTIPART) {
            if (!json.get("model").isJsonArray()) {
                BovinesAndButtercups.LOG.error("\"model\" field ({}) in multipart \"bovinesandbuttercups:generic\" is not a valid multipart.", json.get("model"));
                return null;
            }
            root.add("multipart", json.get("model"));
        } else {
            if (!json.get("model").isJsonPrimitive() || !json.get("model").getAsJsonPrimitive().isString()) {
                BovinesAndButtercups.LOG.error("\"model\" field ({}) in variant \"bovinesandbuttercups:generic\" is not a valid model location.", json.get("model"));
                return null;
            }
            JsonObject variants = new JsonObject();
            JsonObject model = new JsonObject();
            model.add("model", json.get("model"));
            variants.add("", model);
            root.add("variants", variants);
        }
        return root;
    }

    public enum StateType implements StringRepresentable {
        VARIANT("variant"),
        MULTIPART("multipart");

        public static final Codec<StateType> CODEC = StringRepresentable.fromEnum(StateType::values);
        private String name;

        StateType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
