package house.greenhouse.bovinesandbuttercups.content.recipe;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class BovinesRecipeSerializers {
    public static final RecipeSerializer<FlowerCrownRecipe> FLOWER_CROWN = new SimpleCraftingRecipeSerializer<>(FlowerCrownRecipe::new);

    public static void registerAll(RegistrationCallback<RecipeSerializer<?>> callback) {
        callback.register(BuiltInRegistries.RECIPE_SERIALIZER, BovinesAndButtercups.asResource("crafting_special_flowercrown"), FLOWER_CROWN);
    }
}
