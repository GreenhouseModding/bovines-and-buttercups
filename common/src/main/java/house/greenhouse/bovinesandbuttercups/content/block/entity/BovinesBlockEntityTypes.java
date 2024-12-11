package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class BovinesBlockEntityTypes {
    public static final BlockEntityType<CustomFlowerBlockEntity> CUSTOM_FLOWER = new BlockEntityType<>(CustomFlowerBlockEntity::new, Set.of(BovinesBlocks.CUSTOM_FLOWER));
    public static final BlockEntityType<CustomMushroomBlockEntity> CUSTOM_MUSHROOM = new BlockEntityType<>(CustomMushroomBlockEntity::new, Set.of(BovinesBlocks.CUSTOM_MUSHROOM));
    public static final BlockEntityType<CustomFlowerPotBlockEntity> POTTED_CUSTOM_FLOWER = new BlockEntityType<>(CustomFlowerPotBlockEntity::new, Set.of(BovinesBlocks.POTTED_CUSTOM_FLOWER));
    public static final BlockEntityType<CustomMushroomPotBlockEntity> POTTED_CUSTOM_MUSHROOM = new BlockEntityType<>(CustomMushroomPotBlockEntity::new, Set.of(BovinesBlocks.POTTED_CUSTOM_MUSHROOM));
    public static final BlockEntityType<CustomHugeMushroomBlockEntity> CUSTOM_MUSHROOM_BLOCK = new BlockEntityType<>(CustomHugeMushroomBlockEntity::new, Set.of(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK));
    public static final BlockEntityType<PlaceableEdibleBlockEntity> PLACEABLE_EDIBLE = new BlockEntityType<>(PlaceableEdibleBlockEntity::new, Set.of(BovinesBlocks.PLACEABLE_EDIBLE));
    
    public static void registerAll(RegistrationCallback<BlockEntityType<?>> callback) {
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_flower"), CUSTOM_FLOWER);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_mushroom"), CUSTOM_MUSHROOM);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_mushroom_block"), CUSTOM_MUSHROOM_BLOCK);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("potted_custom_flower"), POTTED_CUSTOM_FLOWER);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("potted_custom_mushroom"), POTTED_CUSTOM_MUSHROOM);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("placeable_edible"), PLACEABLE_EDIBLE);
    }
}
