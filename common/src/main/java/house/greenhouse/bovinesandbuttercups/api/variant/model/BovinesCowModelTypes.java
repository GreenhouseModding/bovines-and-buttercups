package house.greenhouse.bovinesandbuttercups.api.variant.model;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class BovinesCowModelTypes {
    public static final CowModelType TEMPERATE = register(BovinesAndButtercups.asResource("temperate"), new CowModelType(null, null));
    public static final CowModelType WARM = register(BovinesAndButtercups.asResource("warm"), new CowModelType(BovinesAndButtercups.MOD_ID, "warm_cow"));
    public static final CowModelType COLD = register(BovinesAndButtercups.asResource("cold"), new CowModelType(BovinesAndButtercups.MOD_ID, "cold_cow"));
    public static final CowModelType LUSH = register(BovinesAndButtercups.asResource("lush"), new CowModelType(BovinesAndButtercups.MOD_ID, "lush_cow"));
    public static final CowModelType SCULK = register(BovinesAndButtercups.asResource("sculk"), new CowModelType(BovinesAndButtercups.MOD_ID, "sculk_cow"));

    @Deprecated
    public static final CowModelType BUFFALO = register(BovinesAndButtercups.asResource("buffalo"), new CowModelType(BovinesAndButtercups.MOD_ID, "buffalo_cow"));
    @Deprecated
    public static final CowModelType DEFAULT = register(BovinesAndButtercups.asResource("default"), new CowModelType(null, null));
    @Deprecated
    public static final CowModelType HIGHLAND = register(BovinesAndButtercups.asResource("highland"), new CowModelType(BovinesAndButtercups.MOD_ID, "highland_cow"));
    @Deprecated
    public static final CowModelType OX = register(BovinesAndButtercups.asResource("ox"), new CowModelType(BovinesAndButtercups.MOD_ID, "ox_cow"));
    @Deprecated
    public static final CowModelType FLAT = register(BovinesAndButtercups.asResource("flat"), new CowModelType(BovinesAndButtercups.MOD_ID, "flat_cow"));

    public static void registerAll() {}

    private static CowModelType register(ResourceLocation id, CowModelType type) {
        return Registry.register(BovinesRegistries.MODEL_TYPE, id, type);
    }

}