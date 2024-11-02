package house.greenhouse.bovinesandbuttercups.api.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.PlaceableEdibleBlock;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.util.BlockUtil;
import house.greenhouse.bovinesandbuttercups.util.CreativeModeTabEntry;
import house.greenhouse.bovinesandbuttercups.util.FloatRange;
import house.greenhouse.bovinesandbuttercups.util.IntRange;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: VoxelShapes field.
public record EdibleBlockType(
        int bites,
        int maxStackSize,
        Map<BlockValuesEntry, VoxelShape> shapes,
        Map<HolderSet<Item>, AttachmentEntry> attachable,
        Map<BlockValuesEntry, List<ParticleEntry>> particlePositions,
        List<CreativeModeTabEntry> creativeModeTabs) {
    private static final Codec<Pair<BlockValuesEntry, List<ParticleEntry>>> PARTICLE_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BlockValuesEntry.CODEC.fieldOf("comparable").forGetter(Pair::getFirst),
            ParticleEntry.CODEC.listOf().fieldOf("particles").forGetter(Pair::getSecond)
    ).apply(inst, Pair::of));
    private static final Codec<Pair<BlockValuesEntry, VoxelShape>> SHAPE_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BlockValuesEntry.CODEC.fieldOf("comparable").forGetter(Pair::getFirst),
            BlockUtil.VOXEL_SHAPE_CODEC.fieldOf("shape").forGetter(Pair::getSecond)
    ).apply(inst, Pair::of));
    public static final Codec<EdibleBlockType> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.intRange(1, 16).fieldOf("bites").forGetter(EdibleBlockType::bites),
            Codec.intRange(1, 99).fieldOf("stack_size").forGetter(EdibleBlockType::maxStackSize),
            SHAPE_CODEC.listOf().fieldOf("shapes").xmap(pairs -> pairs.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)), map -> map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).toList()).forGetter(EdibleBlockType::shapes),
            Codec.simpleMap(RegistryCodecs.homogeneousList(Registries.ITEM), AttachmentEntry.CODEC, Keyable.forStrings(() -> Stream.of("items", "values"))).codec().optionalFieldOf("attachments", Map.of()).forGetter(EdibleBlockType::attachable),
            PARTICLE_CODEC.listOf().optionalFieldOf("particles", List.of()).xmap(pairs -> pairs.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)), map -> map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).toList()).forGetter(EdibleBlockType::particlePositions),
            CreativeModeTabEntry.CODEC.listOf().optionalFieldOf("creative_mode_tabs", List.of()).forGetter(EdibleBlockType::creativeModeTabs)
    ).apply(inst, EdibleBlockType::new));

    public static final Codec<Holder<EdibleBlockType>> CODEC = RegistryFixedCodec.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE);
    public static final ResourceKey<EdibleBlockType> MISSING_KEY = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("missing_edible"));

    public static EdibleBlockType cupcake(BootstrapContext<EdibleBlockType> context) {
        ImmutableMap.Builder<HolderSet<Item>, AttachmentEntry> builder = ImmutableMap.builder();
        builder.put(context.lookup(Registries.ITEM).getOrThrow(ItemTags.CANDLES), new AttachmentEntry(4, List.of(
                new ActivationEntry(Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tools/igniter"))), true, Optional.of(new SoundSettings(context.lookup(Registries.SOUND_EVENT).get(ResourceKey.create(Registries.SOUND_EVENT, SoundEvents.FLINTANDSTEEL_USE.getLocation())).orElseThrow(), 1.0F, FloatRange.exact(1.0F), FloatRange.exact(1.0F))), Map.of(), List.of(InvertedLootItemCondition.invert(LocationCheck.checkLocation(LocationPredicate.Builder.location().setFluid(FluidPredicate.Builder.fluid().of(context.lookup(Registries.FLUID).getOrThrow(FluidTags.WATER))))).build())),
                new ActivationEntry(Ingredient.of(Items.FIRE_CHARGE), true, Optional.of(new SoundSettings(context.lookup(Registries.SOUND_EVENT).get(ResourceKey.create(Registries.SOUND_EVENT, SoundEvents.FIRECHARGE_USE.getLocation())).orElseThrow(), 1.0F, FloatRange.exact(1.0F), FloatRange.exact(1.0F))), Map.of(), List.of(InvertedLootItemCondition.invert(LocationCheck.checkLocation(LocationPredicate.Builder.location().setFluid(FluidPredicate.Builder.fluid().of(context.lookup(Registries.FLUID).getOrThrow(FluidTags.WATER))))).build())),
                new ActivationEntry(Ingredient.EMPTY, false, Optional.of(new SoundSettings(context.lookup(Registries.SOUND_EVENT).get(ResourceKey.create(Registries.SOUND_EVENT, SoundEvents.FIRECHARGE_USE.getLocation())).orElseThrow(), 1.0F, FloatRange.exact(1.0F), FloatRange.exact(1.0F))), createEmptyActivationParticles(context), List.of())
        ), Optional.of(new SoundSettings(context.lookup(Registries.SOUND_EVENT).get(ResourceKey.create(Registries.SOUND_EVENT, SoundEvents.CAKE_ADD_CANDLE.getLocation())).orElseThrow(), 1.0F, FloatRange.exact(1.0F), FloatRange.exact(1.0F)))));
        return new EdibleBlockType(4, 16, createCupcakeShapeMap(context), builder.build(), createParticlePositionMap(context), List.of(new CreativeModeTabEntry(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.withDefaultNamespace("food_and_drinks")), Optional.of(Items.CAKE.getDefaultInstance()))));
    }

    public static EdibleBlockType mushroomTart(BootstrapContext<EdibleBlockType> context) {
        return new EdibleBlockType(4, 16, createMushroomTartShapeMap(context), Map.of(), Map.of(), List.of(new CreativeModeTabEntry(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.withDefaultNamespace("food_and_drinks")), Optional.of(Items.CAKE.getDefaultInstance()))));
    }

    private static Map<BlockValuesEntry, VoxelShape> createMushroomTartShapeMap(BootstrapContext<EdibleBlockType> context) {
        return Map.of();
    }

    private static Map<BlockValuesEntry, VoxelShape> createCupcakeShapeMap(BootstrapContext<EdibleBlockType> context) {
        Object2ObjectOpenHashMap<BlockValuesEntry, VoxelShape> map = new Object2ObjectOpenHashMap<>();

        HolderSet<Item> candles = context.lookup(Registries.ITEM).getOrThrow(ItemTags.CANDLES);

        var oneEntry = BlockValuesEntry.builder();
        oneEntry.exactBiteCount(1);
        oneEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(0));
        var one = Block.box(5.0, 0.0, 5.0, 11.0, 5.0, 11.0);

        var oneCandleEntry = BlockValuesEntry.builder();
        oneCandleEntry.exactBiteCount(1);
        oneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(1));
        var oneCandle = Shapes.or(one, Block.box(7.0, 5.0, 7.0, 9.0, 9.0, 9.0));

        map.put(oneEntry.build(), one);
        map.put(oneCandleEntry.build(), oneCandle);

        var twoEntry = BlockValuesEntry.builder();
        twoEntry.exactBiteCount(2);
        twoEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(0));
        var two = Block.box(1.0, 0.0, 5.0, 15.0, 5.0, 11.0);

        var twoOneCandleEntry = BlockValuesEntry.builder();
        twoOneCandleEntry.exactBiteCount(2);
        twoOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1));
        var twoOneCandle = Shapes.or(two, Block.box(11.0, 5.0, 7.0, 13.0, 9.0, 9.0));

        var twoTwoCandlesEntry = BlockValuesEntry.builder();
        twoTwoCandlesEntry.exactBiteCount(2);
        twoTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(2));
        var twoTwoCandles = Shapes.or(twoOneCandle, Block.box(3.0, 5.0, 7.0, 5.0, 8.0, 9.0));

        map.put(twoEntry.build(), two);
        map.put(twoOneCandleEntry.build(), twoOneCandle);
        map.put(twoTwoCandlesEntry.build(), twoTwoCandles);

        var threeEntry = BlockValuesEntry.builder();
        threeEntry.exactBiteCount(3);
        threeEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(0));
        var threeFour = Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0);

        var threeOneCandleEntry = BlockValuesEntry.builder();
        threeOneCandleEntry.exactBiteCount(3);
        threeOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1));
        var threeOneCandle = Shapes.or(threeFour, Block.box(11.0, 5.0, 3.0, 13.0, 9.0, 5.0));

        var threeTwoCandlesEntry = BlockValuesEntry.builder();
        threeTwoCandlesEntry.exactBiteCount(3);
        threeTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(2));
        var threeTwoCandles = Shapes.or(threeOneCandle, Block.box(3.0, 5.0, 3.0, 5.0, 8.0, 5.0));

        var threeThreeCandlesEntry = BlockValuesEntry.builder();
        threeThreeCandlesEntry.exactBiteCount(3);
        threeThreeCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(3));
        var threeThreeCandles = Shapes.or(threeTwoCandles, Block.box(7.0, 5.0, 11.0, 9.0, 10.0, 13.0));

        map.put(threeEntry.build(), threeFour);
        map.put(threeOneCandleEntry.build(), threeOneCandle);
        map.put(threeTwoCandlesEntry.build(), threeTwoCandles);
        map.put(threeThreeCandlesEntry.build(), threeThreeCandles);

        var fourEntry = BlockValuesEntry.builder();
        fourEntry.lowerBoundBiteCount(4);
        fourEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(0));

        var fourOneCandleEntry = BlockValuesEntry.builder();
        fourOneCandleEntry.lowerBoundBiteCount(4);
        fourOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1));
        var fourOneCandle = Shapes.or(threeFour, Block.box(11.0, 5.0, 3.0, 13.0, 9.0, 5.0));

        var fourTwoCandlesEntry = BlockValuesEntry.builder();
        fourTwoCandlesEntry.lowerBoundBiteCount(4);
        fourTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(2));
        var fourTwoCandles = Shapes.or(fourOneCandle, Block.box(3.0, 5.0, 3.0, 5.0, 8.0, 5.0));

        var fourThreeCandlesEntry = BlockValuesEntry.builder();
        fourThreeCandlesEntry.lowerBoundBiteCount(4);
        fourThreeCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(3));
        var fourThreeCandles = Shapes.or(fourTwoCandles, Block.box(11.0, 5.0, 11.0, 13.0, 10.0, 13.0));

        var fourFourCandlesEntry = BlockValuesEntry.builder();
        fourFourCandlesEntry.lowerBoundBiteCount(4);
        fourFourCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(4));
        var fourFourCandles = Shapes.or(fourThreeCandles, Block.box(11.0, 5.0, 3.0, 13.0, 9.0, 5.0));

        map.put(fourEntry.build(), threeFour);
        map.put(fourOneCandleEntry.build(), fourOneCandle);
        map.put(fourTwoCandlesEntry.build(), fourTwoCandles);
        map.put(fourThreeCandlesEntry.build(), fourThreeCandles);
        map.put(fourFourCandlesEntry.build(), fourFourCandles);

        return ImmutableMap.copyOf(map);
    }

    private static Map<BlockValuesEntry, List<ParticleEntry>> createParticlePositionMap(BootstrapContext<EdibleBlockType> context) {
        Holder.Reference<SoundEvent> candleAmbient = context.lookup(Registries.SOUND_EVENT).getOrThrow(ResourceKey.create(Registries.SOUND_EVENT, SoundEvents.CANDLE_AMBIENT.getLocation()));
        Object2ObjectOpenHashMap<BlockValuesEntry, List<ParticleEntry>> map = new Object2ObjectOpenHashMap<>();

        HolderSet<Item> candles = context.lookup(Registries.ITEM).getOrThrow(ItemTags.CANDLES);

        var oneEntry = BlockValuesEntry.builder();
        oneEntry.exactBiteCount(1);
        oneEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(1).active(true));
        var one = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.5, 0.6875, 0.5), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.5, 0.6875, 0.5), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        map.put(oneEntry.build(), one);

        var twoOneCandleEntry = BlockValuesEntry.builder();
        twoOneCandleEntry.exactBiteCount(2);
        twoOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1).active(true));
        var twoOneCandle = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.5), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.5), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        var twoTwoCandlesEntry = BlockValuesEntry.builder();
        twoTwoCandlesEntry.exactBiteCount(2);
        twoTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(2).active(true));
        var twoTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.5), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.5), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))) ,
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.5), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.5), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        map.put(twoOneCandleEntry.build(), twoOneCandle);
        map.put(twoTwoCandlesEntry.build(), twoTwoCandles);

        var threeOneCandleEntry = BlockValuesEntry.builder();
        threeOneCandleEntry.exactBiteCount(3);
        threeOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1).active(true));
        var threeOneCandle  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        var threeTwoCandlesEntry = BlockValuesEntry.builder();
        threeTwoCandlesEntry.exactBiteCount(3);
        threeTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(2).active(true));
        var threeTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );


        var threeThreeCandlesEntry = BlockValuesEntry.builder();
        threeThreeCandlesEntry.exactBiteCount(3);
        threeThreeCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(3).active(true));
        var threeThreeCandles = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.5, 0.75, 0.75), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.5, 0.75, 0.75), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        map.put(threeOneCandleEntry.build(), threeOneCandle);
        map.put(threeTwoCandlesEntry.build(), threeTwoCandles);
        map.put(threeThreeCandlesEntry.build(), threeThreeCandles);

        var fourOneCandleEntry = BlockValuesEntry.builder();
        fourOneCandleEntry.lowerBoundBiteCount(4);
        fourOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1).active(true));
        var fourOneCandle  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        var fourTwoCandlesEntry = BlockValuesEntry.builder();
        fourTwoCandlesEntry.lowerBoundBiteCount(4);
        fourTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(2).active(true));
        var fourTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        var fourThreeCandlesEntry = BlockValuesEntry.builder();
        fourThreeCandlesEntry.lowerBoundBiteCount(4);
        fourThreeCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(3).active(true));
        var fourThreeCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.75, 0.75), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.75, 0.75), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        var fourFourCandlesEntry = BlockValuesEntry.builder();
        fourFourCandlesEntry.lowerBoundBiteCount(4);
        fourFourCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(4).active(true));
        var fourFourCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.75, 0.75, 0.75), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.75, 0.75), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F)))),
                new ParticleEntry(ParticleTypes.FLAME, new Vec3(0.25, 0.6875, 0.75), Vec3.ZERO, 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.6875, 0.75), Vec3.ZERO, 0.3F, Optional.of(new SoundSettings(candleAmbient, 0.17F, FloatRange.range(1.0F, 1.1F), FloatRange.range(0.3F, 1.0F))))
        );

        map.put(fourOneCandleEntry.build(), fourOneCandle);
        map.put(fourTwoCandlesEntry.build(), fourTwoCandles);
        map.put(fourThreeCandlesEntry.build(), fourThreeCandles);
        map.put(fourFourCandlesEntry.build(), fourFourCandles);

        return Map.copyOf(map);
    }


    private static Map<BlockValuesEntry, List<ParticleEntry>> createEmptyActivationParticles(BootstrapContext<EdibleBlockType> context) {
        Map<BlockValuesEntry, List<ParticleEntry>> map = new Object2ObjectOpenHashMap<>();

        HolderSet<Item> candles = context.lookup(Registries.ITEM).getOrThrow(ItemTags.CANDLES);

        var oneEntry = BlockValuesEntry.builder();
        oneEntry.exactBiteCount(1);
        oneEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(1).active(true));
        var one = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.5, 0.6875, 0.5), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        map.put(oneEntry.build(), one);

        var twoOneCandleEntry = BlockValuesEntry.builder();
        twoOneCandleEntry.exactBiteCount(2);
        twoOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1).active(true));
        var twoOneCandle = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.5), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        var twoTwoCandlesEntry = BlockValuesEntry.builder();
        twoTwoCandlesEntry.exactBiteCount(2);
        twoTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(2).active(true));
        var twoTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.5), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.5), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        map.put(twoOneCandleEntry.build(), twoOneCandle);
        map.put(twoTwoCandlesEntry.build(), twoTwoCandles);

        var threeOneCandleEntry = BlockValuesEntry.builder();
        threeOneCandleEntry.exactBiteCount(3);
        threeOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1).active(true));
        var threeOneCandle  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        var threeTwoCandlesEntry = BlockValuesEntry.builder();
        threeTwoCandlesEntry.exactBiteCount(3);
        threeTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(2).active(true));
        var threeTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );


        var threeThreeCandlesEntry = BlockValuesEntry.builder();
        threeThreeCandlesEntry.exactBiteCount(3);
        threeThreeCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(3).active(true));
        var threeThreeCandles = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.5, 0.75, 0.75), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        map.put(threeOneCandleEntry.build(), threeOneCandle);
        map.put(threeTwoCandlesEntry.build(), threeTwoCandles);
        map.put(threeThreeCandlesEntry.build(), threeThreeCandles);

        var fourOneCandleEntry = BlockValuesEntry.builder();
        fourOneCandleEntry.lowerBoundBiteCount(4);
        fourOneCandleEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(1).active(true));
        var fourOneCandle  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        var fourTwoCandlesEntry = BlockValuesEntry.builder();
        fourTwoCandlesEntry.lowerBoundBiteCount(4);
        fourTwoCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(2).active(true));
        var fourTwoCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        var fourThreeCandlesEntry = BlockValuesEntry.builder();
        fourThreeCandlesEntry.lowerBoundBiteCount(4);
        fourThreeCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().exactCount(3).active(true));
        var fourThreeCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.75, 0.75), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        var fourFourCandlesEntry = BlockValuesEntry.builder();
        fourFourCandlesEntry.lowerBoundBiteCount(4);
        fourFourCandlesEntry.addAttachment(candles, BlockValuesEntry.AttachmentValueEntry.builder().lowerBoundCount(4).active(true));
        var fourFourCandles  = ImmutableList.of(
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.6875, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.625, 0.25), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.75, 0.75, 0.75), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty()),
                new ParticleEntry(ParticleTypes.SMOKE, new Vec3(0.25, 0.6875, 0.75), new Vec3(0.0F, 0.1F, 0.0F), 1.0F, Optional.empty())
        );

        map.put(fourOneCandleEntry.build(), fourOneCandle);
        map.put(fourTwoCandlesEntry.build(), fourTwoCandles);
        map.put(fourThreeCandlesEntry.build(), fourThreeCandles);
        map.put(fourFourCandlesEntry.build(), fourFourCandles);

        return ImmutableMap.copyOf(map);
    }

    @ApiStatus.Internal
    public static EdibleBlockType missingEdible(BootstrapContext<EdibleBlockType> context) {
        return new EdibleBlockType(1, 64, Map.of(EdibleBlockType.BlockValuesEntry.builder().build(), Shapes.block()), Map.of(), Map.of(), List.of());
    }

    public record ActivationEntry(Ingredient ingredient, boolean setTo, Optional<SoundSettings> sound, Map<BlockValuesEntry, List<ParticleEntry>> particles, List<LootItemCondition> condition) {
        public static final Codec<ActivationEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ActivationEntry::ingredient),
                Codec.BOOL.fieldOf("set_to").forGetter(ActivationEntry::setTo),
                SoundSettings.CODEC.optionalFieldOf("sound").forGetter(ActivationEntry::sound),
                PARTICLE_CODEC.listOf().optionalFieldOf("particles", List.of()).xmap(pairs -> pairs.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)), map -> map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).collect(Collectors.toList())).forGetter(ActivationEntry::particles),
                LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("condition", List.of()).forGetter(ActivationEntry::condition)
        ).apply(inst, ActivationEntry::new));
    }

    public record BlockValuesEntry(Optional<IntRange> biteCount, Map<HolderSet<Item>, AttachmentValueEntry> attachments) {
        public static final Codec<BlockValuesEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                IntRange.codec(0, 16).optionalFieldOf("bite_count").forGetter(BlockValuesEntry::biteCount),
                Codec.simpleMap(RegistryCodecs.homogeneousList(Registries.ITEM), AttachmentValueEntry.CODEC, Keyable.forStrings(() -> Stream.of("items", "values"))).codec().optionalFieldOf("attachments", Map.of()).forGetter(BlockValuesEntry::attachments)
        ).apply(inst, BlockValuesEntry::new));

        public boolean test(PlaceableEdibleBlockEntity blockEntity) {
            if (blockEntity.getEdibleType() == null || !blockEntity.getEdibleType().holder().isBound())
                return false;
            return (biteCount.isEmpty() || biteCount.get().test(blockEntity.getBlockState().getValue(PlaceableEdibleBlock.BITES))) && (attachments.isEmpty() || attachments.entrySet().stream().allMatch(entry -> {
                if (!blockEntity.getAttachments().containsKey(entry.getKey()))
                    return ((entry.getValue().item.isEmpty() || entry.getValue().item.stream().allMatch(predicate ->  predicate.test(ItemStack.EMPTY))) && entry.getValue().count.test(0)) && (entry.getValue().active.isEmpty() || !entry.getValue().active.get());
                var list = blockEntity.getAttachments().entrySet().stream().filter(e -> e.getKey().equals(entry.getKey()) && (e.getValue().items().isEmpty() && entry.getValue().item.stream().allMatch(predicate ->  predicate.test(ItemStack.EMPTY)) && attachments.get(entry.getKey()).count.test(e.getValue().items().size())) || (entry.getValue().item.isEmpty() && entry.getValue().count.test(e.getValue().items().size()) || !entry.getValue().item.isEmpty() && entry.getValue().count.test((int)entry.getValue().item.stream().flatMap(predicate -> {
                    return e.getValue().items().stream().map(predicate::test);
                }).count()))).toList();

                if (list.isEmpty())
                    return ((entry.getValue().item.isEmpty() || entry.getValue().item.stream().allMatch(predicate ->  predicate.test(ItemStack.EMPTY))) && entry.getValue().count.test(blockEntity.getAttachments().get(entry.getKey()).items().size())) && (entry.getValue().active.isEmpty() || !entry.getValue().active.get());

                return list.stream().allMatch(e -> {
                    return entry.getValue().active.isEmpty() || e.getValue().active() == entry.getValue().active.get();
                });
            }));
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BlockValuesEntry entry))
                return false;

            return entry.attachments.equals(attachments) && entry.biteCount.equals(biteCount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(attachments, biteCount);
        }

        public record AttachmentValueEntry(List<ItemPredicate> item, IntRange count, Optional<Boolean> active) {
            public static final Codec<AttachmentValueEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                    ItemPredicate.CODEC.listOf().fieldOf("item").forGetter(AttachmentValueEntry::item),
                    IntRange.codec(0, 16).optionalFieldOf("count", IntRange.lowerBound(1)).forGetter(AttachmentValueEntry::count),
                    Codec.BOOL.optionalFieldOf("active").forGetter(AttachmentValueEntry::active)
            ).apply(inst, AttachmentValueEntry::new));

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder {
                private final List<ItemPredicate> item = new ArrayList<>();
                private IntRange count = IntRange.lowerBound(0);
                private Optional<Boolean> active = Optional.empty();

                public Builder item(ItemPredicate.Builder builder) {
                    item.add(builder.build());
                    return this;
                }

                public Builder exactCount(int value) {
                    count = IntRange.exact(value);
                    return this;
                }

                public Builder rangedCount(int min, int max) {
                    count = IntRange.range(min, max);
                    return this;
                }

                public Builder lowerBoundCount(int min) {
                    count = IntRange.lowerBound(min);
                    return this;
                }

                public Builder upperBoundCount(int max) {
                    count = IntRange.lowerBound(max);
                    return this;
                }

                public Builder active(boolean value) {
                    active = Optional.of(value);
                    return this;
                }

                public AttachmentValueEntry build() {
                    return new AttachmentValueEntry(item, count, active);
                }
            }
        }

        public static class Builder {
            private Optional<IntRange> biteCount = Optional.empty();
            private final Map<HolderSet<Item>, AttachmentValueEntry> attachments = new HashMap<>();

            public Builder exactBiteCount(int value) {
                biteCount = Optional.of(IntRange.exact(value));
                return this;
            }

            public Builder rangedBiteCount(int min, int max) {
                biteCount = Optional.of(IntRange.range(min, max));
                return this;
            }

            public Builder lowerBoundBiteCount(int min) {
                biteCount = Optional.of(IntRange.lowerBound(min));
                return this;
            }

            public Builder upperBoundBiteCount(int max) {
                biteCount = Optional.of(IntRange.upperBound(max));
                return this;
            }

            public Builder addAttachment(HolderSet<Item> items, AttachmentValueEntry.Builder builder) {
                attachments.put(items, builder.build());
                return this;
            }

            public BlockValuesEntry build() {
                return new BlockValuesEntry(biteCount, attachments);
            }
        }
    }

    public record AttachmentEntry(int maxCount, List<ActivationEntry> activations, Optional<SoundSettings> sound) {
        public static final Codec<AttachmentEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.intRange(1, 16).fieldOf("max_count").forGetter(AttachmentEntry::maxCount),
                ActivationEntry.CODEC.listOf().fieldOf("activations").forGetter(AttachmentEntry::activations),
                SoundSettings.CODEC.optionalFieldOf("sound").forGetter(AttachmentEntry::sound)
        ).apply(inst, AttachmentEntry::new));
    }

    public record ParticleEntry(ParticleOptions particle, Vec3 position, Vec3 speed, float chance, Optional<SoundSettings> sound) {
        public static final Codec<ParticleEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ParticleTypes.CODEC.fieldOf("particle").forGetter(ParticleEntry::particle),
                Vec3.CODEC.fieldOf("position").forGetter(ParticleEntry::position),
                Vec3.CODEC.fieldOf("speed").forGetter(ParticleEntry::speed),
                Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(ParticleEntry::chance),
                SoundSettings.CODEC.optionalFieldOf("sound").forGetter(ParticleEntry::sound)
        ).apply(inst, ParticleEntry::new));
    }

    public record SoundSettings(Holder<SoundEvent> sound, float chance, FloatRange volume, FloatRange pitch) {
        public static final Codec<SoundSettings> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                SoundEvent.CODEC.fieldOf("sound").forGetter(SoundSettings::sound),
                Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(SoundSettings::chance),
                FloatRange.codec(0.0F, 2.0F).optionalFieldOf("volume", FloatRange.exact(1.0F)).forGetter(SoundSettings::volume),
                FloatRange.codec(0.0F, 2.0F).optionalFieldOf("pitch", FloatRange.exact(1.0F)).forGetter(SoundSettings::pitch)
        ).apply(inst, SoundSettings::new));
    }
}