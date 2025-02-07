package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.recipe.ingredient.RemainderIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CraftingRecipe.class)
public interface CraftingRecipeMixin extends Recipe<CraftingInput> {
    @ModifyReturnValue(method = "getRemainingItems", at = @At("RETURN"))
    private NonNullList<ItemStack> bovinesandbuttercups$handleRemainderIngredients(NonNullList<ItemStack> original, @Local(argsOnly = true) CraftingInput input) {
        if (!placementInfo().isImpossibleToPlace()) {
            for (int i = 0; i < original.size(); i++) {
                int ingredientIndex = placementInfo().slotsToIngredientIndex().getInt(i);
                if (ingredientIndex == -1)
                    continue;
                RemainderIngredient ingredient = BovinesAndButtercups.getHelper().getRemainderIngredient(placementInfo().ingredients().get(ingredientIndex));
                if (ingredient != null)
                    original.set(i, ingredient.remainder().copy());
            }
        }
        return original;
    }
}
