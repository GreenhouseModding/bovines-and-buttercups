package house.greenhouse.bovinesandbuttercups.api;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class BovinesCowTypes {
    public static final CowType<MoobloomConfiguration> MOOBLOOM_TYPE = new CowType<>(MoobloomConfiguration.CODEC, List.of(BovinesEntityTypes.MOOBLOOM), BovinesCowVariants.MoobloomKeys.MISSING_MOOBLOOM, "bovinesandbuttercups/moobloom/%s_moobloom", MoobloomConfiguration::createMissing);
    public static final CowType<MooshroomConfiguration> MOOSHROOM_TYPE = new CowType<>(MooshroomConfiguration.CODEC, List.of(EntityType.MOOSHROOM), BovinesCowVariants.MooshroomKeys.MISSING_MOOSHROOM,"bovinesandbuttercups/mooshroom/%s_mooshroom", MooshroomConfiguration::createMissing);

    public static void registerAll(RegistrationCallback<CowType<?>> callback) {
        callback.register(BovinesRegistries.COW_TYPE, BovinesAndButtercups.asResource("moobloom"), MOOBLOOM_TYPE);
        callback.register(BovinesRegistries.COW_TYPE, BovinesAndButtercups.asResource("mooshroom"), MOOSHROOM_TYPE);
    }
}