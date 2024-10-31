package house.greenhouse.bovinesandbuttercups.api.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.codec.BovinesCodecs;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Objects;
import java.util.Optional;

public record CustomMushroomType(Optional<SimpleWeightedRandomList<ResourceKey<StructureTemplatePool>>> hugeMushroomStructurePool,
                                 boolean randomlyRotateHugeStructure) {

    public static final Codec<CustomMushroomType> DIRECT_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BovinesCodecs.weightedEntryCodec(ResourceKey.codec(Registries.TEMPLATE_POOL), "template_pool").optionalFieldOf("huge_structures").forGetter(CustomMushroomType::hugeMushroomStructurePool),
            Codec.BOOL.optionalFieldOf("randomly_rotate_huge_structure", false).forGetter(CustomMushroomType::randomlyRotateHugeStructure)
    ).apply(builder, CustomMushroomType::new));

    public static final Codec<Holder<CustomMushroomType>> CODEC = RegistryFileCodec.create(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE, DIRECT_CODEC);
    public static final ResourceKey<CustomMushroomType> MISSING_KEY = ResourceKey.create(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE, BovinesAndButtercups.asResource("missing_mushroom"));
    public static final CustomMushroomType MISSING = new CustomMushroomType(Optional.empty(), false);

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof CustomMushroomType other))
            return false;

        return other.hugeMushroomStructurePool.equals(this.hugeMushroomStructurePool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hugeMushroomStructurePool);
    }
}