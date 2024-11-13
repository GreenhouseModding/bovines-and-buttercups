package house.greenhouse.bovinesandbuttercups.integration.jei.recipe;

import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdible;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.content.recipe.FlowerCrownRecipe;
import house.greenhouse.bovinesandbuttercups.content.recipe.SuspiciousEdibleRecipe;
import house.greenhouse.bovinesandbuttercups.integration.recipe.BovinesRecipeViewerUtil;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

import java.util.List;
import java.util.Random;

public class SuspiciousEdibleJeiRecipe implements ICraftingCategoryExtension<SuspiciousEdibleRecipe> {
    private int width;
    private int height;

    public void setRecipe(RecipeHolder<SuspiciousEdibleRecipe> recipe, IRecipeLayoutBuilder builder, ICraftingGridHelper helper, IFocusGroup focuses) {
        this.width = recipe.value().getPattern().width();
        this.height = recipe.value().getPattern().height();
        helper.createAndSetIngredients(builder, recipe.value().getPattern().ingredients(), width, height);

        ItemStack output = new ItemStack(BovinesItems.PLACEABLE_EDIBLE);
        output.set(BovinesDataComponents.EDIBLE_TYPE, new ItemEdible(recipe.value().getEdibleType(), List.of()));

        helper.createAndSetOutputs(builder, List.of(output));
    }

    public void onDisplayedIngredientsUpdate(RecipeHolder<SuspiciousEdibleRecipe> recipe, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
        SuspiciousEffectHolder effectHolder = SuspiciousEffectHolder.getAllEffectHolders().get(new Random().nextInt(SuspiciousEffectHolder.getAllEffectHolders().size()));

        for (int i = 0; i < 10; ++i) {
            if (i == 4)
                continue;
            var drawable = recipeSlots.get(i);
            if (i == 9) {
                drawable.clearDisplayOverrides();
                ItemStack stack = new ItemStack(BovinesItems.PLACEABLE_EDIBLE);
                stack.set(BovinesDataComponents.EDIBLE_TYPE, new ItemEdible(recipe.value().getEdibleType(), effectHolder.getSuspiciousEffects().effects().stream().map(entry -> new ItemEdible.MobEffectEntry(new MobEffectInstance(entry.effect(), Mth.ceil((float) entry.duration() / 4)), entry.duration(), ItemEdible.MobEffectEntry.ShowTooltip.CREATIVE_MENU_ONLY)).toList()));
                drawable.createDisplayOverrides().addItemStack(stack);
            } else {
                List<ItemStack> stacks = drawable.getItemStacks().toList();
                if (stacks.size() == 1 && drawable.getItemStacks().anyMatch(stack -> stack.is(Items.SUSPICIOUS_STEW))) {
                    drawable.clearDisplayOverrides();
                    ItemStack stack = new ItemStack(Items.SUSPICIOUS_STEW);
                    stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effectHolder.getSuspiciousEffects());
                    drawable.createDisplayOverrides().addItemStack(stack);
                }
            }
        }
    }

    @Override
    public int getWidth(RecipeHolder<SuspiciousEdibleRecipe> recipe) {
        return width;
    }

    @Override
    public int getHeight(RecipeHolder<SuspiciousEdibleRecipe> recipe) {
        return height;
    }
}
