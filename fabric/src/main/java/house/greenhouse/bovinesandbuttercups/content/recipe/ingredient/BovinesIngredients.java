package house.greenhouse.bovinesandbuttercups.content.recipe.ingredient;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;

public class BovinesIngredients {
    public static void init() {
        CustomIngredientSerializer.register(RemainderIngredientImpl.SERIALIZER);
    }
}
