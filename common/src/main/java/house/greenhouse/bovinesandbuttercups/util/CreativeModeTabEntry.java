package house.greenhouse.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record CreativeModeTabEntry(ResourceKey<CreativeModeTab> tab, Optional<ItemStack> after) {
    public static final Codec<CreativeModeTabEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceKey.codec(Registries.CREATIVE_MODE_TAB).fieldOf("tab").forGetter(CreativeModeTabEntry::tab),
            Codec.either(ItemStack.STRICT_SINGLE_ITEM_CODEC, BuiltInRegistries.ITEM.byNameCodec()).xmap(either -> either.map(stack -> stack, Item::getDefaultInstance), stack -> {
                if (ItemStack.isSameItemSameComponents(stack, stack.getItem().getDefaultInstance()))
                    return Either.right(stack.getItem());
                return Either.left(stack);
            }).optionalFieldOf("after").forGetter(CreativeModeTabEntry::after)
    ).apply(inst, CreativeModeTabEntry::new));
}
