package house.greenhouse.bovinesandbuttercups.content.data.nectar;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class BovinesNectars {
    public static final ResourceKey<Nectar> ALSTROEMERIA = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("alstroemeria"));
    public static final ResourceKey<Nectar> BUTTERCUP = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("buttercup"));
    public static final ResourceKey<Nectar> CAMELLIA = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("camellia"));
    public static final ResourceKey<Nectar> CHARGELILY = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("chargelily"));
    public static final ResourceKey<Nectar> FREESIA = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("freesia"));
    public static final ResourceKey<Nectar> HYACINTH = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("hyacinth"));
    public static final ResourceKey<Nectar> LIMELIGHT = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("limelight"));
    public static final ResourceKey<Nectar> LINGHOLM = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("lingholm"));
    public static final ResourceKey<Nectar> PINK_DAISY = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("pink_daisy"));
    public static final ResourceKey<Nectar> SOMBERCUP = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("sombercup"));
    public static final ResourceKey<Nectar> SNOWDROP = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("snowdrop"));
    public static final ResourceKey<Nectar> TROPICAL_BLUE = ResourceKey.create(BovinesRegistryKeys.NECTAR, BovinesAndButtercups.asResource("tropical_blue"));

    public static void bootstrap(BootstrapContext<Nectar> context) {
        context.register(ALSTROEMERIA,
                new Nectar(BovinesAndButtercups.asResource("item/alstroemeria_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.SLOW_FALLING, 7200)))));
        context.register(BUTTERCUP,
                new Nectar(BovinesAndButtercups.asResource("item/buttercup_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.POISON, 2400)))));
        context.register(CAMELLIA,
                new Nectar(BovinesAndButtercups.asResource("item/camellia_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.HUNGER, 2400)))));
        context.register(CHARGELILY,
                new Nectar(BovinesAndButtercups.asResource("item/chargelily_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.DIG_SPEED, 1200)))));
        context.register(FREESIA,
                new Nectar(BovinesAndButtercups.asResource("item/freesia_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.WATER_BREATHING, 9600)))));
        context.register(HYACINTH,
                new Nectar(BovinesAndButtercups.asResource("item/hyacinth_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.WITHER, 2400)))));
        context.register(LIMELIGHT,
                new Nectar(BovinesAndButtercups.asResource("item/limelight_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.REGENERATION, 2400)))));
        context.register(LINGHOLM,
                new Nectar(BovinesAndButtercups.asResource("item/lingholm_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.MOVEMENT_SPEED, 2400)))));
        context.register(PINK_DAISY,
                new Nectar(BovinesAndButtercups.asResource("item/pink_daisy_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.DAMAGE_BOOST, 2400)))));
        context.register(SOMBERCUP,
                new Nectar(BovinesAndButtercups.asResource("item/sombercup_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.DARKNESS, 2400)))));
        context.register(SNOWDROP,
                new Nectar(BovinesAndButtercups.asResource("item/snowdrop_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.DIG_SLOWDOWN, 7200)))));
        context.register(TROPICAL_BLUE,
                new Nectar(BovinesAndButtercups.asResource("item/tropical_blue_nectar_bowl"),
                        new NectarEffects(List.of(new NectarEffects.Entry(MobEffects.FIRE_RESISTANCE, 9600)))));
    }
}