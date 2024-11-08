package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.util.BlockUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.Block;

public class CustomFlowerItem extends BlockItem {
    public CustomFlowerItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(BovinesDataComponents.CUSTOM_FLOWER)) {
            ItemCustomFlower flower = stack.get(BovinesDataComponents.CUSTOM_FLOWER);
            if (flower.holder().isBound())
                return BlockUtil.getOrCreateBlockNameTranslationKey(flower.holder().unwrapKey().orElseThrow().location());
        }
        return BlockUtil.getOrCreateBlockNameTranslationKey(BovinesAndButtercups.asResource("missing_flower"));
    }


    public static SuspiciousStewEffects getSuspiciousStewEffects(ItemStack stack, RegistryAccess registryAccess) {
        if (stack.has(BovinesDataComponents.CUSTOM_FLOWER)) {
            ItemCustomFlower flower = stack.get(BovinesDataComponents.CUSTOM_FLOWER);
            if (flower.holder().isBound())
                return flower.holder().value().stewEffectInstances();
        }
        return SuspiciousStewEffects.EMPTY;
    }

}
