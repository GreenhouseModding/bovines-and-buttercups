package house.greenhouse.bovinesandbuttercups.content.recipe.ingredient;

import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BovinesIngredients {
    public static void registerAll(RegistrationCallback<IngredientType<?>> callback) {
        callback.register(NeoForgeRegistries.INGREDIENT_TYPES, RemainderIngredient.ID, RemainderIngredientImpl.TYPE);
    }
}
