package house.greenhouse.bovinesandbuttercups.api.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.Objects;

public record CustomFlowerType(SuspiciousStewEffects stewEffectInstances) {

    public static final Codec<CustomFlowerType> DIRECT_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ExtraCodecs.catchDecoderException(SuspiciousStewEffects.CODEC).optionalFieldOf("stew_effect", SuspiciousStewEffects.EMPTY).forGetter(CustomFlowerType::stewEffectInstances)
    ).apply(builder, CustomFlowerType::new));

    public static final Codec<Holder<CustomFlowerType>> CODEC = RegistryFileCodec.create(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE, DIRECT_CODEC);
    public static final ResourceKey<CustomFlowerType> MISSING_KEY = ResourceKey.create(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE, BovinesAndButtercups.asResource("missing_flower"));
    public static final CustomFlowerType MISSING = new CustomFlowerType(SuspiciousStewEffects.EMPTY);

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