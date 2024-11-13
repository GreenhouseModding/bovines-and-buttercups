package house.greenhouse.bovinesandbuttercups.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdible;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.recipe.SuspiciousEdibleRecipe;
import house.greenhouse.bovinesandbuttercups.integration.recipe.BovinesRecipeViewerUtil;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SuspiciousEdibleEmiRecipe extends EmiPatternCraftingRecipe {
    private SuspiciousEdibleRecipe recipe;

    public SuspiciousEdibleEmiRecipe(ResourceLocation id, SuspiciousEdibleRecipe recipe) {
        super(createInput(recipe), createOutput(recipe.getEdibleType()), id);
        this.recipe = recipe;
    }

    public static List<EmiIngredient> createInput(SuspiciousEdibleRecipe recipe) {
        return recipe.getIngredients().stream().map(EmiIngredient::of).toList();
    }

    public static EmiStack createOutput(Holder<EdibleBlockType> edibleBlockType) {
        ItemStack stack = new ItemStack(BovinesItems.PLACEABLE_EDIBLE);
        stack.set(BovinesDataComponents.EDIBLE_TYPE, new ItemEdible(edibleBlockType, List.of()));
        return EmiStack.of(stack);
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (recipe.getPattern().ingredients().size() - 1 < slot || recipe.getPattern().ingredients().get(slot).isEmpty())
            return new SlotWidget(EmiStack.EMPTY, x, y);

        if (recipe.getPattern().ingredients().get(slot).getItems().length == 1 && Arrays.stream(recipe.getPattern().ingredients().get(slot).getItems()).anyMatch(stack -> stack.is(Items.SUSPICIOUS_STEW)))
            return new GeneratedSlotWidget(random -> {
                ItemStack stack = new ItemStack(Items.SUSPICIOUS_STEW);
                stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, getEffectHolder(random).getSuspiciousEffects());
                return EmiStack.of(stack);
            }, unique, x, y);
        
        return new GeneratedSlotWidget(random -> EmiIngredient.of(recipe.getPattern().ingredients().get(slot)), unique, x, y);
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(r -> {
            ItemStack stack = new ItemStack(BovinesItems.PLACEABLE_EDIBLE);
            stack.set(BovinesDataComponents.EDIBLE_TYPE, new ItemEdible(recipe.getEdibleType(), getEffectHolder(r).getSuspiciousEffects().effects().stream().map(entry -> new ItemEdible.MobEffectEntry(new MobEffectInstance(entry.effect(), entry.duration() / 4), entry.duration(), ItemEdible.MobEffectEntry.ShowTooltip.CREATIVE_MENU_ONLY)).toList()));
            return EmiStack.of(stack);
        }, unique, x, y);
    }

    private SuspiciousEffectHolder getEffectHolder(Random random) {
        return SuspiciousEffectHolder.getAllEffectHolders().get(random.nextInt(SuspiciousEffectHolder.getAllEffectHolders().size()));
    }
}
