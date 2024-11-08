package house.greenhouse.bovinesandbuttercups.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.integration.emi.recipe.FlowerCrownEmiRecipe;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import net.minecraft.client.Minecraft;

@EmiEntrypoint
public class BovinesEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.setDefaultComparison(EmiStack.of(BovinesItems.CUSTOM_FLOWER), Comparison.compareComponents());
        registry.setDefaultComparison(EmiStack.of(BovinesItems.CUSTOM_MUSHROOM), Comparison.compareComponents());
        registry.setDefaultComparison(EmiStack.of(BovinesItems.CUSTOM_MUSHROOM_BLOCK), Comparison.compareComponents());
        registry.setDefaultComparison(EmiStack.of(BovinesItems.PLACEABLE_EDIBLE), Comparison.compareComponents());

        registry.setDefaultComparison(EmiStack.of(BovinesItems.NECTAR_BOWL), Comparison.compareComponents());

        registry.setDefaultComparison(EmiStack.of(BovinesItems.FLOWER_CROWN), Comparison.compareComponents());
        EmiStack flowerCrown = EmiStack.of(FlowerCrownItem.createRainbowCrown(Minecraft.getInstance().level.registryAccess())).comparison(Comparison.compareComponents());
        registry.removeEmiStacks(emiStack -> emiStack.getItemStack().is(BovinesItems.FLOWER_CROWN) && !emiStack.isEqual(flowerCrown));
        registry.addRecipe(new FlowerCrownEmiRecipe(BovinesAndButtercups.asResource("flower_crown")));
    }
}
