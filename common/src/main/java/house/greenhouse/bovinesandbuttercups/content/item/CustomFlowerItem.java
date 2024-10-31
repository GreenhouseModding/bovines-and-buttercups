package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
                return getOrCreateNameTranslationKey(flower.holder().unwrapKey().orElseThrow().location());
        }
        return super.getName(stack);
    }

    private static Component getOrCreateNameTranslationKey(ResourceLocation location) {
        return Component.translatable("block." + location.getNamespace() + "." + location.getPath());
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
