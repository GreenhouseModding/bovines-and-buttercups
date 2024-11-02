package house.greenhouse.bovinesandbuttercups.client.api.model.condition;

import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;

public interface PlaceableEdibleCondition {
    PlaceableEdibleCondition TRUE = new PlaceableEdibleCondition() {
        @Override
        public boolean test(PlaceableEdibleBlockEntity blockEntity) {
            return true;
        }

        @Override
        public String toModelVariantString() {
            return "true";
        }
    };

    boolean test(PlaceableEdibleBlockEntity blockEntity);

    String toModelVariantString();
}
