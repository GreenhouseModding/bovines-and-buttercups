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
import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.model.PlaceableEdibleMultiPart;
import house.greenhouse.bovinesandbuttercups.content.block.PlaceableEdibleBlock;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryModelBakerImplInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MissingBlockModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EdibleBlockBovinesModelSetType implements BovinesModelSetType {
    public static final EdibleBlockBovinesModelSetType INSTANCE = new EdibleBlockBovinesModelSetType();
    private static final Map<ResourceLocation, EdibleModelDefinition> LOADED = new HashMap<>();

    protected EdibleBlockBovinesModelSetType() {}

    public static BakedModel getBlockModel(BovinesModelSet modelSet, PlaceableEdibleBlockEntity blockEntity) {
        if (modelSet == null || blockEntity.getEdibleType() == null)
            return Minecraft.getInstance().getModelManager().getMissingModel();
        if (!modelSet.lookupKeys().isEmpty() && modelSet.lookupKeys().getFirst() instanceof PlaceableEdibleSelector) {
            Optional<PlaceableEdibleSelector> first = modelSet.lookupKeys().stream().map(object -> (PlaceableEdibleSelector)object).filter(placeableEdibleSelector -> placeableEdibleSelector.test(blockEntity)).findFirst();
            if (first.isPresent())
                return modelSet.getModel(first.get());
        }
        return modelSet.getModel(blockEntity.getEdibleType().holder().unwrapKey().orElseThrow().location().withPath(s -> s + "/" + acceptedProperties(blockEntity)), blockEntity.getEdibleType().holder().unwrapKey().orElseThrow().location().withPath(s -> s + "/"), () -> "Could not get edible block bovines model set for type \"" + modelSet.id() + "\" with properties \"" + acceptedProperties(blockEntity) + "\".");
    }

    @Override
    public BovinesModelSet createReference(ResourceLocation fileId, JsonObject json) {
        Map<ResourceLocation, ResourceLocation> modelIds = new HashMap<>();
        Map<Object, ResourceLocation> lookup = new HashMap<>();

        EdibleModelDefinition definition = EdibleModelDefinition.GSON.fromJson(json, EdibleModelDefinition.class);

        if (definition != null) {
            if (definition.multiPart != null) {
                for (PlaceableEdibleSelector selector : definition.multiPart.getEdibleSelectors()) {
                    ResourceLocation filePath = fileId.withPath(s -> s + "/" + selector.condition().toModelVariantString());
                    ResourceLocation resolvedPath = filePath.withPath(s -> "bovinesandbuttercups/" + s);
                    modelIds.put(filePath, resolvedPath);
                    lookup.put(selector, filePath);
                    LOADED.put(resolvedPath, definition);
                }
            } else if (!definition.variants.isEmpty()) {
                Map<ResourceLocation, ResourceLocation> locations = definition.variants.keySet().stream().map(multiVariant -> {
                    ResourceLocation filePath = fileId.withPath(s -> s + "/" + mapVariant(multiVariant));
                    ResourceLocation resolvedPath = filePath.withPath(s -> "bovinesandbuttercups/" + s);
                    return Pair.of(filePath, resolvedPath);
                }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

                modelIds.putAll(locations);
                locations.values().forEach(rl -> LOADED.put(rl, definition));
            }
        }

        return new BovinesModelSet(fileId, this, modelIds, lookup);
    }

    @Override
    public UnbakedModel createUnbaked(ResourceLocation modelId) {
        EdibleModelDefinition definition = LOADED.get(modelId);
        LOADED.remove(modelId);

        if (definition == null) {
            BovinesAndButtercups.LOG.warn("Failed to load model {} defaulting to missing model.", modelId);
            return MissingBlockModel.missingModel();
        }

        if (definition.multiPart() != null) {
            return new UnbakedModel() {
                @Override
                public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
                    return definition.multiPart().bake(baker);
                }

                @Override
                public void resolveDependencies(Resolver resolver) {
                    definition.multiPart().resolveDependencies(resolver);
                }
            };
        }

        String variant = getVariant(modelId);
        MultiVariant multiVariant;
        if (definition.variants().containsKey(variant))
            multiVariant = definition.variants().get(variant);
        else
            multiVariant = definition.variants().get("");

        if (multiVariant == null) {
            BovinesAndButtercups.LOG.warn("Failed to load model {} with variant {} defaulting to missing model.", modelId.withPath(s -> s.replace("/" + mapVariant(variant), "")), variant);
            return MissingBlockModel.missingModel();
        }

        return new UnbakedModel() {
            @Override
            public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
                return multiVariant.bake(baker);
            }

            @Override
            public void resolveDependencies(Resolver resolver) {
                multiVariant.resolveDependencies(resolver);
            }
        };
    }

    private static String acceptedProperties(PlaceableEdibleBlockEntity blockEntity) {
        String attachments = mapVariant(blockEntity.attachmentsToString());
        return "bites." + blockEntity.getBlockState().getValue(PlaceableEdibleBlock.BITES) + (attachments.isEmpty() ? "" : "-attachments." + mapVariant(blockEntity.attachmentsToString()));
    }

    private static String mapVariant(String variant) {
        return variant.replaceAll("#", "tag.").replace(":", ".separator.").replaceAll("=", ".").replaceAll(",", "-");
    }

    private static String getVariant(ResourceLocation modelId) {
        String path = modelId.getPath();
        if (path.lastIndexOf("/") == path.length() - 1)
            return "";
        return path.substring(path.lastIndexOf("/") + 1).replaceAll("tag.", "#").replace(".separator.", ":").replaceAll("\\.", "=").replaceAll("-", ",");
    }

    private record EdibleModelDefinition(Map<String, MultiVariant> variants, @Nullable PlaceableEdibleMultiPart multiPart) {
        public static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(EdibleModelDefinition.class, new EdibleModelDefinition.Deserializer())
                .registerTypeAdapter(Variant.class, new Variant.Deserializer())
                .registerTypeAdapter(MultiVariant.class, new MultiVariant.Deserializer())
                .registerTypeAdapter(PlaceableEdibleMultiPart.class, new PlaceableEdibleMultiPart.Deserializer())
                .registerTypeAdapter(PlaceableEdibleSelector.class, new PlaceableEdibleSelector.Deserializer())
                .create();

        public static class Deserializer implements JsonDeserializer<EdibleModelDefinition> {
            @Override
            public EdibleModelDefinition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonobject = json.getAsJsonObject();
                Map<String, MultiVariant> map = this.getVariants(context, jsonobject);
                PlaceableEdibleMultiPart multipart$definition = getMultiPart(context, jsonobject);
                if (map.isEmpty() && multipart$definition == null) {
                    throw new JsonParseException("Neither 'variants' nor 'multipart' found");
                } else {
                    return new EdibleModelDefinition(map, multipart$definition);
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
}
