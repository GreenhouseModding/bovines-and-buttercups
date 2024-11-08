package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdibleType;
import house.greenhouse.bovinesandbuttercups.util.BlockUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class PlaceableEdibleItem extends BlockItem {
    public PlaceableEdibleItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(BovinesDataComponents.EDIBLE_TYPE)) {
            ItemEdibleType type = stack.get(BovinesDataComponents.EDIBLE_TYPE);
            if (type.holder().isBound())
                return BlockUtil.getOrCreateBlockNameTranslationKey(type.holder().unwrapKey().orElseThrow().location());
        }
        return BlockUtil.getOrCreateBlockNameTranslationKey(BovinesAndButtercups.asResource("missing_edible"));
    }
}
