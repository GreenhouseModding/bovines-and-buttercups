package house.greenhouse.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.Optional;
import java.util.function.Function;

public record FloatRange(Optional<Float> min, Optional<Float> max) {
    public FloatRange {
        if (min.isEmpty() && max.isEmpty())
            throw new IllegalArgumentException("Cannot create float range without a \"min\" or a \"max\" field");
    }

    public boolean test(int value) {
        return (min.isEmpty() || value >= min.get()) && (max.isEmpty() || value <= max.get());
    }

    public float randomise(RandomSource source) {
        return Mth.nextFloat(source, min.orElse(max.get()), max.orElse(min.get()));
    }

    private static Codec<FloatRange> directCodec(float min, float max) {
        return RecordCodecBuilder.create(inst -> inst.group(
                Codec.floatRange(min, max).optionalFieldOf("min").forGetter(FloatRange::min),
                Codec.floatRange(min, max).optionalFieldOf("max").forGetter(FloatRange::max)
        ).apply(inst, FloatRange::new));
    }

    public static Codec<FloatRange> codec(float min, float max) {
        return Codec.either(Codec.floatRange(min, max), directCodec(min, max)).xmap(either -> either.map(flt -> new FloatRange(Optional.of(flt), Optional.of(flt)), Function.identity()), floatRange -> {
            if (floatRange.min().isPresent() && floatRange.max().isPresent() && floatRange.min() == floatRange.max())
                return Either.left(floatRange.min().orElseThrow());
            return Either.right(floatRange);
        });
    }

    public static FloatRange exact(float value) {
        return new FloatRange(Optional.of(value), Optional.of(value));
    }

    public static FloatRange range(float min, float max) {
        return new FloatRange(Optional.of(min), Optional.of(max));
    }

    public static FloatRange lowerBound(float min) {
        return new FloatRange(Optional.of(min), Optional.empty());
    }

    public static FloatRange upperBound(float max) {
        return new FloatRange(Optional.empty(), Optional.of(max));
    }
}