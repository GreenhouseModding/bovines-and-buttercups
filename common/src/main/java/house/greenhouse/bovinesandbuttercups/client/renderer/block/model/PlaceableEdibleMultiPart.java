package house.greenhouse.bovinesandbuttercups.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaceableEdibleMultiPart extends MultiPart {
    private final List<PlaceableEdibleSelector> edibleSelectors;

    public PlaceableEdibleMultiPart(List<PlaceableEdibleSelector> edibleSelectors) {
        super(List.of());
        this.edibleSelectors = edibleSelectors;
    }

    public List<PlaceableEdibleSelector> getEdibleSelectors() {
        return edibleSelectors;
    }

    @Override
    public Object visualEqualityGroup(BlockState state) {
        throw new UnsupportedOperationException("PlaceableEdibleMultiPart does not support MultiPart#visualEqualityGroup");
    }

    @Override
    public void resolveDependencies(ResolvableModel.Resolver resolver) {
        this.edibleSelectors.forEach(selector -> selector.variant().resolveDependencies(resolver));
    }

    @Override
    public BakedModel bake(ModelBaker modelBaker) {
        List<Pair<PlaceableEdibleSelector, BakedModel>> list = new ArrayList<>(this.edibleSelectors.size());

        for (PlaceableEdibleSelector selector : this.edibleSelectors) {
            BakedModel bakedmodel = selector.variant().bake(modelBaker);
            list.add(Pair.of(selector, bakedmodel));
        }

        return BovinesAndButtercupsClient.getHelper().createPlaceableEdibleModel(list);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof PlaceableEdibleMultiPart multipart && Objects.equals(this.edibleSelectors, multipart.edibleSelectors);
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
