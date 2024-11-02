package house.greenhouse.bovinesandbuttercups.client.model;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import net.minecraft.client.resources.model.BakedModel;

import java.util.List;

public class PlaceableEdibleMultiPartBakedModelBuilder {
    private final List<Pair<PlaceableEdibleSelector, BakedModel>> selectors = Lists.newArrayList();

    public void add(PlaceableEdibleSelector predicate, BakedModel model) {
        this.selectors.add(Pair.of(predicate, model));
    }

    public BakedModel build() {
        return BovinesAndButtercupsClient.getHelper().createPlaceableEdibleModel(selectors);
    }
}