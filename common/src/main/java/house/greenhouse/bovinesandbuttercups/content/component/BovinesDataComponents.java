package house.greenhouse.bovinesandbuttercups.content.component;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BovinesDataComponents {
    public static final DataComponentType<ItemCustomFlower> CUSTOM_FLOWER = register(BovinesAndButtercups.asResource("custom_flower"), DataComponentType.<ItemCustomFlower>builder()
            .persistent(ItemCustomFlower.CODEC)
            .networkSynchronized(ItemCustomFlower.STREAM_CODEC)
            .build());
    public static final DataComponentType<ItemCustomMushroom> CUSTOM_MUSHROOM = register(BovinesAndButtercups.asResource("custom_mushroom"), DataComponentType.<ItemCustomMushroom>builder()
            .persistent(ItemCustomMushroom.CODEC)
            .networkSynchronized(ItemCustomMushroom.STREAM_CODEC)
            .build());
    public static final DataComponentType<ItemEdible> EDIBLE_TYPE = register(BovinesAndButtercups.asResource("edible_type"), DataComponentType.<ItemEdible>builder()
            .persistent(ItemEdible.CODEC)
            .networkSynchronized(ItemEdible.STREAM_CODEC)
            .build());
    public static final DataComponentType<FlowerCrown> FLOWER_CROWN = register(BovinesAndButtercups.asResource("flower_crown"), DataComponentType.<FlowerCrown>builder()
            .persistent(FlowerCrown.CODEC)
            .networkSynchronized(FlowerCrown.STREAM_CODEC)
            .build());

    public static void registerAll() {}

    private static <T> DataComponentType<T> register(ResourceLocation id, DataComponentType<T> componentType) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, componentType);
    }
}
