package house.greenhouse.bovinesandbuttercups.content.component;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.api.block.PlaceableEdibleType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ItemPlaceableEdible(Holder<PlaceableEdibleType> holder) {
    public static final Codec<ItemPlaceableEdible> CODEC =
            PlaceableEdibleType.CODEC.xmap(ItemPlaceableEdible::new, ItemPlaceableEdible::holder);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPlaceableEdible> STREAM_CODEC = ByteBufCodecs.holderRegistry(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE).map(ItemPlaceableEdible::new, ItemPlaceableEdible::holder);

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemPlaceableEdible otherEdible))
            return false;
        return otherEdible.holder.equals(holder);
    }

    @Override
    public int hashCode() {
        return holder.hashCode();
    }
}
