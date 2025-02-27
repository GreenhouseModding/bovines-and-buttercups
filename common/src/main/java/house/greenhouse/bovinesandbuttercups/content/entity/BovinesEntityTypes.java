package house.greenhouse.bovinesandbuttercups.content.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.platform.BovinesPlatform;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BovinesEntityTypes {
    public static final EntityType<Moobloom> MOOBLOOM = register(BovinesAndButtercups.asResource("moobloom"), EntityType.Builder.of(BovinesAndButtercups.getHelper()::createMoobloom, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10).build(BovinesAndButtercups.asResource("moobloom").toString()));

    public static void registerAll() {}

    private static <T extends Entity> EntityType<T> register(ResourceLocation id, EntityType<T> entityType) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, entityType);
    }
}
