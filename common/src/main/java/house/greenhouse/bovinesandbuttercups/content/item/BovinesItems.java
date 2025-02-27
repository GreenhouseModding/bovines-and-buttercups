package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemNectar;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.util.BovinesFoods;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class BovinesItems {
    public static final NectarBowlItem NECTAR_BOWL = register(BovinesAndButtercups.asResource("nectar_bowl"), new NectarBowlItem(new Item.Properties().stacksTo(1).component(BovinesDataComponents.NECTAR, ItemNectar.EMPTY).craftRemainder(Items.BOWL)));
    public static final Item MOOBLOOM_SPAWN_EGG = register(BovinesAndButtercups.asResource("moobloom_spawn_egg"), new SpawnEggItem(BovinesEntityTypes.MOOBLOOM, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final BlockItem BUTTERCUP = register(BovinesAndButtercups.asResource("buttercup"), new BlockItem(BovinesBlocks.BUTTERCUP, new Item.Properties()));
    public static final BlockItem PINK_DAISY = register(BovinesAndButtercups.asResource("pink_daisy"), new BlockItem(BovinesBlocks.PINK_DAISY, new Item.Properties()));
    public static final BlockItem LIMELIGHT = register(BovinesAndButtercups.asResource("limelight"), new BlockItem(BovinesBlocks.LIMELIGHT, new Item.Properties()));
    public static final BlockItem ALSTROEMERIA = register(BovinesAndButtercups.asResource("alstroemeria"), new BlockItem(BovinesBlocks.ALSTROEMERIA, new Item.Properties()));
    public static final BlockItem CHARGELILY = register(BovinesAndButtercups.asResource("chargelily"), new BlockItem(BovinesBlocks.CHARGELILY, new Item.Properties()));
    public static final BlockItem HYACINTH = register(BovinesAndButtercups.asResource("hyacinth"), new BlockItem(BovinesBlocks.HYACINTH, new Item.Properties()));
    public static final BlockItem SNOWDROP = register(BovinesAndButtercups.asResource("snowdrop"), new BlockItem(BovinesBlocks.SNOWDROP, new Item.Properties()));
    public static final BlockItem TROPICAL_BLUE = register(BovinesAndButtercups.asResource("tropical_blue"), new BlockItem(BovinesBlocks.TROPICAL_BLUE, new Item.Properties()));
    public static final BlockItem FREESIA = register(BovinesAndButtercups.asResource("freesia"), new BlockItem(BovinesBlocks.FREESIA, new Item.Properties()));
    public static final BlockItem LINGHOLM = register(BovinesAndButtercups.asResource("lingholm"), new BlockItem(BovinesBlocks.LINGHOLM, new Item.Properties()));
    public static final BlockItem CAMELLIA = register(BovinesAndButtercups.asResource("camellia"), new BlockItem(BovinesBlocks.CAMELLIA, new Item.Properties()));
    public static final BlockItem SOMBERCUP = register(BovinesAndButtercups.asResource("sombercup"), new BlockItem(BovinesBlocks.SOMBERCUP, new Item.Properties()));

    public static final CustomFlowerItem CUSTOM_FLOWER = register(BovinesAndButtercups.asResource("custom_flower"), new CustomFlowerItem(BovinesBlocks.CUSTOM_FLOWER, new Item.Properties()));
    public static final CustomMushroomItem CUSTOM_MUSHROOM = register(BovinesAndButtercups.asResource("custom_mushroom"), new CustomMushroomItem(BovinesBlocks.CUSTOM_MUSHROOM, new Item.Properties()));
    public static final CustomHugeMushroomItem CUSTOM_MUSHROOM_BLOCK = register(BovinesAndButtercups.asResource("custom_mushroom_block"), new CustomHugeMushroomItem(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK, new Item.Properties()));

    public static final Item FLOWER_CROWN = register(BovinesAndButtercups.asResource("flower_crown"), new FlowerCrownItem(new Item.Properties().stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(List.of(), false))));
    public static final PlaceableEdibleItem PLACEABLE_EDIBLE = register(BovinesAndButtercups.asResource("placeable_edible"), new PlaceableEdibleItem(BovinesBlocks.PLACEABLE_EDIBLE, new Item.Properties()));

    public static final HoneyBottleItem RICH_HONEY_BOTTLE = register(BovinesAndButtercups.asResource("rich_honey_bottle"), new HoneyBottleItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(BovinesFoods.RICH_HONEY_BOTTLE).stacksTo(16)));
    public static final BlockItem RICH_HONEY_BLOCK = register(BovinesAndButtercups.asResource("rich_honey_block"), new BlockItem(BovinesBlocks.RICH_HONEY_BLOCK, new Item.Properties()));

    public static void registerAll() {}

    private static <T extends Item> T register(ResourceLocation id, T item) {
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }
}
