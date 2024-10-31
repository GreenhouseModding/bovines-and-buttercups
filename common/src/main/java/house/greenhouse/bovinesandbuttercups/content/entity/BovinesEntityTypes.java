package house.greenhouse.bovinesandbuttercups.content.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.platform.BovinesPlatform;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BovinesEntityTypes {
    public static final EntityType<Moobloom> MOOBLOOM = EntityType.Builder.of(BovinesAndButtercups.getHelper()::createMoobloom, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10).build(getKey(() -> BovinesAndButtercups.asResource("moobloom").toString()));

    @Nullable
    private static String getKey(Supplier<String> key) {
        return BovinesAndButtercups.getHelper().getPlatform() == BovinesPlatform.FABRIC ? null : key.get();
    }

    public static void registerAll(RegistrationCallback<EntityType<?>> callback) {
        callback.register(BuiltInRegistries.ENTITY_TYPE, BovinesAndButtercups.asResource("moobloom"), MOOBLOOM);
    }

}
