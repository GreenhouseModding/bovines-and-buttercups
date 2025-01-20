package house.greenhouse.bovinesandbuttercups.api;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;

public record CowVariant<C extends CowConfiguration>(CowType<C> type, C configuration) {
    public static final Codec<CowVariant<?>> DIRECT_CODEC = BovinesRegistries.COW_TYPE.byNameCodec().dispatch(CowVariant::type, CowType::cowCodec);
    public static final Codec<Holder<CowVariant<?>>> CODEC = RegistryFixedCodec.create(BovinesRegistryKeys.COW_VARIANT);
}
