package house.greenhouse.bovinesandbuttercups.client.api.model.condition;

import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;

import java.util.List;

public record AndCondition(List<PlaceableEdibleCondition> conditions) implements PlaceableEdibleCondition {
    public boolean test(PlaceableEdibleBlockEntity blockEntity) {
        return conditions.stream().allMatch(condition -> condition.test(blockEntity));
    }

    @Override
    public String toModelVariantString() {
        return "and." + String.join("-", conditions.stream().map(PlaceableEdibleCondition::toModelVariantString).toList());
    }
}
