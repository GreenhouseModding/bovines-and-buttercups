package house.greenhouse.bovinesandbuttercups.api.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.phys.AABB;

public class BovinesCodecs {
    public static final Codec<AABB> AABB = RecordCodecBuilder.create(inst -> inst.group(
            Codec.DOUBLE.optionalFieldOf("min_x", 0.0).forGetter(aabb -> aabb.minX),
            Codec.DOUBLE.optionalFieldOf("min_y", 0.0).forGetter(aabb -> aabb.minY),
            Codec.DOUBLE.optionalFieldOf("min_z", 0.0).forGetter(aabb -> aabb.minZ),
            Codec.DOUBLE.optionalFieldOf("max_x", 0.0).forGetter(aabb -> aabb.maxX),
            Codec.DOUBLE.optionalFieldOf("max_y", 0.0).forGetter(aabb -> aabb.maxY),
            Codec.DOUBLE.optionalFieldOf("max_z", 0.0).forGetter(aabb -> aabb.maxZ)
    ).apply(inst, AABB::new));

    public static <T> Codec<SimpleWeightedRandomList<T>> weightedEntryCodec(Codec<T> codec, String fieldName) {
        Codec<WeightedEntry.Wrapper<T>> direct = RecordCodecBuilder.create(inst -> inst.group(
                codec.fieldOf(fieldName).forGetter(WeightedEntry.Wrapper::data),
                Weight.CODEC.optionalFieldOf("weight", Weight.of(1)).forGetter(WeightedEntry.Wrapper::weight)
        ).apply(inst, WeightedEntry.Wrapper::new));

        return Codec.either(codec.listOf(), direct.listOf())
                .xmap(either -> either.map(t -> {
                    var builder = SimpleWeightedRandomList.<T>builder();
                    for (var value : t) {
                        builder.add(value);
                    }
                    return builder.build();
                }, weighted -> {
                    var builder = SimpleWeightedRandomList.<T>builder();
                    for (var value : weighted) {
                        builder.add(value.data(), value.weight().asInt());
                    }
                    return builder.build();
                }),
                wrapper -> Either.right(wrapper.unwrap()));
    }
}
