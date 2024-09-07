package house.greenhouse.bovinesandbuttercups.registry;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.util.ColorConstants;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class BovinesFlowerCrownMaterials {
    public static final ResourceKey<FlowerCrownMaterial> BIRD_OF_PARADISE = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("bird_of_paradise"));
    public static final ResourceKey<FlowerCrownMaterial> BUTTERCUP = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("buttercup"));
    public static final ResourceKey<FlowerCrownMaterial> CHARGELILY = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("chargelily"));
    public static final ResourceKey<FlowerCrownMaterial> FREESIA = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("freesia"));
    public static final ResourceKey<FlowerCrownMaterial> HYACINTH = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("hyacinth"));
    public static final ResourceKey<FlowerCrownMaterial> LIMELIGHT = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("limelight"));
    public static final ResourceKey<FlowerCrownMaterial> LINGHOLM = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("lingholm"));
    public static final ResourceKey<FlowerCrownMaterial> PINK_DAISY = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("pink_daisy"));
    public static final ResourceKey<FlowerCrownMaterial> SNOWDROP = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("snowdrop"));
    public static final ResourceKey<FlowerCrownMaterial> TROPICAL_BLUE = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("tropical_blue"));

    public static void bootstrap(BootstrapContext<FlowerCrownMaterial> context) {
        context.register(BIRD_OF_PARADISE, createFromKey(BovinesItems.BIRD_OF_PARADISE, BIRD_OF_PARADISE, ColorConstants.BIRD_OF_PARADISE));
        context.register(BUTTERCUP, createFromKey(BovinesItems.BUTTERCUP, BUTTERCUP, ColorConstants.BUTTERCUP));
        context.register(CHARGELILY, createFromKey(BovinesItems.CHARGELILY, CHARGELILY, ColorConstants.CHARGELILY));
        context.register(FREESIA, createFromKey(BovinesItems.FREESIA, FREESIA, ColorConstants.FREESIA));
        context.register(HYACINTH, createFromKey(BovinesItems.HYACINTH, HYACINTH, ColorConstants.HYACINTH));
        context.register(LIMELIGHT, createFromKey(BovinesItems.LIMELIGHT, LIMELIGHT, ColorConstants.LIMELIGHT));
        context.register(LINGHOLM, createFromKey(BovinesItems.LINGHOLM, LINGHOLM, ColorConstants.LINGHOLM));
        context.register(PINK_DAISY, createFromKey(BovinesItems.PINK_DAISY, PINK_DAISY, ColorConstants.PINK_DAISY));
        context.register(SNOWDROP, createFromKey(BovinesItems.SNOWDROP, SNOWDROP, ColorConstants.SNOWDROP));
        context.register(TROPICAL_BLUE, createFromKey(BovinesItems.TROPICAL_BLUE, TROPICAL_BLUE, ColorConstants.TROPICAL_BLUE));
    }

    public static FlowerCrownMaterial createFromKey(ItemLike item, ResourceKey<FlowerCrownMaterial> key, int color) {
        return new FlowerCrownMaterial(new ItemStack(item),
                new FlowerCrownMaterial.ItemTextures(
                        key.location().withPath(str -> "bovinesandbuttercups/petals/items/top_left_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/items/top_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/items/top_right_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/items/center_left_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/items/center_right_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/items/bottom_left_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/items/bottom_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/items/bottom_right_" + str)
                ),
                new FlowerCrownMaterial.EquippedTextures(
                        key.location().withPath(str -> "bovinesandbuttercups/petals/models/top_left_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/models/top_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/models/top_right_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/models/center_left_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/models/center_right_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/models/bottom_left_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/models/bottom_" + str),
                        key.location().withPath(str -> "bovinesandbuttercups/petals/models/bottom_right_" + str)
                ),
                Component.translatable(key.location().toLanguageKey("flower_crown_material")).withColor(color));
    }
}