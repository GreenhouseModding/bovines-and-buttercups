package house.greenhouse.bovinesandbuttercups.content.recipe.ingredient;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BovinesIngredients {
    public static void registerAll() {
        Registry.register(NeoForgeRegistries.INGREDIENT_TYPES, RemainderIngredient.ID, RemainderIngredientImpl.TYPE);
    }
}
