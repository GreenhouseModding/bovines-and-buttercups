package house.greenhouse.bovinesandbuttercups.content.component;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ItemEdibleType(Holder<EdibleBlockType> holder) {
    public static final Codec<ItemEdibleType> CODEC =
            EdibleBlockType.CODEC.xmap(ItemEdibleType::new, ItemEdibleType::holder);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemEdibleType> STREAM_CODEC = ByteBufCodecs.holderRegistry(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE).map(ItemEdibleType::new, ItemEdibleType::holder);

    public static void apply(ItemStack stack, Holder<EdibleBlockType> holder) {
        if (holder.isBound()) {
            List<DataComponentType<?>> requiredPatches = List.of(DataComponents.MAX_STACK_SIZE, BovinesDataComponents.EDIBLE_TYPE);
            if (!stack.getComponentsPatch().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet()).containsAll(requiredPatches))
                stack.set(DataComponents.MAX_STACK_SIZE, holder.value().maxStackSize());
            stack.set(BovinesDataComponents.EDIBLE_TYPE, new ItemEdibleType(holder));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof ItemEdibleType otherEdible))
            return false;
        return otherEdible.holder.equals(holder);
    }

    @Override
    public int hashCode() {
        return holder.hashCode();
    }
}
