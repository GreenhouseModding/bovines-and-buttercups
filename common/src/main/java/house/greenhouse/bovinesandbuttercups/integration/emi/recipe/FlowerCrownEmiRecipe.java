package house.greenhouse.bovinesandbuttercups.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import house.greenhouse.bovinesandbuttercups.integration.recipe.BovinesRecipeViewerUtil;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Random;

public class FlowerCrownEmiRecipe extends EmiPatternCraftingRecipe {
    public FlowerCrownEmiRecipe(ResourceLocation id) {
        super(createInput(), EmiStack.of(BovinesItems.FLOWER_CROWN), id);
    }

    private static List<EmiIngredient> createInput() {
        return (List<EmiIngredient>) (List<?>) Minecraft.getInstance().level.registryAccess().registryOrThrow(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL).stream().map(material -> EmiStack.of(material.ingredient())).toList();
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (output == null || slot == 4)
            return new SlotWidget(EmiStack.EMPTY, x, y);
        if (slot < 4)
            return new GeneratedSlotWidget(r -> EmiStack.of(generateRandomFlowerCrown(r).get(BovinesDataComponents.FLOWER_CROWN).getMaterialForRecipeViewer(slot)), unique, x, y);
        return new GeneratedSlotWidget(r -> EmiStack.of(generateRandomFlowerCrown(r).get(BovinesDataComponents.FLOWER_CROWN).getMaterialForRecipeViewer(slot - 1)), unique, x, y);
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(FlowerCrownEmiRecipe::generateRandomFlowerCrown, unique, x, y);
    }

    private static EmiStack generateRandomFlowerCrown(Random random) {
        return EmiStack.of(BovinesRecipeViewerUtil.generateRandomFlowerCrown(random, BovinesItems.FLOWER_CROWN));
    }
}
