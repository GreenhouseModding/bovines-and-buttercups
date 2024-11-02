package house.greenhouse.bovinesandbuttercups.content.component;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ItemCustomMushroom(Holder<CustomMushroomType> holder) {
    public static final Codec<ItemCustomMushroom> CODEC =
            CustomMushroomType.CODEC.xmap(ItemCustomMushroom::new, ItemCustomMushroom::holder);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemCustomMushroom> STREAM_CODEC = ByteBufCodecs.holderRegistry(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE).map(ItemCustomMushroom::new, ItemCustomMushroom::holder);

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof ItemCustomMushroom otherMushroom))
            return false;
        return otherMushroom.holder.equals(holder);
    }

    @Override
    public int hashCode() {
        return holder.hashCode();
    }
}
