package house.greenhouse.bovinesandbuttercups.mixin.fabric.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.EdibleBlockBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomMushroomBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TerrainParticle.class)
public class TerrainParticleMixin {
    @ModifyArg(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",  at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/TerrainParticle;setSprite(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
    private TextureAtlasSprite bovinesandbuttercups$useCustomBlocksForParticle(TextureAtlasSprite original, @Local(argsOnly = true) ClientLevel level, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CustomFlowerBlockEntity customFlower && customFlower.getFlowerType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(customFlower.getFlowerType().holder().unwrapKey().get().location());
            if (modelSet != null)
                return StateDefinitionBovinesModelSetType.getBlockModel(modelSet, BovinesBlocks.CUSTOM_FLOWER.defaultBlockState()).getParticleIcon();
        } else if (level.getBlockEntity(pos) instanceof CustomMushroomBlockEntity customMushroom && customMushroom.getMushroomType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(customMushroom.getMushroomType().holder().unwrapKey().get().location());
            if (modelSet != null)
                return StateDefinitionBovinesModelSetType.getBlockModel(modelSet, BovinesBlocks.CUSTOM_MUSHROOM.defaultBlockState()).getParticleIcon();
        } else if (level.getBlockEntity(pos) instanceof CustomHugeMushroomBlockEntity customMushroomBlock && customMushroomBlock.getMushroomType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(customMushroomBlock.getMushroomType().holder().unwrapKey().get().location().withPath(s -> s + "_block"));
            if (modelSet != null)
                return StateDefinitionBovinesModelSetType.getBlockModel(modelSet, BovinesBlocks.CUSTOM_MUSHROOM_BLOCK.defaultBlockState()).getParticleIcon();
        } else if (level.getBlockEntity(pos) instanceof PlaceableEdibleBlockEntity edibleBlock && edibleBlock.getEdibleType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(edibleBlock.getEdibleType().holder().unwrapKey().get().location());
            if (modelSet != null)
                return EdibleBlockBovinesModelSetType.getBlockModel(modelSet, edibleBlock).getParticleIcon();
        }
        return original;
    }
}
