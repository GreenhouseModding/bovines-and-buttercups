package house.greenhouse.bovinesandbuttercups.api.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.Objects;

public record CustomFlowerType(
        boolean hasPotted,
        SuspiciousStewEffects stewEffectInstances) {

    public static final Codec<CustomFlowerType> DIRECT_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.BOOL.optionalFieldOf("has_potted", true).forGetter(CustomFlowerType::hasPotted),
            ExtraCodecs.catchDecoderException(SuspiciousStewEffects.CODEC).optionalFieldOf("stew_effects", SuspiciousStewEffects.EMPTY).forGetter(CustomFlowerType::stewEffectInstances)
    ).apply(builder, CustomFlowerType::new));

    public static final Codec<Holder<CustomFlowerType>> CODEC = RegistryFixedCodec.create(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE);
    public static final ResourceKey<CustomFlowerType> MISSING_KEY = ResourceKey.create(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE, BovinesAndButtercups.asResource("missing_flower"));
    public static final CustomFlowerType MISSING = new CustomFlowerType(true, SuspiciousStewEffects.EMPTY);

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof CustomFlowerType other))
            return false;

        return other.stewEffectInstances.equals(this.stewEffectInstances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.stewEffectInstances);
    }
}