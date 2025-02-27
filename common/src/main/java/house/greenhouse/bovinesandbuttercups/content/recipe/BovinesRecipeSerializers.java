package house.greenhouse.bovinesandbuttercups.content.recipe;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class BovinesRecipeSerializers {
    public static final RecipeSerializer<FlowerCrownRecipe> FLOWER_CROWN = register(BovinesAndButtercups.asResource("crafting_special_flowercrown"), new SimpleCraftingRecipeSerializer<>(FlowerCrownRecipe::new));
    public static final RecipeSerializer<SuspiciousEdibleRecipe> SUSPICIOUS_EDIBLE = register(BovinesAndButtercups.asResource("crafting_special_suspicious_edible"), new SuspiciousEdibleRecipe.Serializer());

    public static void registerAll() {}

    private static <T extends Recipe<?>> RecipeSerializer<T> register(ResourceLocation id, RecipeSerializer<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);
    }
}
