package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.recipe.ingredient.RemainderIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Recipe.class)
public interface RecipeMixin<T extends RecipeInput> {
    @Shadow NonNullList<Ingredient> getIngredients();

    @ModifyReturnValue(method = "getRemainingItems", at = @At("RETURN"))
    private NonNullList<ItemStack> bovinesandbuttercups$handleRemainderIngredients(NonNullList<ItemStack> original, T input) {
        if (input instanceof CraftingInput) {
            for (int i = 0; i < original.size(); i++) {
                if (getIngredients().size() - 1 < i)
                    continue;
                RemainderIngredient ingredient = BovinesAndButtercups.getHelper().getRemainderIngredient(getIngredients().get(i));
                if (ingredient != null)
                    original.set(i, ingredient.remainder().copy());
            }
        }
        return original;
    }
}
