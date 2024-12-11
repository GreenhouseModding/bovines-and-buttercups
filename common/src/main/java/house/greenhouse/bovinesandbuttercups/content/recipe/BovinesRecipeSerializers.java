package house.greenhouse.bovinesandbuttercups.content.recipe;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BovinesRecipeSerializers {
    public static final RecipeSerializer<FlowerCrownRecipe> FLOWER_CROWN = new CustomRecipe.Serializer<>(FlowerCrownRecipe::new);
    public static final RecipeSerializer<SuspiciousEdibleRecipe> SUSPICIOUS_EDIBLE = new SuspiciousEdibleRecipe.Serializer();

    public static void registerAll(RegistrationCallback<RecipeSerializer<?>> callback) {
        callback.register(BuiltInRegistries.RECIPE_SERIALIZER, BovinesAndButtercups.asResource("crafting_special_flowercrown"), FLOWER_CROWN);
        callback.register(BuiltInRegistries.RECIPE_SERIALIZER, BovinesAndButtercups.asResource("crafting_special_suspicious_edible"), SUSPICIOUS_EDIBLE);
    }
}
