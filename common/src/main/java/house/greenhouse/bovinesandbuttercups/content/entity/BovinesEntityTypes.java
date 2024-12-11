package house.greenhouse.bovinesandbuttercups.content.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class BovinesEntityTypes {
    public static final EntityType<Moobloom> MOOBLOOM = EntityType.Builder.of(BovinesAndButtercups.getHelper()::createMoobloom, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10).build(ResourceKey.create(Registries.ENTITY_TYPE, BovinesAndButtercups.asResource("moobloom")));

    public static void registerAll(RegistrationCallback<EntityType<?>> callback) {
        callback.register(BuiltInRegistries.ENTITY_TYPE, BovinesAndButtercups.asResource("moobloom"), MOOBLOOM);
    }

}
