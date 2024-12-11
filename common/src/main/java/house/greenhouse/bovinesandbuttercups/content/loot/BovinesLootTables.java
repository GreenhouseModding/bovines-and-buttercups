package house.greenhouse.bovinesandbuttercups.content.loot;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class BovinesLootTables {
    public static final ResourceKey<LootTable> MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("entities/moobloom"));
    public static final ResourceKey<LootTable> SHEAR_BIRD_OF_PARADISE_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/bird_of_paradise"));
    public static final ResourceKey<LootTable> SHEAR_BUTTERCUP_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/buttercup"));
    public static final ResourceKey<LootTable> SHEAR_CHARGELILY_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/chargelily"));
    public static final ResourceKey<LootTable> SHEAR_FREESIA_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/freesia"));
    public static final ResourceKey<LootTable> SHEAR_HYACINTH_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/hyacinth"));
    public static final ResourceKey<LootTable> SHEAR_LIMELIGHT_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/limelight"));
    public static final ResourceKey<LootTable> SHEAR_LINGHOLM_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/lingholm"));
    public static final ResourceKey<LootTable> SHEAR_PINK_DAISY_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/pink_daisy"));
    public static final ResourceKey<LootTable> SHEAR_SNOWDROP_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/snowdrop"));
    public static final ResourceKey<LootTable> SHEAR_TROPICAL_BLUE_MOOBLOOM = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("shearing/moobloom/tropical_blue"));
    public static final ResourceKey<LootTable> RANCH = ResourceKey.create(Registries.LOOT_TABLE, BovinesAndButtercups.asResource("chests/ranch"));
}
