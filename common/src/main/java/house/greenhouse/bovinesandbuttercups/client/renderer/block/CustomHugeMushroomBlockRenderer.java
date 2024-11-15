package house.greenhouse.bovinesandbuttercups.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class CustomHugeMushroomBlockRenderer implements BlockEntityRenderer<CustomHugeMushroomBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CustomHugeMushroomBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(CustomHugeMushroomBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BakedModel bakedModel = StateDefinitionBovinesModelSetType.getBlockModel(BovinesModelSetRegistry.get(BovinesAndButtercups.asResource("missing_mushroom_block")), blockEntity.getBlockState());

        if (blockEntity.getMushroomType() != null && blockEntity.getMushroomType().holder().isBound() && blockEntity.getMushroomType().holder().value().hasHugeBlock()) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(blockEntity.getMushroomType().holder().unwrapKey().get().location().withPath(s -> s + "_block"));

            if (modelSet != null) {
                var newModel = StateDefinitionBovinesModelSetType.getBlockModel(modelSet, blockEntity.getBlockState());
                if (newModel != null)
                    bakedModel = newModel;
            }
        }
        BovinesAndButtercupsClient.getHelper().tesselateBlock(blockRenderDispatcher, blockEntity.getLevel(), bakedModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource, RenderType.cutout(), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }
}
