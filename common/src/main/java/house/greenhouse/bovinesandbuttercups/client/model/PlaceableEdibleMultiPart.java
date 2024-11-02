package house.greenhouse.bovinesandbuttercups.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaceableEdibleMultiPart extends MultiPart {
    private final List<PlaceableEdibleSelector> edibleSelectors;

    public PlaceableEdibleMultiPart(List<PlaceableEdibleSelector> edibleSelectors) {
        super(BovinesBlocks.PLACEABLE_EDIBLE.getStateDefinition(), List.of());
        this.edibleSelectors = edibleSelectors;
    }

    public List<PlaceableEdibleSelector> getEdibleSelectors() {
        return edibleSelectors;
    }

    @Override
    public Set<MultiVariant> getMultiVariants() {
        Set<MultiVariant> set = Sets.newHashSet();

        for (PlaceableEdibleSelector selector : this.edibleSelectors)
            set.add(selector.variant());

        return set;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState state) {
        PlaceableEdibleMultiPartBakedModelBuilder multipartbakedmodel$builder = new PlaceableEdibleMultiPartBakedModelBuilder();

        for (PlaceableEdibleSelector selector : edibleSelectors) {
            BakedModel bakedmodel = selector.variant().bake(baker, spriteGetter, state);
            if (bakedmodel != null)
                multipartbakedmodel$builder.add(selector, bakedmodel);
        }

        return multipartbakedmodel$builder.build();
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return this.getEdibleSelectors().stream().flatMap(selector -> selector.variant().getDependencies().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof PlaceableEdibleMultiPart multipart && Objects.equals(this.edibleSelectors, multipart.edibleSelectors);
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
        this.getEdibleSelectors().forEach(selector -> selector.variant().resolveParents(resolver));
    }

    public static class Deserializer implements JsonDeserializer<PlaceableEdibleMultiPart> {
        public PlaceableEdibleMultiPart deserialize(JsonElement json, Type type, JsonDeserializationContext jsonContext) throws JsonParseException {
            var selectors = getSelectors(jsonContext, json.getAsJsonArray());
            return new PlaceableEdibleMultiPart(selectors);
        }

        private List<PlaceableEdibleSelector> getSelectors(JsonDeserializationContext jsonContext, JsonArray elements) {
            List<PlaceableEdibleSelector> list = Lists.newArrayList();

            for (JsonElement jsonelement : elements)
                list.add(jsonContext.deserialize(jsonelement, PlaceableEdibleSelector.class));

            return list;
        }
    }
}
