package house.greenhouse.bovinesandbuttercups.mixin.neoforge.client;

import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.EdibleBlockBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomMushroomBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockModelShaper.class)
public class BlockModelShaperMixin {
    @ModifyVariable(method = "getTexture", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"))
    private BakedModel bovinesandbuttercups$getActualCustomModelModel(BakedModel original, BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CustomFlowerBlockEntity customFlower && customFlower.getFlowerType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(customFlower.getFlowerType().holder().unwrapKey().get().location());
            if (modelSet != null)
                return StateDefinitionBovinesModelSetType.getBlockModel(modelSet, BovinesBlocks.CUSTOM_FLOWER.defaultBlockState());
        } else if (level.getBlockEntity(pos) instanceof CustomMushroomBlockEntity customMushroom && customMushroom.getMushroomType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(customMushroom.getMushroomType().holder().unwrapKey().get().location());
            if (modelSet != null)
                return StateDefinitionBovinesModelSetType.getBlockModel(modelSet, BovinesBlocks.CUSTOM_MUSHROOM.defaultBlockState());
        } else if (level.getBlockEntity(pos) instanceof CustomHugeMushroomBlockEntity customMushroomBlock && customMushroomBlock.getMushroomType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(customMushroomBlock.getMushroomType().holder().unwrapKey().get().location().withPath(s -> s + "_block"));
            if (modelSet != null)
                return StateDefinitionBovinesModelSetType.getBlockModel(modelSet, BovinesBlocks.CUSTOM_MUSHROOM_BLOCK.defaultBlockState());
        } else if (level.getBlockEntity(pos) instanceof PlaceableEdibleBlockEntity edibleBlock && edibleBlock.getEdibleType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(edibleBlock.getEdibleType().holder().unwrapKey().get().location());
            if (modelSet != null)
                return EdibleBlockBovinesModelSetType.getBlockModel(modelSet, edibleBlock);
        }
        return original;
    }
}
