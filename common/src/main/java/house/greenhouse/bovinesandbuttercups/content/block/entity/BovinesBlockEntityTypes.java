package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BovinesBlockEntityTypes {
    public static final BlockEntityType<CustomFlowerBlockEntity> CUSTOM_FLOWER = register(BovinesAndButtercups.asResource("custom_flower"), BlockEntityType.Builder.of(CustomFlowerBlockEntity::new, BovinesBlocks.CUSTOM_FLOWER).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_flower").toString())));
    public static final BlockEntityType<CustomMushroomBlockEntity> CUSTOM_MUSHROOM = register(BovinesAndButtercups.asResource("custom_mushroom"), BlockEntityType.Builder.of(CustomMushroomBlockEntity::new, BovinesBlocks.CUSTOM_MUSHROOM).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_mushroom").toString())));
    public static final BlockEntityType<CustomFlowerPotBlockEntity> POTTED_CUSTOM_FLOWER = register(BovinesAndButtercups.asResource("potted_custom_flower"), BlockEntityType.Builder.of(CustomFlowerPotBlockEntity::new, BovinesBlocks.POTTED_CUSTOM_FLOWER).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("potted_custom_flower").toString())));
    public static final BlockEntityType<CustomMushroomPotBlockEntity> POTTED_CUSTOM_MUSHROOM = register(BovinesAndButtercups.asResource("potted_custom_mushroom"), BlockEntityType.Builder.of(CustomMushroomPotBlockEntity::new, BovinesBlocks.POTTED_CUSTOM_MUSHROOM).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("potted_custom_mushroom").toString())));
    public static final BlockEntityType<CustomHugeMushroomBlockEntity> CUSTOM_MUSHROOM_BLOCK = register(BovinesAndButtercups.asResource("custom_mushroom_block"), BlockEntityType.Builder.of(CustomHugeMushroomBlockEntity::new, BovinesBlocks.CUSTOM_MUSHROOM_BLOCK).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_mushroom_block").toString())));
    public static final BlockEntityType<PlaceableEdibleBlockEntity> PLACEABLE_EDIBLE = register(BovinesAndButtercups.asResource("placeable_edible"), BlockEntityType.Builder.of(PlaceableEdibleBlockEntity::new, BovinesBlocks.PLACEABLE_EDIBLE).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("placeable_edible").toString())));

    public static void registerAll() {}

    private static <T extends BlockEntity> BlockEntityType<T> register(ResourceLocation id, BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type);
    }
}
