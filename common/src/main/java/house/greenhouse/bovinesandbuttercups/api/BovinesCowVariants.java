package house.greenhouse.bovinesandbuttercups.api;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.BlockReference;
import house.greenhouse.bovinesandbuttercups.api.variant.ConvertData;
import house.greenhouse.bovinesandbuttercups.api.variant.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.variant.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.CowConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.*;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootTables;
import house.greenhouse.bovinesandbuttercups.content.particle.BovinesParticleTypes;
import house.greenhouse.bovinesandbuttercups.content.predicate.BlockInRadiusCondition;
import house.greenhouse.bovinesandbuttercups.content.predicate.CowSubPredicate;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.util.ColorConstants;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.List;
import java.util.Optional;

public class BovinesCowVariants {
    public static class MoobloomKeys {
        public static final ResourceKey<CowVariant<?>> ALSTROEMERIA = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("alstroemeria"));
        public static final ResourceKey<CowVariant<?>> BUTTERCUP = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("buttercup"));
        public static final ResourceKey<CowVariant<?>> CAMELLIA = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("camellia"));
        public static final ResourceKey<CowVariant<?>> CHARGELILY = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("chargelily"));
        public static final ResourceKey<CowVariant<?>> FREESIA = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("freesia"));
        public static final ResourceKey<CowVariant<?>> HYACINTH = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("hyacinth"));
        public static final ResourceKey<CowVariant<?>> LIMELIGHT = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("limelight"));
        public static final ResourceKey<CowVariant<?>> LINGHOLM = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("lingholm"));
        public static final ResourceKey<CowVariant<?>> NIGHTSHADE = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("nightshade"));
        public static final ResourceKey<CowVariant<?>> PINK_DAISY = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("pink_daisy"));
        public static final ResourceKey<CowVariant<?>> SNOWDROP = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("snowdrop"));
        public static final ResourceKey<CowVariant<?>> SOMBERCUP = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("sombercup"));
        public static final ResourceKey<CowVariant<?>> TROPICAL_BLUE = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("tropical_blue"));
    }

    public static class MooshroomKeys {
        public static final ResourceKey<CowVariant<?>> RED_MUSHROOM = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("red_mushroom"));
        public static final ResourceKey<CowVariant<?>> BROWN_MUSHROOM = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("brown_mushroom"));
    }

    public static class CowKeys {
        public static final ResourceKey<CowVariant<?>> DEFAULT_COW = ResourceKey.create(BovinesRegistryKeys.COW_VARIANT, BovinesAndButtercups.asResource("default_cow"));
    }

    public static void bootstrap(BootstrapContext<CowVariant<?>> context) {
        var blockRegistry = context.lookup(Registries.BLOCK);

        Holder.Reference<CowVariant<?>> chargelilyMoobloom = context.lookup(BovinesRegistryKeys.COW_VARIANT).getOrThrow(MoobloomKeys.CHARGELILY);
        Holder.Reference<CowVariant<?>> sombercupMoobloom = context.lookup(BovinesRegistryKeys.COW_VARIANT).getOrThrow(MoobloomKeys.SOMBERCUP);

        SimpleWeightedRandomList<ConvertData> chargelilyConvertData = SimpleWeightedRandomList.single(new ConvertData(chargelilyMoobloom, List.of(), true));
        SimpleWeightedRandomList<ConvertData> sombercupConvertData = SimpleWeightedRandomList.single(new ConvertData(sombercupMoobloom, List.of(), true));

        context.register(MoobloomKeys.CHARGELILY, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.TEMPERATE,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.CHARGELILY)),
                        new OffspringConditions(List.of(new BlockInRadiusCondition.Builder(BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.CHARGELILY, BovinesBlocks.POTTED_CHARGELILY)).withRadius(12, 6).withOffset(0, 1, 0).build()),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.CHARGELILY.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("chargelily_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.CHARGELILY_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_CHARGELILY_MOOBLOOM.location()),
                sombercupConvertData)));

        // Moobloom Types
        context.register(MoobloomKeys.SOMBERCUP, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.SCULK,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/sombercup_moobloom_sculk_moss_layer"), List.of(
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/sombercup_moobloom_pulasting_layer"), List.of(
                                        new TranslucentTextureModifierFactory(), new EmissiveTextureModifierFactory(), new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/cold_cow_snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.SOMBERCUP)),
                        new OffspringConditions(List.of(new BlockInRadiusCondition.Builder(
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.SOMBERCUP, BovinesBlocks.POTTED_SOMBERCUP)).withRadius(12, 6).withOffset(0, 1, 0).build()),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.SOMBERCUP.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("sombercup_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.SOMBERCUP_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_SOMBERCUP_MOOBLOOM.location()),
                sombercupConvertData)));


        SimpleWeightedRandomList<HolderSet<Biome>> buttercupFlowerForestSet = SimpleWeightedRandomList.<HolderSet<Biome>>builder().add(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST), 7).build();
        SimpleWeightedRandomList<HolderSet<Biome>> pinkDaisyFlowerForestSet = SimpleWeightedRandomList.<HolderSet<Biome>>builder().add(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST), 1).build();

        context.register(MoobloomKeys.ALSTROEMERIA, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.WARM,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.ALSTROEMERIA)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.MELON, Blocks.MELON_STEM),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.POPPY, Blocks.POTTED_POPPY),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.ACACIA_SAPLING, Blocks.POTTED_ACACIA_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.ALSTROEMERIA, BovinesBlocks.POTTED_ALSTROEMERIA))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.ALSTROEMERIA.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("alstroemeria_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.ALSTROEMERIA_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_ALSTROEMERIA_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.BUTTERCUP, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.TEMPERATE,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        buttercupFlowerForestSet, chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.BUTTERCUP)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.SUNFLOWER).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.DANDELION, Blocks.POTTED_DANDELION),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.BIRCH_SAPLING, Blocks.POTTED_BIRCH_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.BUTTERCUP, BovinesBlocks.POTTED_BUTTERCUP))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.BUTTERCUP.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("buttercup_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.BUTTERCUP_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_BUTTERCUP_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.CAMELLIA, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.TEMPERATE,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.CAMELLIA)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.CHERRY_LEAVES),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.PINK_PETALS),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.CHERRY_SAPLING, Blocks.POTTED_CHERRY_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.PINK_DAISY, BovinesBlocks.POTTED_PINK_DAISY))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.CAMELLIA.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("camellia_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.CAMELLIA_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_CAMELLIA_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.FREESIA, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.WARM,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.FREESIA)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.LILY_PAD),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.BLUE_ORCHID, Blocks.POTTED_BLUE_ORCHID),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.MANGROVE_PROPAGULE, Blocks.POTTED_MANGROVE_PROPAGULE)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.FREESIA, BovinesBlocks.POTTED_FREESIA))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.FREESIA.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("freesia_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.FREESIA_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_FREESIA_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.HYACINTH, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.TEMPERATE,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.HYACINTH)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.ROSE_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.CORNFLOWER, Blocks.POTTED_CORNFLOWER),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.HYACINTH, BovinesBlocks.POTTED_HYACINTH))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.HYACINTH.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("hyacinth_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.HYACINTH_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_HYACINTH_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.LIMELIGHT, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.LUSH,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/lush_moobloom_moss_layer"), List.of(new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/lush_snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.LIMELIGHT)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.BIG_DRIPLEAF, Blocks.SMALL_DRIPLEAF),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.FLOWERING_AZALEA, Blocks.POTTED_FLOWERING_AZALEA)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.LIMELIGHT, BovinesBlocks.POTTED_LIMELIGHT))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)
                ),
                new BlockReference<>(Optional.of(BovinesBlocks.LIMELIGHT.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("limelight_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.LIMELIGHT_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_LIMELIGHT_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.LINGHOLM, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.COLD,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/cold_moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/cold_cow_snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.LINGHOLM)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.PUMPKIN, Blocks.PUMPKIN_STEM),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.SWEET_BERRY_BUSH),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.LINGHOLM, BovinesBlocks.POTTED_LINGHOLM))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.LINGHOLM.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("lingholm_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.LINGHOLM_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_LINGHOLM_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.NIGHTSHADE, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.LUSH,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/lush_moobloom_pale_moss_layer"), List.of(
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/lush_snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.NIGHTSHADE)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.PALE_HANGING_MOSS),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.CLOSED_EYEBLOSSOM, Blocks.OPEN_EYEBLOSSOM, Blocks.POTTED_CLOSED_EYEBLOSSOM, Blocks.POTTED_OPEN_EYEBLOSSOM),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.PALE_OAK_LOG, Blocks.PALE_OAK_WOOD, Blocks.PALE_OAK_SAPLING, Blocks.POTTED_PALE_OAK_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.NIGHTSHADE, BovinesBlocks.POTTED_NIGHTSHADE))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.NIGHTSHADE.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("nightshade_bud")), Optional.empty()),
                true,
                Optional.of(BovinesItems.NIGHTSHADE_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_NIGHTSHADE_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.PINK_DAISY, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.TEMPERATE,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        pinkDaisyFlowerForestSet, chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.PINK_DAISY)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.LILAC).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.ALLIUM, Blocks.POTTED_ALLIUM, Blocks.PINK_TULIP, Blocks.POTTED_PINK_TULIP),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.OAK_LOG, Blocks.OAK_WOOD, Blocks.OAK_SAPLING, Blocks.POTTED_OAK_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.PINK_DAISY, BovinesBlocks.POTTED_PINK_DAISY))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.PINK_DAISY.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("pink_daisy_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.PINK_DAISY_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_PINK_DAISY_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.SNOWDROP, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.COLD,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/cold_moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/cold_cow_snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.SNOWDROP)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.SNOW_BLOCK, Blocks.SNOW),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.FERN, Blocks.POTTED_FERN),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.SNOWDROP, BovinesBlocks.POTTED_SNOWDROP))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.SNOWDROP.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("snowdrop_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.SNOWDROP_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_SNOWDROP_MOOBLOOM.location()),
                sombercupConvertData)));
        context.register(MoobloomKeys.TROPICAL_BLUE, new CowVariant<>(BovinesCowTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new BaseCowConfiguration.Settings(Optional.empty(), BovinesCowModelTypes.TEMPERATE,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                        new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                        SimpleWeightedRandomList.empty(), chargelilyConvertData, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.TROPICAL_BLUE)),
                        new OffspringConditions(List.of(createCondition(
                                List.of(
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.COCOA),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.BAMBOO, Blocks.BAMBOO_SAPLING),
                                        BlockPredicate.Builder.block().of(blockRegistry, Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.JUNGLE_SAPLING, Blocks.POTTED_JUNGLE_SAPLING)
                                ),
                                BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.TROPICAL_BLUE, BovinesBlocks.POTTED_TROPICAL_BLUE))),
                                List.of(),
                                OffspringConditions.Inheritance.PARENT)),
                new BlockReference<>(Optional.of(BovinesBlocks.TROPICAL_BLUE.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("tropical_blue_bud")), Optional.empty()),
                false,
                Optional.of(BovinesItems.TROPICAL_BLUE_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_TROPICAL_BLUE_MOOBLOOM.location()),
                sombercupConvertData)));

        // Mooshroom Types
        context.register(MooshroomKeys.RED_MUSHROOM, new CowVariant<>(BovinesCowTypes.MOOSHROOM_TYPE, new MooshroomConfiguration(
                new BaseCowConfiguration.Settings(Optional.of(ResourceLocation.parse("cow/red_mooshroom")), BovinesCowModelTypes.TEMPERATE,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/mooshroom/mooshroom_mycelium_layer"), List.of(new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))
                        ),
                        SimpleWeightedRandomList.single(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOSHROOM_MUSHROOM)),
                        SimpleWeightedRandomList.single(new ConvertData(context.lookup(BovinesRegistryKeys.COW_VARIANT).getOrThrow(MooshroomKeys.BROWN_MUSHROOM), List.of(), true)),
                        Optional.of(ColorParticleOption.create(BovinesParticleTypes.SHROOM, ColorConstants.RED_MUSHROOM)),
                        OffspringConditions.EMPTY),
                new BlockReference<>(Optional.of(Blocks.RED_MUSHROOM.defaultBlockState()), Optional.empty(), Optional.empty()),
                Optional.empty(),
                Optional.of(MushroomCow.Variant.RED),
                Optional.empty()
        )));
        context.register(MooshroomKeys.BROWN_MUSHROOM, new CowVariant<>(BovinesCowTypes.MOOSHROOM_TYPE, new MooshroomConfiguration(
                new BaseCowConfiguration.Settings(Optional.of(ResourceLocation.parse("cow/brown_mooshroom")), BovinesCowModelTypes.TEMPERATE,
                        List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/mooshroom/mooshroom_mycelium_layer"), List.of(new FallbackTextureModifierFactory(List.of()))),
                                new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                        List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))
                        ),
                        SimpleWeightedRandomList.empty(),
                        SimpleWeightedRandomList.single(new ConvertData(context.lookup(BovinesRegistryKeys.COW_VARIANT).getOrThrow(MooshroomKeys.RED_MUSHROOM), List.of(), true)),
                        Optional.of(ColorParticleOption.create(BovinesParticleTypes.SHROOM, ColorConstants.BROWN_MUSHROOM)),
                        OffspringConditions.EMPTY),
                new BlockReference<>(Optional.of(Blocks.BROWN_MUSHROOM.defaultBlockState()), Optional.empty(), Optional.empty()),
                Optional.empty(),
                Optional.of(MushroomCow.Variant.BROWN),
                Optional.empty()
        )));
        SimpleWeightedRandomList<ConvertData> cowConvertData = SimpleWeightedRandomList.<ConvertData>builder()
                .add(
                        new ConvertData(context.lookup(BovinesRegistryKeys.COW_VARIANT).getOrThrow(MoobloomKeys.BUTTERCUP), List.of(
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().located(
                                LocationPredicate.Builder.location().setBiomes(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST))).build()).build()),
                        false), 1
                ).add(
                        new ConvertData(context.lookup(BovinesRegistryKeys.COW_VARIANT).getOrThrow(MooshroomKeys.RED_MUSHROOM), List.of(
                                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().located(
                                        LocationPredicate.Builder.location().setBiomes(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOSHROOM_MUSHROOM))).build()).build()),
                                false), 1
                ).build();

        context.register(CowKeys.DEFAULT_COW, new CowVariant<>(BovinesCowTypes.COW_TYPE, new CowConfiguration(
                new BaseCowConfiguration.Settings(Optional.of(ResourceLocation.parse("cow/cow")), BovinesCowModelTypes.TEMPERATE,
                        List.of(),
                        SimpleWeightedRandomList.single(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_COW_DEFAULT)),
                        cowConvertData,
                        Optional.of(ColorParticleOption.create(BovinesParticleTypes.SHROOM, ColorConstants.BROWN_MUSHROOM)),
                        OffspringConditions.EMPTY)
        )));
    }

    private static LootItemCondition createCondition(List<BlockPredicate.Builder> blocks, BlockPredicate.Builder flowerBlocks) {
        return AnyOfCondition.anyOf(
                AllOfCondition.allOf(blocks.stream().map(predicate -> AnyOfCondition.anyOf(new BlockInRadiusCondition.Builder(predicate).withRadius(12, 6).withOffset(0, 1, 0))).toArray(LootItemCondition.Builder[]::new)),
                new BlockInRadiusCondition.Builder(flowerBlocks).withRadius(12, 6).withOffset(0, 1, 0)
        ).build();
    }

}
