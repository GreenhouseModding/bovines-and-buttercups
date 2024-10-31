package house.greenhouse.bovinesandbuttercups.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.BovinesModelSetTypes;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.client.util.BovineModelSetUtil;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CustomMushroomPotBlockRenderer implements BlockEntityRenderer<CustomMushroomPotBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CustomMushroomPotBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(CustomMushroomPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BakedModel bakedModel = StateDefinitionBovinesModelSetType.getBlockModel(BovinesModelSetRegistry.get(BovinesAndButtercups.asResource("potted_missing_mushroom")), blockEntity.getBlockState());

        if (blockEntity.getMushroomType() != null) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(blockEntity.getMushroomType().holder().unwrapKey().get().location().withPath(s -> "potted_" + s));

            if (modelSet != null) {
                var newModel = StateDefinitionBovinesModelSetType.getBlockModel(modelSet, blockEntity.getBlockState());
                if (newModel != null)
                    bakedModel = newModel;
            }
        }
        blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), bakedModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutout()), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }
}
