package house.greenhouse.bovinesandbuttercups.content.data.edible;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class BovinesEdibleBlockTypes {
    public static final ResourceKey<EdibleBlockType> BIRD_OF_PARADISE_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("bird_of_paradise_cupcake"));
    public static final ResourceKey<EdibleBlockType> BUTTERCUP_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("buttercup_cupcake"));
    public static final ResourceKey<EdibleBlockType> CHARGELILY_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("chargelily_cupcake"));
    public static final ResourceKey<EdibleBlockType> FREESIA_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("freesia_cupcake"));
    public static final ResourceKey<EdibleBlockType> HYACINTH_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("hyacinth_cupcake"));
    public static final ResourceKey<EdibleBlockType> LIMELIGHT_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("limelight_cupcake"));
    public static final ResourceKey<EdibleBlockType> LINGHOLM_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("lingholm_cupcake"));
    public static final ResourceKey<EdibleBlockType> PINK_DAISY_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("pink_daisy_cupcake"));
    public static final ResourceKey<EdibleBlockType> SNOWDROP_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("snowdrop_cupcake"));
    public static final ResourceKey<EdibleBlockType> TROPICAL_BLUE_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("tropical_blue_cupcake"));

    public static final ResourceKey<EdibleBlockType> RED_MUSHROOM_PUFF_PASTRY = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("red_mushroom_puff_pastry"));
    public static final ResourceKey<EdibleBlockType> BROWN_MUSHROOM_PUFF_PASTRY = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("brown_mushroom_puff_pastry"));

    public static void bootstrap(BootstrapContext<EdibleBlockType> context) {
        context.register(BIRD_OF_PARADISE_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(BUTTERCUP_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(CHARGELILY_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(FREESIA_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(HYACINTH_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(LIMELIGHT_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(LINGHOLM_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(PINK_DAISY_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(SNOWDROP_CUPCAKE, EdibleBlockType.cupcake(context));
        context.register(TROPICAL_BLUE_CUPCAKE, EdibleBlockType.cupcake(context));

        context.register(BROWN_MUSHROOM_PUFF_PASTRY, EdibleBlockType.puffPastry(context));
        context.register(RED_MUSHROOM_PUFF_PASTRY, EdibleBlockType.puffPastry(context));
    }
}
