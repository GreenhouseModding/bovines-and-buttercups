package house.greenhouse.bovinesandbuttercups.client.api.model.type;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import house.greenhouse.bovinesandbuttercups.client.model.PlaceableEdibleMultiPart;
import house.greenhouse.bovinesandbuttercups.content.block.PlaceableEdibleBlock;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class EdibleBlockBovinesModelSetType extends InventoryBovinesModelSetType {
    public static final EdibleBlockBovinesModelSetType INSTANCE = new EdibleBlockBovinesModelSetType();
    private static final Map<ResourceLocation, BlockModelDefinition> LOADED = new HashMap<>();

    protected EdibleBlockBovinesModelSetType() {}

    public static BakedModel getItemModel(BovinesModelSet modelSet) {
        if (modelSet == null)
            return Minecraft.getInstance().getModelManager().getMissingModel();
        return modelSet.getModel(modelSet.id().withPath(s -> s + "/inventory"));
    }

    public static BakedModel getBlockModel(BovinesModelSet modelSet, PlaceableEdibleBlockEntity blockEntity) {
        if (modelSet == null)
            return Minecraft.getInstance().getModelManager().getMissingModel();
        if (!modelSet.lookupKeys().isEmpty() && modelSet.lookupKeys().getFirst() instanceof PlaceableEdibleSelector) {
            Optional<PlaceableEdibleSelector> first = modelSet.lookupKeys().stream().map(object -> (PlaceableEdibleSelector)object).filter(placeableEdibleSelector -> placeableEdibleSelector.test(blockEntity)).findFirst();
            if (first.isPresent())
                return modelSet.getModel(first.get());
        }
        return modelSet.getModel(blockEntity.getEdibleType().holder().unwrapKey().orElseThrow().location().withPath(acceptedProperties(blockEntity)));
    }

    @Override
    public BovinesModelSet createReference(ResourceLocation fileId, JsonObject json) {
        Map<ResourceLocation, ResourceLocation> modelIds = new HashMap<>();
        Map<Object, ResourceLocation> lookup = new HashMap<>();

        if (json.has("item_model")) {
            ResourceLocation itemModelLocation = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, json.get("item_model")).getOrThrow().getFirst();
            modelIds.put(fileId.withPath(s -> s + "/inventory"), itemModelLocation.withPath(s -> "bovinesandbuttercups/item/" + s + "/inventory"));
        }

        BlockModelDefinition definition = Deserializer.GSON.fromJson(json, BlockModelDefinition.class);

        if (definition != null) {
            if (definition.isMultiPart() && definition.getMultiPart() instanceof PlaceableEdibleMultiPart multiPart) {
                for (PlaceableEdibleSelector selector : multiPart.getEdibleSelectors()) {
                    ResourceLocation filePath = fileId.withPath(s -> s + "/" + selector.condition().toModelVariantString());
                    ResourceLocation resolvedPath = filePath.withPath(s -> "bovinesandbuttercups/" + s);
                    modelIds.put(filePath, resolvedPath);
                    lookup.put(selector, filePath);
                    LOADED.put(resolvedPath, definition);
                }
            }
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
            return ((ModelBakeryAccessor)BovinesAndButtercupsClient.getModelBakery()).bovinesandbuttercups$getMissingModel();
        }

        if (definition.isMultiPart())
            return definition.getMultiPart();

        String variant = getVariant(modelId);
        if (definition.getVariants().containsKey(variant))
            return definition.getVariants().get(variant);

        return definition.getVariants().get("");
    }

    private static String acceptedProperties(PlaceableEdibleBlockEntity blockEntity) {
        return "bites." + blockEntity.getBlockState().getValue(PlaceableEdibleBlock.BITES) + "-attachments." + getAttachmentAsProperties(blockEntity.attachmentsToString());
    }

    private static String getAttachmentAsProperties(String attachments) {
        return attachments.replaceAll("=", ".").replaceAll(",", "-").replaceAll("#", "tag.");
    }

    private static String getVariant(ResourceLocation modelId) {
        return modelId.toString().replaceAll("\\.", ".").replaceAll("-", ",").replaceAll("tag.", "#");
    }

    public static class Deserializer implements JsonDeserializer<BlockModelDefinition> {
        public static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(BlockModelDefinition.class, new Deserializer())
                .registerTypeAdapter(Variant.class, new Variant.Deserializer())
                .registerTypeAdapter(MultiVariant.class, new MultiVariant.Deserializer())
                .registerTypeAdapter(PlaceableEdibleMultiPart.class, new PlaceableEdibleMultiPart.Deserializer())
                .registerTypeAdapter(PlaceableEdibleSelector.class, new PlaceableEdibleSelector.Deserializer())
                .create();

        public BlockModelDefinition deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonobject = json.getAsJsonObject();
            Map<String, MultiVariant> map = getVariants(context, jsonobject);
            PlaceableEdibleMultiPart multipart = getMultiPart(context, jsonobject);
            if (!map.isEmpty() || multipart != null && !multipart.getMultiVariants().isEmpty()) {
                return new BlockModelDefinition(map, multipart);
            } else {
                throw new JsonParseException("Neither 'variants' nor 'multipart' found");
            }
        }

        protected Map<String, MultiVariant> getVariants(JsonDeserializationContext context, JsonObject json) {
            Map<String, MultiVariant> map = Maps.newHashMap();
            if (json.has("variants")) {
                JsonObject jsonobject = GsonHelper.getAsJsonObject(json, "variants");

                for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    map.put(entry.getKey(), context.deserialize(entry.getValue(), MultiVariant.class));
                }
            }

            return map;
        }

        @Nullable
        protected PlaceableEdibleMultiPart getMultiPart(JsonDeserializationContext context, JsonObject json) {
            if (!json.has("multipart")) {
                return null;
            } else {
                JsonArray jsonarray = GsonHelper.getAsJsonArray(json, "multipart");
                return context.deserialize(jsonarray, PlaceableEdibleMultiPart.class);
            }
        }
    }
}
