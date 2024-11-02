package house.greenhouse.bovinesandbuttercups.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.EdibleBlockBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class PlaceableEdibleBlockRenderer implements BlockEntityRenderer<PlaceableEdibleBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public PlaceableEdibleBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(PlaceableEdibleBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BakedModel bakedModel = EdibleBlockBovinesModelSetType.getBlockModel(BovinesModelSetRegistry.get(BovinesAndButtercups.asResource("missing_edible")), blockEntity);

        if (blockEntity.getEdibleType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(blockEntity.getEdibleType().holder().unwrapKey().get().location());

            if (modelSet != null) {
                var newModel = EdibleBlockBovinesModelSetType.getBlockModel(modelSet, blockEntity);
                if (newModel != null)
                    bakedModel = newModel;
            }
        }
        blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), bakedModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutout()), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }
}
