package house.greenhouse.bovinesandbuttercups.content.data.edible;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.NectarEffects;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class BovinesEdibleBlockTypes {
    public static final ResourceKey<EdibleBlockType> ALSTROEMERIA_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("alstroemeria_cupcake"));
    public static final ResourceKey<EdibleBlockType> BUTTERCUP_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("buttercup_cupcake"));
    public static final ResourceKey<EdibleBlockType> CAMELLIA_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("camellia_cupcake"));
    public static final ResourceKey<EdibleBlockType> CHARGELILY_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("chargelily_cupcake"));
    public static final ResourceKey<EdibleBlockType> FREESIA_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("freesia_cupcake"));
    public static final ResourceKey<EdibleBlockType> HYACINTH_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("hyacinth_cupcake"));
    public static final ResourceKey<EdibleBlockType> LIMELIGHT_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("limelight_cupcake"));
    public static final ResourceKey<EdibleBlockType> LINGHOLM_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("lingholm_cupcake"));
    public static final ResourceKey<EdibleBlockType> PINK_DAISY_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("pink_daisy_cupcake"));
    public static final ResourceKey<EdibleBlockType> SOMBERCUP_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("sombercup_cupcake"));
    public static final ResourceKey<EdibleBlockType> SNOWDROP_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("snowdrop_cupcake"));
    public static final ResourceKey<EdibleBlockType> TROPICAL_BLUE_CUPCAKE = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("tropical_blue_cupcake"));

    public static final ResourceKey<EdibleBlockType> BROWN_MUSHROOM_PUFF_PASTRY = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("brown_mushroom_puff_pastry"));
    public static final ResourceKey<EdibleBlockType> RED_MUSHROOM_PUFF_PASTRY = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("red_mushroom_puff_pastry"));
    public static final ResourceKey<EdibleBlockType> SUSPICIOUS_BROWN_MUSHROOM_PUFF_PASTRY = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("suspicious_brown_mushroom_puff_pastry"));
    public static final ResourceKey<EdibleBlockType> SUSPICIOUS_RED_MUSHROOM_PUFF_PASTRY = ResourceKey.create(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesAndButtercups.asResource("suspicious_red_mushroom_puff_pastry"));

    public static void bootstrap(BootstrapContext<EdibleBlockType> context) {
        context.register(ALSTROEMERIA_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.SLOW_FALLING, 7200)))));
        context.register(BUTTERCUP_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.POISON, 2400)))));
        context.register(CAMELLIA_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.HUNGER, 2400)))));
        context.register(CHARGELILY_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.DIG_SPEED, 1200)))));
        context.register(FREESIA_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.WATER_BREATHING, 9600)))));
        context.register(HYACINTH_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.WITHER, 2400)))));
        context.register(LIMELIGHT_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.REGENERATION, 2400)))));
        context.register(LINGHOLM_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.MOVEMENT_SPEED, 2400)))));
        context.register(PINK_DAISY_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.DAMAGE_BOOST, 2400)))));
        context.register(SOMBERCUP_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.DARKNESS, 2400)))));
        context.register(SNOWDROP_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.DIG_SLOWDOWN, 7200)))));
        context.register(TROPICAL_BLUE_CUPCAKE, EdibleBlockType.cupcake(context, new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.FIRE_RESISTANCE, 9600)))));

        context.register(BROWN_MUSHROOM_PUFF_PASTRY, EdibleBlockType.puffPastry(context));
        context.register(RED_MUSHROOM_PUFF_PASTRY, EdibleBlockType.puffPastry(context));
        context.register(SUSPICIOUS_BROWN_MUSHROOM_PUFF_PASTRY, EdibleBlockType.suspiciousPuffPastry(context));
        context.register(SUSPICIOUS_RED_MUSHROOM_PUFF_PASTRY, EdibleBlockType.suspiciousPuffPastry(context));
    }
}
