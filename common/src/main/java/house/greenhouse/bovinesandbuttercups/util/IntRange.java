package house.greenhouse.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.Optional;
import java.util.function.Function;

public record IntRange(Optional<Integer> min, Optional<Integer> max) {
    public IntRange {
        if (min.isEmpty() && max.isEmpty())
            throw new IllegalArgumentException("Cannot create int range without a \"min\" or a \"max\" field");
    }

    public boolean test(int value) {
        return (min.isEmpty() || value >= min.get()) && (max.isEmpty() || value <= max.get());
    }

    public int randomise(RandomSource source) {
        return Mth.nextInt(source, min.orElse(max.get()), max.orElse(min.get()));
    }

    private static Codec<IntRange> directCodec(int min, int max) {
        return RecordCodecBuilder.create(inst -> inst.group(
                Codec.intRange(min, max).optionalFieldOf("min").forGetter(IntRange::min),
                Codec.intRange(min, max).optionalFieldOf("max").forGetter(IntRange::max)
        ).apply(inst, IntRange::new));
    }

    public static Codec<IntRange> codec(int min, int max) {
        return Codec.either(Codec.intRange(min, max), directCodec(min, max)).xmap(either -> either.map(integer -> new IntRange(Optional.of(integer), Optional.of(integer)), Function.identity()), intRange -> {
            if (intRange.min().isPresent() && intRange.max().isPresent() && intRange.min() == intRange.max())
                return Either.left(intRange.min().orElseThrow());
            return Either.right(intRange);
        });
    }

    public static IntRange exact(int value) {
        return new IntRange(Optional.of(value), Optional.of(value));
    }

    public static IntRange range(int min, int max) {
        return new IntRange(Optional.of(min), Optional.of(max));
    }

    public static IntRange lowerBound(int min) {
        return new IntRange(Optional.of(min), Optional.empty());
    }

    public static IntRange upperBound(int max) {
        return new IntRange(Optional.empty(), Optional.of(max));
    }
}