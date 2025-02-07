package house.greenhouse.bovinesandbuttercups.api.variant.model;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;

public class BovinesCowModelTypes {
    public static final CowModelType TEMPERATE = new CowModelType(null, null,"_baby");
    public static final CowModelType WARM = new CowModelType(BovinesAndButtercups.MOD_ID, "warm_cow","_baby");
    public static final CowModelType COLD = new CowModelType(BovinesAndButtercups.MOD_ID, "cold_cow","_baby");
    public static final CowModelType LUSH = new CowModelType(BovinesAndButtercups.MOD_ID, "lush_cow","_baby");
    public static final CowModelType SCULK = new CowModelType(BovinesAndButtercups.MOD_ID, "sculk_cow","_baby");

    @Deprecated
    public static final CowModelType BUFFALO = new CowModelType(BovinesAndButtercups.MOD_ID, "buffalo_cow","_baby");
    @Deprecated
    public static final CowModelType DEFAULT = new CowModelType(null, null,"_baby");
    @Deprecated
    public static final CowModelType HIGHLAND = new CowModelType(BovinesAndButtercups.MOD_ID, "highland_cow","_baby");
    @Deprecated
    public static final CowModelType OX = new CowModelType(BovinesAndButtercups.MOD_ID, "ox_cow","_baby");
    @Deprecated
    public static final CowModelType FLAT = new CowModelType(BovinesAndButtercups.MOD_ID, "sculk_cow","_baby");

    public static void registerAll(RegistrationCallback<CowModelType> callback) {
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("temperate"), TEMPERATE);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("warm"), WARM);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("cold"), COLD);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("lush"), LUSH);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("sculk"), SCULK);

        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("buffalo"), BUFFALO);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("default"), DEFAULT);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("highland"), HIGHLAND);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("ox"), OX);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("flat"), FLAT);
    }
}
