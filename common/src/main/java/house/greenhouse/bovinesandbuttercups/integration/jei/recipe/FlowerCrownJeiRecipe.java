package house.greenhouse.bovinesandbuttercups.integration.jei.recipe;

import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.content.recipe.FlowerCrownRecipe;
import house.greenhouse.bovinesandbuttercups.integration.recipe.BovinesRecipeViewerUtil;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Random;

public class FlowerCrownJeiRecipe implements ICraftingCategoryExtension<FlowerCrownRecipe> {
    public void setRecipe(RecipeHolder<FlowerCrownRecipe> recipe, IRecipeLayoutBuilder builder, ICraftingGridHelper helper, IFocusGroup focuses) {
        ItemStack stack = FlowerCrownItem.createRainbowCrown(Minecraft.getInstance().level.registryAccess());
        Ingredient flowerCrownIngredient = Ingredient.of(Minecraft.getInstance().level.registryAccess().registryOrThrow(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL).stream().map(FlowerCrownMaterial::ingredient));
        List<Ingredient> ingredients = List.of(
                flowerCrownIngredient,
                flowerCrownIngredient,
                flowerCrownIngredient,
                flowerCrownIngredient,
                Ingredient.EMPTY,
                flowerCrownIngredient,
                flowerCrownIngredient,
                flowerCrownIngredient,
                flowerCrownIngredient
        );
        helper.createAndSetIngredients(builder, ingredients, 3, 3);
        helper.createAndSetOutputs(builder, List.of(stack));
    }

    public void onDisplayedIngredientsUpdate(RecipeHolder<FlowerCrownRecipe> recipe, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
        ItemStack flowerCrown = BovinesRecipeViewerUtil.generateRandomFlowerCrown(new Random(), BovinesItems.FLOWER_CROWN);
        FlowerCrown component = flowerCrown.get(BovinesDataComponents.FLOWER_CROWN);

        for (int i = 0; i < 10; ++i) {
            if (i == 4)
                continue;
            var drawable = recipeSlots.get(i);
            drawable.clearDisplayOverrides();
            drawable.createDisplayOverrides().addItemStack(i == 9 ? flowerCrown : i > 4 ? component.getMaterialForRecipeViewer(i - 1) : component.getMaterialForRecipeViewer(i));
        }
    }

    @Override
    public int getWidth(RecipeHolder<FlowerCrownRecipe> recipe) {
        return 3;
    }

    @Override
    public int getHeight(RecipeHolder<FlowerCrownRecipe> recipe) {
        return 3;
    }
}
