package house.greenhouse.bovinesandbuttercups.content.data.nectar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record Nectar(ResourceLocation modelLocation,
                     NectarEffects effects) {
    public static final Codec<Nectar> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("model_location").forGetter(Nectar::modelLocation),
            NectarEffects.CODEC.optionalFieldOf("effects", NectarEffects.EMPTY).forGetter(Nectar::effects)
    ).apply(inst, Nectar::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Nectar> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, Nectar::modelLocation,
            NectarEffects.STREAM_CODEC, Nectar::effects,
            Nectar::new
    );
    public static final Codec<Holder<Nectar>> CODEC = RegistryFileCodec.create(BovinesRegistryKeys.NECTAR, Nectar.DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Nectar>> STREAM_CODEC = ByteBufCodecs.holder(BovinesRegistryKeys.NECTAR, DIRECT_STREAM_CODEC);

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Nectar nectar))
            return false;
        return nectar.modelLocation.equals(modelLocation) && nectar.effects == effects;
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelLocation, effects);
    }
}
