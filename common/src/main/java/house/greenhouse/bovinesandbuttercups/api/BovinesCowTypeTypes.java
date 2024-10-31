package house.greenhouse.bovinesandbuttercups.api;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class BovinesCowTypeTypes {
    public static final CowTypeType<MoobloomConfiguration> MOOBLOOM_TYPE = new CowTypeType<>(MoobloomConfiguration.CODEC, List.of(BovinesEntityTypes.MOOBLOOM), BovinesCowTypes.MoobloomKeys.MISSING_MOOBLOOM, "bovinesandbuttercups/moobloom/%s_moobloom", MoobloomConfiguration.DEFAULT);
    public static final CowTypeType<MooshroomConfiguration> MOOSHROOM_TYPE = new CowTypeType<>(MooshroomConfiguration.CODEC, List.of(EntityType.MOOSHROOM), BovinesCowTypes.MooshroomKeys.MISSING_MOOSHROOM,"bovinesandbuttercups/mooshroom/%s_mooshroom", MooshroomConfiguration.DEFAULT);

    public static void registerAll(RegistrationCallback<CowTypeType<?>> callback) {
        callback.register(BovinesRegistries.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"), MOOBLOOM_TYPE);
        callback.register(BovinesRegistries.COW_TYPE_TYPE, BovinesAndButtercups.asResource("mooshroom"), MOOSHROOM_TYPE);
    }
}