package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdible;
import house.greenhouse.bovinesandbuttercups.util.BlockUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class PlaceableEdibleItem extends BlockItem {
    public PlaceableEdibleItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(BovinesDataComponents.EDIBLE_TYPE)) {
            ItemEdible type = stack.get(BovinesDataComponents.EDIBLE_TYPE);
            if (type.holder().isBound())
                return BlockUtil.getOrCreateBlockNameTranslationKey(type.holder().unwrapKey().orElseThrow().location());
        }
        return BlockUtil.getOrCreateBlockNameTranslationKey(BovinesAndButtercups.asResource("missing_edible"));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(BovinesDataComponents.EDIBLE_TYPE))
            stack.get(BovinesDataComponents.EDIBLE_TYPE).addToTooltip(context, tooltipComponents::add, tooltipFlag);
    }
}
