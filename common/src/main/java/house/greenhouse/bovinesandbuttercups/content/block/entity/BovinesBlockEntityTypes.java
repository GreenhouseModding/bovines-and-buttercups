package house.greenhouse.bovinesandbuttercups.content.block.entity;

import com.mojang.datafixers.types.Type;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.platform.BovinesPlatform;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BovinesBlockEntityTypes {
    public static final BlockEntityType<CustomFlowerBlockEntity> CUSTOM_FLOWER = BlockEntityType.Builder.of(CustomFlowerBlockEntity::new, BovinesBlocks.CUSTOM_FLOWER).build(getType(() -> Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_flower").toString())));
    public static final BlockEntityType<CustomMushroomBlockEntity> CUSTOM_MUSHROOM = BlockEntityType.Builder.of(CustomMushroomBlockEntity::new, BovinesBlocks.CUSTOM_MUSHROOM).build(getType(() -> Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_mushroom").toString())));
    public static final BlockEntityType<CustomFlowerPotBlockEntity> POTTED_CUSTOM_FLOWER = BlockEntityType.Builder.of(CustomFlowerPotBlockEntity::new, BovinesBlocks.POTTED_CUSTOM_FLOWER).build(getType(() -> Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_potted_flower").toString())));
    public static final BlockEntityType<CustomMushroomPotBlockEntity> POTTED_CUSTOM_MUSHROOM = BlockEntityType.Builder.of(CustomMushroomPotBlockEntity::new, BovinesBlocks.POTTED_CUSTOM_MUSHROOM).build(getType(() -> Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_potted_mushroom").toString())));
    public static final BlockEntityType<CustomHugeMushroomBlockEntity> CUSTOM_MUSHROOM_BLOCK = BlockEntityType.Builder.of(CustomHugeMushroomBlockEntity::new, BovinesBlocks.CUSTOM_MUSHROOM_BLOCK).build(getType(() -> Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_mushroom_block").toString())));

    @Nullable
    private static Type<?> getType(Supplier<Type<?>> type) {
        return BovinesAndButtercups.getHelper().getPlatform() == BovinesPlatform.FABRIC ? null : type.get();
    }
    
    public static void registerAll(RegistrationCallback<BlockEntityType<?>> callback) {
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_flower"), CUSTOM_FLOWER);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_mushroom"), CUSTOM_MUSHROOM);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("potted_custom_flower"), POTTED_CUSTOM_FLOWER);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("potted_custom_mushroom"), POTTED_CUSTOM_MUSHROOM);
        callback.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_mushroom_block"), CUSTOM_MUSHROOM_BLOCK);
    }
}
