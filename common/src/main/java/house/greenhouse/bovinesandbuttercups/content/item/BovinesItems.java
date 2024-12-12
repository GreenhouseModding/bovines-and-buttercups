package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import house.greenhouse.bovinesandbuttercups.util.BovinesFoods;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;

import java.util.List;

public class BovinesItems {
    public static final Item MOOBLOOM_SPAWN_EGG = new SpawnEggItem(BovinesEntityTypes.MOOBLOOM, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("moobloom_spawn_egg"))));

    public static final BlockItem BUTTERCUP = new BlockItem(BovinesBlocks.BUTTERCUP, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("buttercup"))));
    public static final BlockItem PINK_DAISY = new BlockItem(BovinesBlocks.PINK_DAISY, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("pink_daisy"))));
    public static final BlockItem LIMELIGHT = new BlockItem(BovinesBlocks.LIMELIGHT, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("limelight"))));
    public static final BlockItem BIRD_OF_PARADISE = new BlockItem(BovinesBlocks.BIRD_OF_PARADISE, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("bird_of_paradise"))));
    public static final BlockItem CHARGELILY = new BlockItem(BovinesBlocks.CHARGELILY, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("chargelily"))));
    public static final BlockItem HYACINTH = new BlockItem(BovinesBlocks.HYACINTH, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("hyacinth"))));
    public static final BlockItem SNOWDROP = new BlockItem(BovinesBlocks.SNOWDROP, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("snowdrop"))));
    public static final BlockItem TROPICAL_BLUE = new BlockItem(BovinesBlocks.TROPICAL_BLUE, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("tropical_blue"))));
    public static final BlockItem FREESIA = new BlockItem(BovinesBlocks.FREESIA, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("freesia"))));
    public static final BlockItem LINGHOLM = new BlockItem(BovinesBlocks.LINGHOLM, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("lingholm"))));
    public static final BlockItem CAMELLIA = new BlockItem(BovinesBlocks.CAMELLIA, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("camellia"))));

    public static final NectarBowlItem BIRD_OF_PARADISE_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("bird_of_paradise_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.BIRD_OF_PARADISE_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem BUTTERCUP_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("buttercup_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.BUTTERCUP_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem CAMELLIA_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("camellia_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.CAMELLIA_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem CHARGELILY_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("chargelily_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.CHARGELILY_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem FREESIA_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("freesia_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.FREESIA_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem HYACINTH_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("hyacinth_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.HYACINTH_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem LIMELIGHT_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("limelight_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.LIMELIGHT_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem LINGHOLM_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("lingholm_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.LINGHOLM_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem PINK_DAISY_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("pink_daisy_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.PINK_DAISY_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem SNOWDROP_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("snowdrop_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.SNOWDROP_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));
    public static final NectarBowlItem TROPICAL_BLUE_NECTAR_BOWL = new NectarBowlItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("tropical_blue_nectar_bowl"))).stacksTo(1).component(DataComponents.CONSUMABLE, BovinesFoods.Consumables.TROPICAL_BLUE_NECTAR).usingConvertsTo(Items.BOWL).craftRemainder(Items.BOWL));

    public static final CustomFlowerItem CUSTOM_FLOWER = new CustomFlowerItem(BovinesBlocks.CUSTOM_FLOWER, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("custom_flower"))));
    public static final CustomMushroomItem CUSTOM_MUSHROOM = new CustomMushroomItem(BovinesBlocks.CUSTOM_MUSHROOM, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("custom_mushroom"))));
    public static final CustomHugeMushroomItem CUSTOM_MUSHROOM_BLOCK = new CustomHugeMushroomItem(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("custom_mushroom_block"))));

    public static final Item FLOWER_CROWN = new FlowerCrownItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("flower_crown"))).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(List.of(), false)).component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD).setEquipSound(BovinesSoundEvents.EQUIP_FLOWER_CROWN).setDamageOnHurt(false).build()));
    public static final PlaceableEdibleItem PLACEABLE_EDIBLE = new PlaceableEdibleItem(BovinesBlocks.PLACEABLE_EDIBLE, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("placeable_edible"))));

    public static final Item RICH_HONEY_BOTTLE = new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("rich_honey_bottle"))).craftRemainder(Items.GLASS_BOTTLE).food(BovinesFoods.Properties.RICH_HONEY_BOTTLE, BovinesFoods.Consumables.RICH_HONEY_BOTTLE).stacksTo(16));
    public static final BlockItem RICH_HONEY_BLOCK = new BlockItem(BovinesBlocks.RICH_HONEY_BLOCK, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BovinesAndButtercups.asResource("rich_honey_block"))));

    public static void registerAll(RegistrationCallback<Item> callback) {
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("moobloom_spawn_egg"), MOOBLOOM_SPAWN_EGG);

        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("buttercup"), BUTTERCUP);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("pink_daisy"), PINK_DAISY);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("limelight"), LIMELIGHT);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("bird_of_paradise"), BIRD_OF_PARADISE);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("chargelily"), CHARGELILY);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("hyacinth"), HYACINTH);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("snowdrop"), SNOWDROP);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("tropical_blue"), TROPICAL_BLUE);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("freesia"), FREESIA);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("lingholm"), LINGHOLM);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("camellia"), CAMELLIA);

        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("bird_of_paradise_nectar_bowl"), BIRD_OF_PARADISE_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("buttercup_nectar_bowl"), BUTTERCUP_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("camellia_nectar_bowl"), CAMELLIA_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("chargelily_nectar_bowl"), CHARGELILY_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("freesia_nectar_bowl"), FREESIA_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("hyacinth_nectar_bowl"), HYACINTH_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("limelight_nectar_bowl"), LIMELIGHT_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("lingholm_nectar_bowl"), LINGHOLM_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("pink_daisy_nectar_bowl"), PINK_DAISY_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("snowdrop_nectar_bowl"), SNOWDROP_NECTAR_BOWL);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("tropical_blue_nectar_bowl"), TROPICAL_BLUE_NECTAR_BOWL);

        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("custom_flower"), CUSTOM_FLOWER);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("custom_mushroom"), CUSTOM_MUSHROOM);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("custom_mushroom_block"), CUSTOM_MUSHROOM_BLOCK);

        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("flower_crown"), FLOWER_CROWN);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("placeable_edible"), PLACEABLE_EDIBLE);

        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("rich_honey_bottle"), RICH_HONEY_BOTTLE);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("rich_honey_block"), RICH_HONEY_BLOCK);
    }
}
