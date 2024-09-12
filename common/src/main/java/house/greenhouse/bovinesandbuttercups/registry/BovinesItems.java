package house.greenhouse.bovinesandbuttercups.registry;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.component.ItemNectar;
import house.greenhouse.bovinesandbuttercups.content.item.CustomFlowerItem;
import house.greenhouse.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import house.greenhouse.bovinesandbuttercups.content.item.CustomMushroomItem;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.content.item.NectarBowlItem;
import house.greenhouse.bovinesandbuttercups.registry.internal.RegistrationCallback;
import house.greenhouse.bovinesandbuttercups.util.BovinesFoods;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class BovinesItems {
    public static final NectarBowlItem NECTAR_BOWL = new NectarBowlItem(new Item.Properties().stacksTo(1).component(BovinesDataComponents.NECTAR, ItemNectar.EMPTY).craftRemainder(Items.BOWL));
    public static final Item MOOBLOOM_SPAWN_EGG = new SpawnEggItem(BovinesEntityTypes.MOOBLOOM, 0xfad200, 0x437f34, new Item.Properties());

    public static final BlockItem BUTTERCUP = new BlockItem(BovinesBlocks.BUTTERCUP, new Item.Properties());
    public static final BlockItem PINK_DAISY = new BlockItem(BovinesBlocks.PINK_DAISY, new Item.Properties());
    public static final BlockItem LIMELIGHT = new BlockItem(BovinesBlocks.LIMELIGHT, new Item.Properties());
    public static final BlockItem BIRD_OF_PARADISE = new BlockItem(BovinesBlocks.BIRD_OF_PARADISE, new Item.Properties());
    public static final BlockItem CHARGELILY = new BlockItem(BovinesBlocks.CHARGELILY, new Item.Properties());
    public static final BlockItem HYACINTH = new BlockItem(BovinesBlocks.HYACINTH, new Item.Properties());
    public static final BlockItem SNOWDROP = new BlockItem(BovinesBlocks.SNOWDROP, new Item.Properties());
    public static final BlockItem TROPICAL_BLUE = new BlockItem(BovinesBlocks.TROPICAL_BLUE, new Item.Properties());
    public static final BlockItem FREESIA = new BlockItem(BovinesBlocks.FREESIA, new Item.Properties());
    public static final BlockItem LINGHOLM = new BlockItem(BovinesBlocks.LINGHOLM, new Item.Properties());

    public static final CustomFlowerItem CUSTOM_FLOWER = new CustomFlowerItem(BovinesBlocks.CUSTOM_FLOWER, new Item.Properties());
    public static final CustomMushroomItem CUSTOM_MUSHROOM = new CustomMushroomItem(BovinesBlocks.CUSTOM_MUSHROOM, new Item.Properties());
    public static final CustomHugeMushroomItem CUSTOM_MUSHROOM_BLOCK = new CustomHugeMushroomItem(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK, new Item.Properties());

    public static final Item FLOWER_CROWN = new FlowerCrownItem(new Item.Properties().stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(List.of(), false)));

    public static final HoneyBottleItem RICH_HONEY_BOTTLE = new HoneyBottleItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(BovinesFoods.RICH_HONEY_BOTTLE).stacksTo(16));
    public static final BlockItem RICH_HONEY_BLOCK = new BlockItem(BovinesBlocks.RICH_HONEY_BLOCK, new Item.Properties());

    public static void registerAll(RegistrationCallback<Item> callback) {
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("nectar_bowl"), NECTAR_BOWL);
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

        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("custom_flower"), CUSTOM_FLOWER);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("custom_mushroom"), CUSTOM_MUSHROOM);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("custom_mushroom_block"), CUSTOM_MUSHROOM_BLOCK);

        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("flower_crown"), FLOWER_CROWN);

        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("rich_honey_bottle"), RICH_HONEY_BOTTLE);
        callback.register(BuiltInRegistries.ITEM, BovinesAndButtercups.asResource("rich_honey_block"), RICH_HONEY_BLOCK);
    }
}
