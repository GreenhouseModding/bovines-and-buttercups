package net.merchantpug.bovinesandbuttercups.api;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;

public class BovinesTags {
    public static class BiomeTags {
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_BIRD_OF_PARADISE = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/bird_of_paradise"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_BUTTERCUP  = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/buttercup"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_CHARGELILY = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/chargelily"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_FREESIA = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/freesia"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_HYACINTH = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/hyacinth"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_LIMELIGHT = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/limelight"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_LINGHOLM = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/lingholm"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_PINK_DAISY = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/pink_daisy"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_SNOWDROP = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/snowdrop"));
        public static final TagKey<Biome> HAS_RANCH_STRUCTURE_TROPICAL_BLUE = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_structure/ranch/tropical_blue"));

        public static final TagKey<Biome> HAS_MOOBLOOM_FLOWER_FOREST = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("has_moobloom/flower_forest"));
        public static final TagKey<Biome> PREVENT_COW_SPAWNS = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("prevent_cow_spawns"));
    }

    public static class BlockTags {
        public static final TagKey<Block> MOOBLOOM_FLOWERS = TagKey.create(Registries.BLOCK, BovinesAndButtercups.asResource("moobloom_flowers"));
        public static final TagKey<Block> SNOWDROP_PLACEABLE = TagKey.create(Registries.BLOCK, BovinesAndButtercups.asResource("snowdrop_placeable"));
    }

    public static class ConfiguredFeatureTags {
        public static final TagKey<ConfiguredFeature<?, ?>> RANCH_ALLOWED = TagKey.create(Registries.CONFIGURED_FEATURE, BovinesAndButtercups.asResource("ranch_allowed"));
    }

    public static class ItemTags {
        public static final TagKey<Item> MOOBLOOM_FLOWERS = TagKey.create(Registries.ITEM, BovinesAndButtercups.asResource("moobloom_flowers"));
    }

    public static class StructureTags {
        public static final TagKey<Structure> RANCH = TagKey.create(Registries.STRUCTURE, BovinesAndButtercups.asResource("ranch"));
    }
}
