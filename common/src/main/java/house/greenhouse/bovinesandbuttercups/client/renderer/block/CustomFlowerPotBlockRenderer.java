package house.greenhouse.bovinesandbuttercups.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CustomFlowerPotBlockRenderer implements BlockEntityRenderer<CustomFlowerPotBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CustomFlowerPotBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(CustomFlowerPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BakedModel bakedModel = StateDefinitionBovinesModelSetType.getBlockModel(BovinesModelSetRegistry.get(BovinesAndButtercups.asResource("potted_missing_flower")), blockEntity.getBlockState());

        if (blockEntity.getFlowerType() != null && blockEntity.getFlowerType().holder().isBound() && blockEntity.getFlowerType().holder().value().hasPotted()) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(blockEntity.getFlowerType().holder().unwrapKey().get().location().withPath(s -> "potted_" + s));

            if (modelSet != null) {
                var newModel = StateDefinitionBovinesModelSetType.getBlockModel(modelSet, blockEntity.getBlockState());
                if (newModel != null)
                    bakedModel = newModel;
            }
        }
        BovinesAndButtercupsClient.getHelper().tesselateBlock(blockRenderDispatcher, blockEntity.getLevel(), bakedModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource, RenderType.cutout(), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }

    @Override
    public boolean shouldRender(CustomFlowerPotBlockEntity blockEntity, Vec3 cameraPos) {
        return true;
    }
}
