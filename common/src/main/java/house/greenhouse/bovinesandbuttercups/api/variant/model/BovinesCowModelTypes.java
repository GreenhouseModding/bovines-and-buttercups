package house.greenhouse.bovinesandbuttercups.api.variant.model;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;

public class BovinesCowModelTypes {
    public static final CowModelType BUFFALO = new CowModelType(BovinesAndButtercups.MOD_ID, "buffalo","_baby");
    public static final CowModelType DEFAULT = new CowModelType(null, null,"_baby");
    public static final CowModelType HIGHLAND = new CowModelType(BovinesAndButtercups.MOD_ID, "highland","_baby");
    public static final CowModelType OX = new CowModelType(BovinesAndButtercups.MOD_ID, "ox","_baby");
    public static final CowModelType FLAT = new CowModelType(BovinesAndButtercups.MOD_ID, "flat","_baby");

    public static void registerAll(RegistrationCallback<CowModelType> callback) {
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("buffalo"), BUFFALO);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("default"), DEFAULT);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("highland"), HIGHLAND);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("ox"), OX);
        callback.register(BovinesRegistries.MODEL_TYPE, BovinesAndButtercups.asResource("flat"), FLAT);
    }
}
