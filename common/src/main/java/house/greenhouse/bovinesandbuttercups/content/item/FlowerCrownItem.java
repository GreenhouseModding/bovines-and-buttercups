package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.BovinesFlowerCrownMaterials;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class FlowerCrownItem extends ArmorItem {
    private static final ItemAttributeModifiers EMPTY_NO_TOOLTIP = new ItemAttributeModifiers(List.of(), false);

    public FlowerCrownItem(Properties properties) {
        super(BovinesArmorMaterials.FLOWER_CROWN, Type.HELMET, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(BovinesDataComponents.FLOWER_CROWN))
            stack.get(BovinesDataComponents.FLOWER_CROWN).addToTooltip(context, tooltipComponents::add, tooltipFlag);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return EMPTY_NO_TOOLTIP;
    }

    public static ItemStack createRainbowCrown(HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<FlowerCrownMaterial> registry = lookup.lookupOrThrow(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL);
        ItemStack rainbowCrown = new ItemStack(BovinesItems.FLOWER_CROWN);
        rainbowCrown.set(BovinesDataComponents.FLOWER_CROWN, new FlowerCrown(registry.getOrThrow(BovinesFlowerCrownMaterials.ALSTROEMERIA), registry.getOrThrow(BovinesFlowerCrownMaterials.BUTTERCUP), registry.getOrThrow(BovinesFlowerCrownMaterials.LIMELIGHT), registry.getOrThrow(BovinesFlowerCrownMaterials.FREESIA), registry.getOrThrow(BovinesFlowerCrownMaterials.CHARGELILY), registry.getOrThrow(BovinesFlowerCrownMaterials.PINK_DAISY), registry.getOrThrow(BovinesFlowerCrownMaterials.HYACINTH), registry.getOrThrow(BovinesFlowerCrownMaterials.TROPICAL_BLUE)));
        return rainbowCrown;
    }
}