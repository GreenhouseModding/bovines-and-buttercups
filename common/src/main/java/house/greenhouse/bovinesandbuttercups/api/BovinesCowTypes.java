package house.greenhouse.bovinesandbuttercups.api;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.CowConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class BovinesCowTypes {
    public static final CowType<MoobloomConfiguration> MOOBLOOM_TYPE = register(BovinesAndButtercups.asResource("moobloom"), new CowType<>(MoobloomConfiguration.CODEC, List.of(BovinesEntityTypes.MOOBLOOM), BovinesCowVariants.MoobloomKeys.BUTTERCUP, "bovinesandbuttercups/moobloom/%s_moobloom"));
    public static final CowType<MooshroomConfiguration> MOOSHROOM_TYPE = register(BovinesAndButtercups.asResource("mooshroom"), new CowType<>(MooshroomConfiguration.CODEC, List.of(EntityType.MOOSHROOM), BovinesCowVariants.MooshroomKeys.RED_MUSHROOM,"bovinesandbuttercups/mooshroom/%s_mooshroom", true));
    public static final CowType<CowConfiguration> COW_TYPE = register(BovinesAndButtercups.asResource("cow"), new CowType<>(CowConfiguration.CODEC, List.of(EntityType.COW), BovinesCowVariants.CowKeys.DEFAULT_COW,"bovinesandbuttercups/cow/%s_cow", true));

    public static void registerAll() {}

    private static <T extends BaseCowConfiguration> CowType<T> register(ResourceLocation id, CowType<T> componentType) {
        return Registry.register(BovinesRegistries.COW_TYPE, id, componentType);
    }
}