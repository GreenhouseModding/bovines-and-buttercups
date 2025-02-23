package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class BovinesBlockEntityTypes {
    public static final BlockEntityType<CustomFlowerBlockEntity> CUSTOM_FLOWER = register(BovinesAndButtercups.asResource("custom_flower"), new BlockEntityType<>(CustomFlowerBlockEntity::new, Set.of(BovinesBlocks.CUSTOM_FLOWER)));
    public static final BlockEntityType<CustomMushroomBlockEntity> CUSTOM_MUSHROOM = register(BovinesAndButtercups.asResource("custom_mushroom"), new BlockEntityType<>(CustomMushroomBlockEntity::new, Set.of(BovinesBlocks.CUSTOM_MUSHROOM)));
    public static final BlockEntityType<CustomHugeMushroomBlockEntity> CUSTOM_MUSHROOM_BLOCK = register(BovinesAndButtercups.asResource("custom_mushroom_block"), new BlockEntityType<>(CustomHugeMushroomBlockEntity::new, Set.of(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK)));
    public static final BlockEntityType<CustomFlowerPotBlockEntity> POTTED_CUSTOM_FLOWER = register(BovinesAndButtercups.asResource("potted_custom_flower"), new BlockEntityType<>(CustomFlowerPotBlockEntity::new, Set.of(BovinesBlocks.POTTED_CUSTOM_FLOWER)));
    public static final BlockEntityType<CustomMushroomPotBlockEntity> POTTED_CUSTOM_MUSHROOM = register(BovinesAndButtercups.asResource("potted_custom_mushroom"), new BlockEntityType<>(CustomMushroomPotBlockEntity::new, Set.of(BovinesBlocks.POTTED_CUSTOM_MUSHROOM)));
    public static final BlockEntityType<PlaceableEdibleBlockEntity> PLACEABLE_EDIBLE = register(BovinesAndButtercups.asResource("placeable_edible"), new BlockEntityType<>(PlaceableEdibleBlockEntity::new, Set.of(BovinesBlocks.PLACEABLE_EDIBLE)));

    public static void registerAll() {}

    private static <T extends BlockEntity> BlockEntityType<T> register(ResourceLocation id, BlockEntityType<T> event) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, event);
    }
}
