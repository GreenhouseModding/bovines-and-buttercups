package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.util.BovinesFoods;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BovinesItems {
    public static final Item MOOBLOOM_SPAWN_EGG = register(BovinesAndButtercups.asResource("moobloom_spawn_egg"), new SpawnEggItem(BovinesEntityTypes.MOOBLOOM, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("moobloom_spawn_egg")))));

    public static final BlockItem BUTTERCUP = register(BovinesAndButtercups.asResource("buttercup"), new BlockItem(BovinesBlocks.BUTTERCUP, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("buttercup")))));
    public static final BlockItem PINK_DAISY = register(BovinesAndButtercups.asResource("pink_daisy"), new BlockItem(BovinesBlocks.PINK_DAISY, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("pink_daisy")))));
    public static final BlockItem LIMELIGHT = register(BovinesAndButtercups.asResource("limelight"), new BlockItem(BovinesBlocks.LIMELIGHT, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("limelight")))));
    public static final BlockItem ALSTROEMERIA = register(BovinesAndButtercups.asResource("alstroemeria"), new BlockItem(BovinesBlocks.ALSTROEMERIA, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("alstroemeria")))));
    public static final BlockItem CHARGELILY = register(BovinesAndButtercups.asResource("chargelily"), new BlockItem(BovinesBlocks.CHARGELILY, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("chargelily")))));
    public static final BlockItem HYACINTH = register(BovinesAndButtercups.asResource("hyacinth"), new BlockItem(BovinesBlocks.HYACINTH, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("hyacinth")))));
    public static final BlockItem SNOWDROP = register(BovinesAndButtercups.asResource("snowdrop"), new BlockItem(BovinesBlocks.SNOWDROP, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("snowdrop")))));
    public static final BlockItem TROPICAL_BLUE = register(BovinesAndButtercups.asResource("tropical_blue"), new BlockItem(BovinesBlocks.TROPICAL_BLUE, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("tropical_blue")))));
    public static final BlockItem FREESIA = register(BovinesAndButtercups.asResource("freesia"), new BlockItem(BovinesBlocks.FREESIA, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("freesia")))));
    public static final BlockItem LINGHOLM = register(BovinesAndButtercups.asResource("lingholm"), new BlockItem(BovinesBlocks.LINGHOLM, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("lingholm")))));
    public static final BlockItem CAMELLIA = register(BovinesAndButtercups.asResource("camellia"), new BlockItem(BovinesBlocks.CAMELLIA, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("camellia")))));
    public static final BlockItem SOMBERCUP = register(BovinesAndButtercups.asResource("sombercup"), new BlockItem(BovinesBlocks.SOMBERCUP, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("sombercup")))));
    public static final BlockItem NIGHTSHADE = register(BovinesAndButtercups.asResource("nightshade"), new BlockItem(BovinesBlocks.NIGHTSHADE, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("nightshade")))));

    public static final NectarBowlItem ALSTROEMERIA_NECTAR_BOWL = register(BovinesAndButtercups.asResource("alstroemeria_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("alstroemeria_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.ALSTROEMERIA_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem BUTTERCUP_NECTAR_BOWL = register(BovinesAndButtercups.asResource("buttercup_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("buttercup_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.BUTTERCUP_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem CAMELLIA_NECTAR_BOWL = register(BovinesAndButtercups.asResource("camellia_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("camellia_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.CAMELLIA_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem CHARGELILY_NECTAR_BOWL = register(BovinesAndButtercups.asResource("chargelily_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("chargelily_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.CHARGELILY_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem FREESIA_NECTAR_BOWL = register(BovinesAndButtercups.asResource("freesia_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("freesia_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.FREESIA_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem HYACINTH_NECTAR_BOWL = register(BovinesAndButtercups.asResource("hyacinth_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("hyacinth_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.HYACINTH_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem LIMELIGHT_NECTAR_BOWL = register(BovinesAndButtercups.asResource("limelight_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("limelight_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.LIMELIGHT_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem LINGHOLM_NECTAR_BOWL = register(BovinesAndButtercups.asResource("lingholm_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("lingholm_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.LINGHOLM_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem NIGHTSHADE_NECTAR_BOWL = register(BovinesAndButtercups.asResource("nightshade_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("nightshade_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.NIGHTSHADE_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem PINK_DAISY_NECTAR_BOWL = register(BovinesAndButtercups.asResource("pink_daisy_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("pink_daisy_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.PINK_DAISY_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem SNOWDROP_NECTAR_BOWL = register(BovinesAndButtercups.asResource("snowdrop_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("snowdrop_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.SNOWDROP_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem SOMBERCUP_NECTAR_BOWL = register(BovinesAndButtercups.asResource("sombercup_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("sombercup_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.SOMBERCUP_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));
    public static final NectarBowlItem TROPICAL_BLUE_NECTAR_BOWL = register(BovinesAndButtercups.asResource("tropical_blue_nectar_bowl"), new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("tropical_blue_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.TROPICAL_BLUE_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL)));

    public static final CustomFlowerItem CUSTOM_FLOWER = register(BovinesAndButtercups.asResource("custom_flower"), new CustomFlowerItem(BovinesBlocks.CUSTOM_FLOWER, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("custom_flower")))));
    public static final CustomMushroomItem CUSTOM_MUSHROOM = register(BovinesAndButtercups.asResource("custom_mushroom"), new CustomMushroomItem(BovinesBlocks.CUSTOM_MUSHROOM, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("custom_mushroom")))));
    public static final CustomHugeMushroomItem CUSTOM_MUSHROOM_BLOCK = register(BovinesAndButtercups.asResource("custom_mushroom_block"), new CustomHugeMushroomItem(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("custom_mushroom_block")))));

    public static final Item FLOWER_CROWN = register(BovinesAndButtercups.asResource("flower_crown"), new FlowerCrownItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("flower_crown"))).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(List.of(), false)).component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD).setEquipSound(BovinesSoundEvents.EQUIP_FLOWER_CROWN).setDamageOnHurt(false).build())));
    public static final PlaceableEdibleItem PLACEABLE_EDIBLE = register(BovinesAndButtercups.asResource("placeable_edible"), new PlaceableEdibleItem(BovinesBlocks.PLACEABLE_EDIBLE, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("placeable_edible")))));

    public static final Item RICH_HONEY_BOTTLE = register(BovinesAndButtercups.asResource("rich_honey_bottle"), new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("rich_honey_bottle"))).craftRemainder(Items.GLASS_BOTTLE).food(BovinesFoods.Properties.RICH_HONEY_BOTTLE, BovinesFoods.Consumables.RICH_HONEY_BOTTLE).stacksTo(16)));
    public static final BlockItem RICH_HONEY_BLOCK = register(BovinesAndButtercups.asResource("rich_honey_block"), new BlockItem(BovinesBlocks.RICH_HONEY_BLOCK, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("rich_honey_block")))));

    public static void registerAll() {}

    private static <T extends Item> T register(ResourceLocation id, T item) {
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }
}
