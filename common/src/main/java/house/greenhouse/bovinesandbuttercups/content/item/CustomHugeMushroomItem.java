package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomMushroom;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.util.BlockUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CustomHugeMushroomItem extends BlockItem {
    public CustomHugeMushroomItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(BovinesDataComponents.CUSTOM_MUSHROOM)) {
            ItemCustomMushroom mushroom = stack.get(BovinesDataComponents.CUSTOM_MUSHROOM);
            if (mushroom.holder().isBound())
                return BlockUtil.getOrCreateBlockNameTranslationKey(mushroom.holder().unwrapKey().orElseThrow().location());
        }
        return super.getName(stack);
    }
}
