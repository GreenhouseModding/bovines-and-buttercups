package house.greenhouse.bovinesandbuttercups.content.component;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ItemCustomFlower(Holder<CustomFlowerType> holder) {
    public static final Codec<ItemCustomFlower> CODEC =
            CustomFlowerType.CODEC.xmap(ItemCustomFlower::new, ItemCustomFlower::holder);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemCustomFlower> STREAM_CODEC = ByteBufCodecs.holderRegistry(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE).map(ItemCustomFlower::new, ItemCustomFlower::holder);

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof ItemCustomFlower otherMushroom))
            return false;
        return otherMushroom.holder.equals(holder);
    }

    @Override
    public int hashCode() {
        return holder.hashCode();
    }
}
