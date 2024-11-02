package house.greenhouse.bovinesandbuttercups.client.api.model.condition;

import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;

import java.util.List;

public record OrCondition(List<PlaceableEdibleCondition> conditions) implements PlaceableEdibleCondition {
    public boolean test(PlaceableEdibleBlockEntity blockEntity) {
        return conditions.stream().anyMatch(condition -> condition.test(blockEntity));
    }

    @Override
    public String toModelVariantString() {
        return "or." + String.join("-", conditions.stream().map(PlaceableEdibleCondition::toModelVariantString).toList());
    }
}
