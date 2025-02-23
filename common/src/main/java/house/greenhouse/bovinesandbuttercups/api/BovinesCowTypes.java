package house.greenhouse.bovinesandbuttercups.api;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class BovinesCowTypes {
    public static final CowType<MoobloomConfiguration> MOOBLOOM_TYPE = register(BovinesAndButtercups.asResource("moobloom"), new CowType<>(MoobloomConfiguration.CODEC, List.of(BovinesEntityTypes.MOOBLOOM), BovinesCowVariants.MoobloomKeys.MISSING_MOOBLOOM, "bovinesandbuttercups/moobloom/%s_moobloom", MoobloomConfiguration::createMissing));
    public static final CowType<MooshroomConfiguration> MOOSHROOM_TYPE = register(BovinesAndButtercups.asResource("mooshroom"), new CowType<>(MooshroomConfiguration.CODEC, List.of(EntityType.MOOSHROOM), BovinesCowVariants.MooshroomKeys.MISSING_MOOSHROOM,"bovinesandbuttercups/mooshroom/%s_mooshroom", MooshroomConfiguration::createMissing));

    public static void registerAll() {}

    private static <T extends CowConfiguration> CowType<T> register(ResourceLocation id, CowType<T> componentType) {
        return Registry.register(BovinesRegistries.COW_TYPE, id, componentType);
    }
}