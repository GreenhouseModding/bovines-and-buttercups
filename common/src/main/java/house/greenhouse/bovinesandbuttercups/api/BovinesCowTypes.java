package house.greenhouse.bovinesandbuttercups.api;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.BlockReference;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.ConditionedModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.FallbackModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.GrassTintTextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.predicate.BlockInRadiusCondition;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.registry.BovinesNectars;
import house.greenhouse.bovinesandbuttercups.registry.BovinesParticleTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.util.ColorConstants;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.List;
import java.util.Optional;

public class BovinesCowTypes {
    public static class MoobloomKeys {
        public static final ResourceKey<CowType<?>> BIRD_OF_PARADISE = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("bird_of_paradise"));
        public static final ResourceKey<CowType<?>> BUTTERCUP = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("buttercup"));
        public static final ResourceKey<CowType<?>> CHARGELILY = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("chargelily"));
        public static final ResourceKey<CowType<?>> FREESIA = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("freesia"));
        public static final ResourceKey<CowType<?>> HYACINTH = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("hyacinth"));
        public static final ResourceKey<CowType<?>> LIMELIGHT = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("limelight"));
        public static final ResourceKey<CowType<?>> LINGHOLM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("lingholm"));
        public static final ResourceKey<CowType<?>> PINK_DAISY = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("pink_daisy"));
        public static final ResourceKey<CowType<?>> SNOWDROP = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("snowdrop"));
        public static final ResourceKey<CowType<?>> TROPICAL_BLUE = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("tropical_blue"));
        public static final ResourceKey<CowType<?>> MISSING_MOOBLOOM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("missing_moobloom"));
    }

    public static class MooshroomKeys {
        public static final ResourceKey<CowType<?>> RED_MUSHROOM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("red_mushroom"));
        public static final ResourceKey<CowType<?>> BROWN_MUSHROOM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("brown_mushroom"));
        public static final ResourceKey<CowType<?>> MISSING_MOOSHROOM = ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("missing_mooshroom"));
    }

    public static void bootstrap(BootstrapContext<CowType<?>> context) {
        var nectarRegistry = context.lookup(BovinesRegistryKeys.NECTAR);
        var snowTag = new CompoundTag();

        snowTag.putBoolean("has_snow", true);

        // Moobloom Types
        context.register(MoobloomKeys.CHARGELILY, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.empty(), Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.CHARGELILY))),
                new BlockReference<>(Optional.of(BovinesBlocks.CHARGELILY.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/chargelily_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.CHARGELILY)),
                new OffspringConditions(List.of(new BlockInRadiusCondition.Builder(BlockPredicate.Builder.block().of(BovinesBlocks.CHARGELILY, BovinesBlocks.POTTED_CHARGELILY)).withRadius(12, 6).withOffset(0, 1, 0).build()),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));

        Holder.Reference<CowType<?>> chargelilyMoobloom = context.lookup(BovinesRegistryKeys.COW_TYPE).getOrThrow(MoobloomKeys.CHARGELILY);
        SimpleWeightedRandomList<HolderSet<Biome>> buttercupFlowerForestSet = SimpleWeightedRandomList.<HolderSet<Biome>>builder().add(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST), 7).build();
        SimpleWeightedRandomList<HolderSet<Biome>> pinkDaisyFlowerForestSet = SimpleWeightedRandomList.<HolderSet<Biome>>builder().add(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST), 1).build();
        SimpleWeightedRandomList<Holder<CowType<?>>> chargelilyWeighted = SimpleWeightedRandomList.single(chargelilyMoobloom);

        context.register(MoobloomKeys.BIRD_OF_PARADISE, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.BIRD_OF_PARADISE))),
                new BlockReference<>(Optional.of(BovinesBlocks.BIRD_OF_PARADISE.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/bird_of_paradise_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.BIRD_OF_PARADISE)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.MELON, Blocks.MELON_STEM),
                                BlockPredicate.Builder.block().of(Blocks.POPPY, Blocks.POTTED_POPPY),
                                BlockPredicate.Builder.block().of(Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.ACACIA_SAPLING, Blocks.POTTED_ACACIA_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.BIRD_OF_PARADISE, BovinesBlocks.POTTED_BIRD_OF_PARADISE))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.BUTTERCUP, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), buttercupFlowerForestSet, chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.BUTTERCUP))),
                new BlockReference<>(Optional.of(BovinesBlocks.BUTTERCUP.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/buttercup_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.BUTTERCUP)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.SUNFLOWER).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                BlockPredicate.Builder.block().of(Blocks.DANDELION, Blocks.POTTED_DANDELION),
                                BlockPredicate.Builder.block().of(Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.BIRCH_SAPLING, Blocks.POTTED_BIRCH_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.BUTTERCUP, BovinesBlocks.POTTED_BUTTERCUP))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.FREESIA, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.FREESIA))),
                new BlockReference<>(Optional.of(BovinesBlocks.FREESIA.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/freesia_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.FREESIA)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.LILY_PAD),
                                BlockPredicate.Builder.block().of(Blocks.BLUE_ORCHID, Blocks.POTTED_BLUE_ORCHID),
                                BlockPredicate.Builder.block().of(Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.MANGROVE_PROPAGULE, Blocks.POTTED_MANGROVE_PROPAGULE)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.FREESIA, BovinesBlocks.POTTED_FREESIA))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.HYACINTH, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.HYACINTH))),
                new BlockReference<>(Optional.of(BovinesBlocks.HYACINTH.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/hyacinth_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.HYACINTH)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.ROSE_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                BlockPredicate.Builder.block().of(Blocks.CORNFLOWER, Blocks.POTTED_CORNFLOWER),
                                BlockPredicate.Builder.block().of(Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.HYACINTH, BovinesBlocks.POTTED_HYACINTH))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.LIMELIGHT, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.LIMELIGHT))),
                new BlockReference<>(Optional.of(BovinesBlocks.LIMELIGHT.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/limelight_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_moss_layer"), List.of(new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.LIMELIGHT)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT),
                                BlockPredicate.Builder.block().of(Blocks.BIG_DRIPLEAF, Blocks.SMALL_DRIPLEAF),
                                BlockPredicate.Builder.block().of(Blocks.FLOWERING_AZALEA_LEAVES, Blocks.FLOWERING_AZALEA, Blocks.POTTED_FLOWERING_AZALEA)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.LIMELIGHT, BovinesBlocks.POTTED_LIMELIGHT))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.LINGHOLM, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.LINGHOLM))),
                new BlockReference<>(Optional.of(BovinesBlocks.LINGHOLM.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/lingholm_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.LINGHOLM)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.PUMPKIN, Blocks.PUMPKIN_STEM),
                                BlockPredicate.Builder.block().of(Blocks.SWEET_BERRY_BUSH),
                                BlockPredicate.Builder.block().of(Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.LINGHOLM, BovinesBlocks.POTTED_LINGHOLM))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.PINK_DAISY, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), pinkDaisyFlowerForestSet, chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.PINK_DAISY))),
                new BlockReference<>(Optional.of(BovinesBlocks.PINK_DAISY.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/pink_daisy_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.PINK_DAISY)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.LILAC).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)),
                                BlockPredicate.Builder.block().of(Blocks.ALLIUM, Blocks.POTTED_ALLIUM, Blocks.PINK_TULIP, Blocks.POTTED_PINK_TULIP, Blocks.PINK_PETALS),
                                BlockPredicate.Builder.block().of(Blocks.OAK_LOG, Blocks.OAK_WOOD, Blocks.OAK_SAPLING, Blocks.POTTED_OAK_SAPLING,
                                        Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.CHERRY_SAPLING, Blocks.POTTED_CHERRY_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.PINK_DAISY, BovinesBlocks.POTTED_PINK_DAISY))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.SNOWDROP, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.SNOWDROP))),
                new BlockReference<>(Optional.of(BovinesBlocks.SNOWDROP.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/snowdrop_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.SNOWDROP)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.SNOW_BLOCK, Blocks.SNOW),
                                BlockPredicate.Builder.block().of(Blocks.FERN, Blocks.POTTED_FERN),
                                BlockPredicate.Builder.block().of(Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.SNOWDROP, BovinesBlocks.POTTED_SNOWDROP))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));
        context.register(MoobloomKeys.TROPICAL_BLUE, new CowType<>(BovinesCowTypeTypes.MOOBLOOM_TYPE, new MoobloomConfiguration(
                new CowTypeConfiguration.Settings(Optional.empty(), SimpleWeightedRandomList.empty(), chargelilyWeighted, Optional.of(ColorParticleOption.create(BovinesParticleTypes.BLOOM, ColorConstants.TROPICAL_BLUE))),
                new BlockReference<>(Optional.of(BovinesBlocks.TROPICAL_BLUE.defaultBlockState()), Optional.empty(), Optional.empty()),
                new BlockReference<>(Optional.empty(), Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/tropical_blue_bud")), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory(),
                                new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))),
                Optional.of(nectarRegistry.getOrThrow(BovinesNectars.TROPICAL_BLUE)),
                new OffspringConditions(List.of(createCondition(
                        List.of(
                                BlockPredicate.Builder.block().of(Blocks.COCOA),
                                BlockPredicate.Builder.block().of(Blocks.BAMBOO, Blocks.BAMBOO_SAPLING),
                                BlockPredicate.Builder.block().of(Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.JUNGLE_SAPLING, Blocks.POTTED_JUNGLE_SAPLING)
                        ),
                        BlockPredicate.Builder.block().of(BovinesBlocks.TROPICAL_BLUE, BovinesBlocks.POTTED_TROPICAL_BLUE))),
                        List.of(),
                        OffspringConditions.Inheritance.PARENT))));

        // Mooshroom Types
        context.register(MooshroomKeys.RED_MUSHROOM, new CowType<>(BovinesCowTypeTypes.MOOSHROOM_TYPE, new MooshroomConfiguration(
                new CowTypeConfiguration.Settings(Optional.of(ResourceLocation.parse("cow/red_mooshroom")), SimpleWeightedRandomList.single(context.lookup(Registries.BIOME).getOrThrow(BovinesTags.BiomeTags.HAS_MOOSHROOM_MUSHROOM)), SimpleWeightedRandomList.single(context.lookup(BovinesRegistryKeys.COW_TYPE).getOrThrow(MooshroomKeys.BROWN_MUSHROOM)), Optional.of(ColorParticleOption.create(BovinesParticleTypes.SHROOM, ColorConstants.RED_MUSHROOM))),
                new BlockReference<>(Optional.of(Blocks.RED_MUSHROOM.defaultBlockState()), Optional.empty(), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/mooshroom/mooshroom_mycelium_layer"), List.of(new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))
                ),
                Optional.empty(),
                Optional.of(MushroomCow.MushroomType.RED),
                OffspringConditions.EMPTY
        )));
        context.register(MooshroomKeys.BROWN_MUSHROOM, new CowType<>(BovinesCowTypeTypes.MOOSHROOM_TYPE, new MooshroomConfiguration(
                new CowTypeConfiguration.Settings(Optional.of(ResourceLocation.parse("cow/brown_mooshroom")), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.single(context.lookup(BovinesRegistryKeys.COW_TYPE).getOrThrow(MooshroomKeys.RED_MUSHROOM)), Optional.of(ColorParticleOption.create(BovinesParticleTypes.SHROOM, ColorConstants.BROWN_MUSHROOM))),
                new BlockReference<>(Optional.of(Blocks.BROWN_MUSHROOM.defaultBlockState()), Optional.empty(), Optional.empty()),
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/mooshroom/mooshroom_mycelium_layer"), List.of(new FallbackModifierFactory(List.of()))),
                        new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/snow_layer"), List.of(new ConditionedModifierFactory(BovinesAndButtercups.asResource("snow_layer_with_snow"),
                                List.of(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(snowTag)).build()).build()),1)))
                ),
                Optional.empty(),
                Optional.of(MushroomCow.MushroomType.BROWN),
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
