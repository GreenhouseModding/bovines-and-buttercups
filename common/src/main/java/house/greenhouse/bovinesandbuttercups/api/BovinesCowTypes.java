package house.greenhouse.bovinesandbuttercups.api;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.BlockReference;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.ConditionedTextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.EmissiveTextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.FallbackTextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.GrassTintTextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.TranslucentTextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootTables;
import house.greenhouse.bovinesandbuttercups.content.predicate.BlockInRadiusCondition;
import house.greenhouse.bovinesandbuttercups.content.predicate.CowSubPredicate;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.particle.BovinesParticleTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.util.ColorConstants;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
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

public class BovinesCowTypes {
    public static class MoobloomKeys {
        public static final ResourceKey<CowType<?>> BIRD_OF_PARADISE = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("bird_of_paradise"));
        public static final ResourceKey<CowType<?>> BUTTERCUP = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("buttercup"));
        public static final ResourceKey<CowType<?>> CAMELLIA = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("camellia"));
        public static final ResourceKey<CowType<?>> CHARGELILY = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("chargelily"));
        public static final ResourceKey<CowType<?>> FREESIA = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("freesia"));
        public static final ResourceKey<CowType<?>> HYACINTH = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("hyacinth"));
        public static final ResourceKey<CowType<?>> LIMELIGHT = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("limelight"));
        public static final ResourceKey<CowType<?>> LINGHOLM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("lingholm"));
        public static final ResourceKey<CowType<?>> NIGHTSHADE = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("nightshade"));
        public static final ResourceKey<CowType<?>> PINK_DAISY = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("pink_daisy"));
        public static final ResourceKey<CowType<?>> SNOWDROP = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("snowdrop"));
        public static final ResourceKey<CowType<?>> SOMBERCUP = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("sombercup"));
        public static final ResourceKey<CowType<?>> TROPICAL_BLUE = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("tropical_blue"));
        public static final ResourceKey<CowType<?>> MISSING_MOOBLOOM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("missing_moobloom"));
    }

    public static class MooshroomKeys {
        public static final ResourceKey<CowType<?>> RED_MUSHROOM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("red_mushroom"));
        public static final ResourceKey<CowType<?>> BROWN_MUSHROOM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("brown_mushroom"));
        public static final ResourceKey<CowType<?>> MISSING_MOOSHROOM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("missing_mooshroom"));
    }

    public static void bootstrap(BootstrapContext<CowType<?>> context) {
        var blockRegistry = context.lookup(Registries.BLOCK);

        // Moobloom Types
        context.register(MoobloomKeys.SOMBERCUP, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.empty(), Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.SOMBERCUP))),
                new BlockReference<>(Optional.of(BovinesBlocks.SOMBERCUP.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("sombercup_bud")), Optional.empty()),
                BovinesCowModelTypes.FLAT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_sculk_moss_layer"), List.of(
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/sombercup_moobloom_pulasting_layer"), List.of(
                                new TranslucentTextureModifierFactory(), new EmissiveTextureModifierFactory(), new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.SOMBERCUP_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_SOMBERCUP_MOOBLOOM.location()),
                SimpleWeightedRandomList.empty(),
                new OffspringConditions(List.of(new BlockInRadiusCondition.Builder(BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.SOMBERCUP, BovinesBlocks.POTTED_SOMBERCUP)).withRadius(12, 6).withOffset(0, 1, 0).build()),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        Holder.Reference<CowType<?>> sombercupMoobloom = context.lookup(BovinesRegistryKeys.COW_TYPE).getOrThrow(MoobloomKeys.SOMBERCUP);

        context.register(MoobloomKeys.CHARGELILY, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.empty(), Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.CHARGELILY))),
                new BlockReference<>(Optional.of(BovinesBlocks.CHARGELILY.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("chargelily_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.CHARGELILY_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_CHARGELILY_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(new BlockInRadiusCondition.Builder(BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.CHARGELILY, BovinesBlocks.POTTED_CHARGELILY)).withRadius(12, 6).withOffset(0, 1, 0).build()),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        Holder.Reference<CowType<?>> chargelilyMoobloom = context.lookup(BovinesRegistryKeys.COW_TYPE).getOrThrow(MoobloomKeys.CHARGELILY);

        SimpleWeightedRandomList<HolderSet<Biome>> buttercupFlowerForestSet = SimpleWeightedRandomList.<HolderSet<Biome>>builder().add(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST), 7).build();
        SimpleWeightedRandomList<HolderSet<Biome>> pinkDaisyFlowerForestSet = SimpleWeightedRandomList.<HolderSet<Biome>>builder().add(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST), 1).build();
        SimpleWeightedRandomList<Holder<CowType<?>>> chargelilyWeighted = SimpleWeightedRandomList.single(chargelilyMoobloom);

        context.register(MoobloomKeys.BIRD_OF_PARADISE, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.BIRD_OF_PARADISE))),
                new BlockReference<>(Optional.of(BovinesBlocks.BIRD_OF_PARADISE.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bird_of_paradise_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.BIRD_OF_PARADISE_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_BIRD_OF_PARADISE_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.MELON, Blocks.MELON_STEM),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.POPPY, Blocks.POTTED_POPPY),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.ACACIA_SAPLING, Blocks.POTTED_ACACIA_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.BIRD_OF_PARADISE, BovinesBlocks.POTTED_BIRD_OF_PARADISE))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.BUTTERCUP, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), buttercupFlowerForestSet, chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.BUTTERCUP))),
                new BlockReference<>(Optional.of(BovinesBlocks.BUTTERCUP.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("buttercup_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.BUTTERCUP_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_BUTTERCUP_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.SUNFLOWER).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.DANDELION, Blocks.POTTED_DANDELION),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.BIRCH_SAPLING, Blocks.POTTED_BIRCH_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.BUTTERCUP, BovinesBlocks.POTTED_BUTTERCUP))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.CAMELLIA, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.CAMELLIA))),
                new BlockReference<>(Optional.of(BovinesBlocks.CAMELLIA.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("camellia_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.CAMELLIA_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_CAMELLIA_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.CHERRY_LEAVES),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.PINK_PETALS),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.CHERRY_SAPLING, Blocks.POTTED_CHERRY_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.PINK_DAISY, BovinesBlocks.POTTED_PINK_DAISY))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.FREESIA, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.FREESIA))),
                new BlockReference<>(Optional.of(BovinesBlocks.FREESIA.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("freesia_bud")), Optional.empty()),
                BovinesCowModelTypes.BUFFALO,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.FREESIA_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_FREESIA_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.LILY_PAD),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.BLUE_ORCHID, Blocks.POTTED_BLUE_ORCHID),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.MANGROVE_PROPAGULE, Blocks.POTTED_MANGROVE_PROPAGULE)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.FREESIA, BovinesBlocks.POTTED_FREESIA))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.HYACINTH, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.HYACINTH))),
                new BlockReference<>(Optional.of(BovinesBlocks.HYACINTH.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("hyacinth_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.HYACINTH_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_HYACINTH_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.ROSE_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.CORNFLOWER, Blocks.POTTED_CORNFLOWER),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.HYACINTH, BovinesBlocks.POTTED_HYACINTH))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.LIMELIGHT, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.LIMELIGHT))),
                new BlockReference<>(Optional.of(BovinesBlocks.LIMELIGHT.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("limelight_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_moss_layer"), List.of(new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.LIMELIGHT_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_LIMELIGHT_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.BIG_DRIPLEAF, Blocks.SMALL_DRIPLEAF),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.FLOWERING_AZALEA, Blocks.POTTED_FLOWERING_AZALEA)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.LIMELIGHT, BovinesBlocks.POTTED_LIMELIGHT))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.LINGHOLM, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.LINGHOLM))),
                new BlockReference<>(Optional.of(BovinesBlocks.LINGHOLM.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("lingholm_bud")), Optional.empty()),
                BovinesCowModelTypes.HIGHLAND,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.LINGHOLM_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_LINGHOLM_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.PUMPKIN, Blocks.PUMPKIN_STEM),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.SWEET_BERRY_BUSH),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.LINGHOLM, BovinesBlocks.POTTED_LINGHOLM))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.NIGHTSHADE, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.empty(), Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.NIGHTSHADE))),
                new BlockReference<>(Optional.of(BovinesBlocks.NIGHTSHADE.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("nightshade_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_pale_moss_layer"), List.of(
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.NIGHTSHADE_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_NIGHTSHADE_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.PALE_HANGING_MOSS),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.CLOSED_EYEBLOSSOM, Blocks.OPEN_EYEBLOSSOM, Blocks.POTTED_CLOSED_EYEBLOSSOM, Blocks.POTTED_OPEN_EYEBLOSSOM),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.PALE_OAK_LOG, Blocks.PALE_OAK_WOOD, Blocks.PALE_OAK_SAPLING, Blocks.POTTED_PALE_OAK_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.NIGHTSHADE, BovinesBlocks.POTTED_NIGHTSHADE))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.PINK_DAISY, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), pinkDaisyFlowerForestSet, chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.PINK_DAISY))),
                new BlockReference<>(Optional.of(BovinesBlocks.PINK_DAISY.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("pink_daisy_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.PINK_DAISY_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_PINK_DAISY_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.LILAC).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.ALLIUM, Blocks.POTTED_ALLIUM, Blocks.PINK_TULIP, Blocks.POTTED_PINK_TULIP),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.OAK_LOG, Blocks.OAK_WOOD, Blocks.OAK_SAPLING, Blocks.POTTED_OAK_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.PINK_DAISY, BovinesBlocks.POTTED_PINK_DAISY))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.SNOWDROP, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.SNOWDROP))),
                new BlockReference<>(Optional.of(BovinesBlocks.SNOWDROP.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("snowdrop_bud")), Optional.empty()),
                BovinesCowModelTypes.HIGHLAND,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.SNOWDROP_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_SNOWDROP_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.SNOW_BLOCK, Blocks.SNOW),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.FERN, Blocks.POTTED_FERN),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.SNOWDROP, BovinesBlocks.POTTED_SNOWDROP))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.TROPICAL_BLUE, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.TROPICAL_BLUE))),
                new BlockReference<>(Optional.of(BovinesBlocks.TROPICAL_BLUE.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("tropical_blue_bud")), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))),
                Optional.of(BovinesItems.TROPICAL_BLUE_NECTAR_BOWL.getDefaultInstance()),
                Optional.of(BovinesLootTables.SHEAR_TROPICAL_BLUE_MOOBLOOM.location()),
                SimpleWeightedRandomList.single(sombercupMoobloom),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.COCOA),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.BAMBOO, Blocks.BAMBOO_SAPLING),
                                BlockPredicate.Builder.block().of(blockRegistry, Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.JUNGLE_SAPLING, Blocks.POTTED_JUNGLE_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(blockRegistry, BovinesBlocks.TROPICAL_BLUE, BovinesBlocks.POTTED_TROPICAL_BLUE))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));

        // Mooshroom Types
        context.register(MooshroomKeys.RED_MUSHROOM, new CowType<>(BovinesCowTypeTypes.MOOSHROOM_TYPE, new MooshroomConfiguration(
                new CowTypeConfiguration.Settings(Optional.of(ResourceLocation.parse("cow/red_mooshroom")), SimpleWeightedRandomList.single(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOSHROOM_MUSHROOM)), SimpleWeightedRandomList.single(context.lookup(BovinesRegistryKeys.COW_TYPE).getOrThrow(MooshroomKeys.BROWN_MUSHROOM)), Optional.of(ColorParticleOption.create(BovinesParticleTypes.SHROOM, ColorConstants.RED_MUSHROOM))),
                new BlockReference<>(Optional.of(Blocks.RED_MUSHROOM.defaultBlockState()), Optional.empty(), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/mooshroom/mooshroom_mycelium_layer"), List.of(new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))
                ),
                Optional.empty(),
                Optional.of(MushroomCow.Variant.RED),
                Optional.empty(),
                OffspringConditions.EMPTY
        )));
        context.register(MooshroomKeys.BROWN_MUSHROOM, new CowType<>(BovinesCowTypeTypes.MOOSHROOM_TYPE, new MooshroomConfiguration(
                new CowTypeConfiguration.Settings(Optional.of(ResourceLocation.parse("cow/brown_mooshroom")), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.single(context.lookup(BovinesRegistryKeys.COW_TYPE).getOrThrow(MooshroomKeys.RED_MUSHROOM)), Optional.of(ColorParticleOption.create(BovinesParticleTypes.SHROOM, ColorConstants.BROWN_MUSHROOM))),
                new BlockReference<>(Optional.of(Blocks.BROWN_MUSHROOM.defaultBlockState()), Optional.empty(), Optional.empty()),
                BovinesCowModelTypes.DEFAULT,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/mooshroom/mooshroom_mycelium_layer"), List.of(new FallbackTextureModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedTextureModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(new CowSubPredicate(Optional.empty(),Optional.of(true))).build()).build()),1)))
                ),
                Optional.empty(),
                Optional.of(MushroomCow.Variant.BROWN),
                Optional.empty(),
                OffspringConditions.EMPTY
        )));
    }

    private static LootItemCondition createCondition(List<BlockPredicate.Builder> blocks, BlockPredicate.Builder flowerBlocks) {
        return AnyOfCondition.anyOf(
                AllOfCondition.allOf(blocks.stream().map(predicate -> AnyOfCondition.anyOf(new BlockInRadiusCondition.Builder(predicate).withRadius(12, 6).withOffset(0, 1, 0))).toArray(LootItemCondition.Builder[]::new)),
                new BlockInRadiusCondition.Builder(flowerBlocks).withRadius(12, 6).withOffset(0, 1, 0)
        ).build();
    }

}
