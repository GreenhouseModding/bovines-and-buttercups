package house.greenhouse.bovinesandbuttercups.api.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: VoxelShapes field.
public record PlaceableEdibleType(
        int feeds,
        List<ActivationEntry> activations,
        Map<HolderSet<Item>, Integer> attachable,
        Map<BlockValuesEntry, List<ParticleEntry>> particlePositions) {
    private static final Codec<Pair<BlockValuesEntry, List<ParticleEntry>>> POSITION_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BlockValuesEntry.CODEC.fieldOf("comparable").forGetter(Pair::getFirst),
            ParticleEntry.CODEC.listOf().fieldOf("particles").forGetter(Pair::getSecond)
    ).apply(inst, Pair::of));
    public static final Codec<PlaceableEdibleType> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("feeds").forGetter(PlaceableEdibleType::feeds),
            ActivationEntry.CODEC.listOf().fieldOf("activations").forGetter(PlaceableEdibleType::activations),
            Codec.simpleMap(RegistryCodecs.homogeneousList(Registries.ITEM), Codec.INT, Keyable.forStrings(() -> Stream.of("items", "count"))).codec().optionalFieldOf("attachments", Map.of()).forGetter(PlaceableEdibleType::attachable),
            POSITION_CODEC.listOf().optionalFieldOf("particles", List.of()).xmap(pairs -> pairs.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)), map -> map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).collect(Collectors.toList())).forGetter(PlaceableEdibleType::particlePositions)
    ).apply(inst, PlaceableEdibleType::new));

    public static final Codec<Holder<PlaceableEdibleType>> CODEC = RegistryFileCodec.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, DIRECT_CODEC);
    public static final ResourceKey<PlaceableEdibleType> MISSING_KEY = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("missing"));

    public static PlaceableEdibleType cupcake(RegistryOps.RegistryInfoLookup lookup) {
        ImmutableMap.Builder<HolderSet<Item>, Integer> builder = ImmutableMap.builder();
        lookup.lookup(Registries.ITEM).orElseThrow().getter().getOrThrow(ItemTags.CANDLES).forEach(itemHolder -> {
            builder.put(HolderSet.direct(itemHolder), 4);
        });
        return new PlaceableEdibleType(4,
                List.of(new ActivationEntry(Ingredient.of(BovinesTags.ItemTags.CUPCAKE_LIGHTERS), true), new ActivationEntry(Ingredient.EMPTY, false)),
                builder.build(), createParticlePositionMap(lookup));
    }

    public static PlaceableEdibleType mushroomTart(RegistryOps.RegistryInfoLookup lookup) {
        return new PlaceableEdibleType(4, List.of(), Map.of(), Map.of());
    }

    public static Map<BlockValuesEntry, List<ParticleEntry>> createParticlePositionMap(RegistryOps.RegistryInfoLookup lookup) {
        Object2ObjectOpenHashMap<BlockValuesEntry, List<ParticleEntry>> map = new Object2ObjectOpenHashMap<>();

        HolderSet<Item> candles = lookup.lookup(Registries.ITEM).orElseThrow().getter().getOrThrow(ItemTags.CANDLES);

        var oneEntry = BlockValuesEntry.builder();
        oneEntry.feedCount(1);
        oneEntry.addAttachment(candles, 1);
        oneEntry.active(true);
        var one = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.5, 0.6875, 0.5))
        );
        map.put(oneEntry.build(), one);

        var twoOneCandleEntry = BlockValuesEntry.builder();
        twoOneCandleEntry.feedCount(2);
        twoOneCandleEntry.addAttachment(candles, 1);
        twoOneCandleEntry.active(true);
        var twoOneCandle = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.5))
        );
        map.put(twoOneCandleEntry.build(), twoOneCandle);

        var twoTwoCandlesEntry = BlockValuesEntry.builder();
        twoTwoCandlesEntry.feedCount(2);
        twoTwoCandlesEntry.addAttachment(candles, 2);
        twoTwoCandlesEntry.active(true);
        var twoTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.5)), new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.5))
        );
        map.put(twoTwoCandlesEntry.build(), twoTwoCandles);

        var threeOneCandleEntry = BlockValuesEntry.builder();
        threeOneCandleEntry.feedCount(3);
        threeOneCandleEntry.addAttachment(candles, 1);
        threeOneCandleEntry.active(true);
        var threeOneCandle  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25))
        );
        map.put(threeOneCandleEntry.build(), threeOneCandle);

        var threeTwoCandlesEntry = BlockValuesEntry.builder();
        threeTwoCandlesEntry.feedCount(3);
        threeTwoCandlesEntry.addAttachment(candles, 2);
        threeTwoCandlesEntry.active(true);
        var threeTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25)),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25))
        );
        map.put(threeTwoCandlesEntry.build(), threeTwoCandles);


        var threeThreeCandlesEntry = BlockValuesEntry.builder();
        threeThreeCandlesEntry.feedCount(3);
        threeThreeCandlesEntry.addAttachment(candles, 3);
        threeThreeCandlesEntry.active(true);
        var threeThreeCandles = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25)),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25)), new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.5, 0.75, 0.75))
        );
        map.put(threeThreeCandlesEntry.build(), threeThreeCandles);

        var fourOneCandleEntry = BlockValuesEntry.builder();
        fourOneCandleEntry.feedCount(4);
        fourOneCandleEntry.addAttachment(candles, 1);
        fourOneCandleEntry.active(true);
        var fourOneCandle  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25))
        );

        var fourTwoCandlesEntry = BlockValuesEntry.builder();
        fourTwoCandlesEntry.feedCount(4);
        fourTwoCandlesEntry.addAttachment(candles, 2);
        fourTwoCandlesEntry.active(true);
        var fourTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25)),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25))
        );

        var fourThreeCandlesEntry = BlockValuesEntry.builder();
        fourThreeCandlesEntry.feedCount(4);
        fourThreeCandlesEntry.addAttachment(candles, 3);
        fourThreeCandlesEntry.active(true);
        var fourThreeCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25)),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25)),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.75, 0.75))
        );

        var fourFourCandlesEntry = BlockValuesEntry.builder();
        fourFourCandlesEntry.feedCount(4);
        fourFourCandlesEntry.addAttachment(candles, 4);
        fourFourCandlesEntry.active(true);
        var fourFourCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25)),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25)),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.75, 0.75)),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.6875, 0.75))
        );

        map.put(fourOneCandleEntry.build(), fourOneCandle);
        map.put(fourTwoCandlesEntry.build(), fourTwoCandles);
        map.put(fourThreeCandlesEntry.build(), fourThreeCandles);
        map.put(fourFourCandlesEntry.build(), fourFourCandles);

        return Map.copyOf(map);
    }

    public static PlaceableEdibleType createMissing(RegistryOps.RegistryInfoLookup lookup) {
        return cupcake(lookup);
    }

    public record ActivationEntry(Ingredient ingredient, boolean setTo) {
        public static final Codec<ActivationEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ActivationEntry::ingredient),
                Codec.BOOL.fieldOf("set_to").forGetter(ActivationEntry::setTo)
        ).apply(inst, ActivationEntry::new));
    }

    public record BlockValuesEntry(Optional<Integer> feedCount, Map<HolderSet<Item>, Integer> attachments, Optional<Boolean> active) {
        public static final Codec<BlockValuesEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("feed_count").forGetter(BlockValuesEntry::feedCount),
                Codec.simpleMap(RegistryCodecs.homogeneousList(Registries.ITEM), Codec.INT, Keyable.forStrings(() -> Stream.of("attachment", "count"))).codec().optionalFieldOf("attachments", Map.of()).forGetter(BlockValuesEntry::attachments),
                Codec.BOOL.optionalFieldOf("active").forGetter(BlockValuesEntry::active)
        ).apply(inst, BlockValuesEntry::new));

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Optional<Integer> feedCount = Optional.empty();
            private Map<HolderSet<Item>, Integer> attachments = new HashMap<>();
            private Optional<Boolean> active = Optional.empty();

            public Builder feedCount(int value) {
                feedCount = Optional.of(value);
                return this;
            }

            public Builder addAttachment(HolderSet<Item> attachment, int value) {
                attachments.put(attachment, value);
                return this;
            }

            public Builder active(boolean active) {
                this.active = Optional.of(active);
                return this;
            }

            public BlockValuesEntry build() {
                return new BlockValuesEntry(feedCount, attachments, active);
            }
        }
    }

    public record ParticleEntry(ParticleOptions particle, Vec3 position) {
        public static final Codec<ParticleEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ParticleTypes.CODEC.fieldOf("particle").forGetter(ParticleEntry::particle),
                Vec3.CODEC.fieldOf("position").forGetter(ParticleEntry::position)
        ).apply(inst, ParticleEntry::new));
    }
}