//package house.greenhouse.bovinesandbuttercups.integration.emi;
//
//import dev.emi.emi.api.EmiApi;
//import dev.emi.emi.api.EmiEntrypoint;
//import dev.emi.emi.api.EmiPlugin;
//import dev.emi.emi.api.EmiRegistry;
//import dev.emi.emi.api.recipe.EmiRecipe;
//import dev.emi.emi.api.stack.Comparison;
//import dev.emi.emi.api.stack.EmiStack;
//import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
//import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
//import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
//import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
//import house.greenhouse.bovinesandbuttercups.content.recipe.SuspiciousEdibleRecipe;
//import house.greenhouse.bovinesandbuttercups.content.recipe.ingredient.RemainderIngredient;
//import house.greenhouse.bovinesandbuttercups.integration.emi.recipe.FlowerCrownEmiRecipe;
//import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
//import house.greenhouse.bovinesandbuttercups.integration.emi.recipe.SuspiciousEdibleEmiRecipe;
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.item.crafting.CraftingRecipe;
//import net.minecraft.world.item.crafting.RecipeHolder;
//import net.minecraft.world.item.crafting.RecipeType;
//
//@EmiEntrypoint
//public class BovinesEmiPlugin implements EmiPlugin {
//
//    @Override
//    public void register(EmiRegistry registry) {
//        registry.setDefaultComparison(EmiStack.of(BovinesItems.CUSTOM_FLOWER), Comparison.compareComponents());
//        registry.setDefaultComparison(EmiStack.of(BovinesItems.CUSTOM_MUSHROOM), Comparison.compareComponents());
//        registry.setDefaultComparison(EmiStack.of(BovinesItems.CUSTOM_MUSHROOM_BLOCK), Comparison.compareComponents());
//        registry.setDefaultComparison(EmiStack.of(BovinesItems.PLACEABLE_EDIBLE), Comparison.compareData(emiStack -> {
//            if (emiStack.getItemStack().get(BovinesDataComponents.EDIBLE_TYPE) == null)
//                return EdibleBlockType.MISSING_KEY;
//            return emiStack.getItemStack().get(BovinesDataComponents.EDIBLE_TYPE).holder().unwrapKey().orElse(EdibleBlockType.MISSING_KEY);
//        }));
//        registry.setDefaultComparison(EmiStack.of(BovinesItems.NECTAR_BOWL), Comparison.compareComponents());
//
//        EmiStack flowerCrown = EmiStack.of(FlowerCrownItem.createRainbowCrown(Minecraft.getInstance().level.registryAccess())).comparison(Comparison.compareComponents());
//        registry.removeEmiStacks(emiStack -> emiStack.getItemStack().is(BovinesItems.FLOWER_CROWN) && !emiStack.isEqual(flowerCrown));
//
//        registry.addRecipe(new FlowerCrownEmiRecipe(BovinesAndButtercups.asResource("flower_crown")));
//        for (RecipeHolder<CraftingRecipe> recipe : registry.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING).stream().filter(holder -> holder.value() instanceof SuspiciousEdibleRecipe).toList()) {
//            if (recipe.value() instanceof SuspiciousEdibleRecipe suspiciousEdibleRecipe)
//                registry.addRecipe(new SuspiciousEdibleEmiRecipe(recipe.id(), suspiciousEdibleRecipe));
//        }
//        registry.addDeferredRecipes(emiRecipeConsumer -> {
//            for (RecipeHolder<CraftingRecipe> recipe : registry.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
//                if (recipe.value().getIngredients().stream().anyMatch(ingredient -> BovinesAndButtercups.getHelper().getRemainderIngredient(ingredient) != null)) {
//                    EmiRecipe emiRecipe = EmiApi.getRecipeManager().getRecipe(recipe.id());
//                    for (int i = 0; i < recipe.value().getIngredients().size(); ++i) {
//                        RemainderIngredient ingredient = BovinesAndButtercups.getHelper().getRemainderIngredient(recipe.value().getIngredients().get(i));
//                        if (emiRecipe != null && ingredient != null)
//                            emiRecipe.getInputs().get(i).getEmiStacks().forEach(emiStack ->
//                                    emiStack.setRemainder(EmiStack.of(ingredient.remainder())));
//                    }
//                }
//            }
//        });
//    }
//}
