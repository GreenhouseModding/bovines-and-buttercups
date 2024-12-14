package house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MooshroomDatapackMushroomLayer<T extends MushroomCowRenderState> extends RenderLayer<T, CowModel> {
    private final BlockRenderDispatcher blockRenderer;

    public MooshroomDatapackMushroomLayer(RenderLayerParent<T, CowModel> context, BlockRenderDispatcher blockRenderer) {
        super(context);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T renderState, float yRot, float xRot) {
        boolean bl = renderState.appearsGlowing && renderState.isInvisible;
        Holder<CowType<MooshroomConfiguration>> cowType = ((CowTypeRenderState<MushroomCow, MooshroomConfiguration, CowModel>)renderState).getCowType();

        if (cowType == null || renderState.isInvisible && !bl
                || renderState.isBaby
                || cowType.value().configuration().mushroom().blockState().isPresent() && cowType.value().configuration().vanillaType().isPresent() && cowType.value().configuration().mushroom().blockState().get().equals(cowType.value().configuration().vanillaType().get().getBlockState()))
            return;

        int m = LivingEntityRenderer.getOverlayCoords(renderState, 0.0f);

        BakedModel model = null;
        if (cowType.value().configuration().mushroom().modelSet().isPresent()) {
            var modelSet = BovinesModelSetRegistry.get(cowType.value().configuration().mushroom().modelSet().get());
            if (modelSet != null) {
                model = modelSet.getModel();
                if (model == null)
                    model = StateDefinitionBovinesModelSetType.getBlockModel(BovinesModelSetRegistry.get(BovinesAndButtercups.asResource("missing_mushroom")), BovinesBlocks.CUSTOM_MUSHROOM.defaultBlockState());
            }
        }
        else if (cowType.value().configuration().mushroom().customType().isPresent() && cowType.value().configuration().mushroom().customType().orElseThrow().unwrapKey().isPresent()) {
            var modelSet = BovinesModelSetRegistry.get(cowType.value().configuration().mushroom().customType().orElseThrow().unwrapKey().get().location());
            if (modelSet != null) {
                model = StateDefinitionBovinesModelSetType.getBlockModel(modelSet, BovinesBlocks.CUSTOM_MUSHROOM.defaultBlockState());
                if (model == null)
                    model = StateDefinitionBovinesModelSetType.getBlockModel(BovinesModelSetRegistry.get(BovinesAndButtercups.asResource("missing_mushroom")), BovinesBlocks.CUSTOM_MUSHROOM.defaultBlockState());
            }
        } else if (cowType.value().configuration().mushroom().blockState().isEmpty())
            return;

        handleMooshroomRender(poseStack, bufferSource, packedLight, bl, m, cowType.value().configuration().mushroom().blockState(), model);
    }

    private void handleMooshroomRender(PoseStack poseStack, MultiBufferSource buffer, int i, boolean outlineAndInvisible, int overlay, Optional<BlockState> blockState, @Nullable BakedModel model) {
        poseStack.pushPose();
        poseStack.translate(0.2F, -0.35F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-48.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        this.renderMushroomBlock(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, model);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2F, -0.35F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(42.0F));
        poseStack.translate(0.1F, 0.0F, -0.6F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-48.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        this.renderMushroomBlock(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, model);
        poseStack.popPose();

        poseStack.pushPose();
        this.getParentModel().getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0F, -0.7F, -0.2F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-78.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        this.renderMushroomBlock(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, model);
        poseStack.popPose();
    }

    private void renderMushroomBlock(PoseStack poseStack, MultiBufferSource buffer, int light, boolean outlineAndInvisible, BlockRenderDispatcher blockRenderDispatcher, int overlay, Optional<BlockState> mushroomState, BakedModel model) {
        BakedModel mushroomModel = mushroomState.map(blockRenderDispatcher::getBlockModel).orElse(model);

        if (outlineAndInvisible) {
            blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)), null, mushroomModel, 0.0f, 0.0f, 0.0f, light, overlay);
        } else {
            if (mushroomState.isPresent()) {
                blockRenderDispatcher.renderSingleBlock(mushroomState.get(), poseStack, buffer, light, overlay);
            } else {
                blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(Sheets.cutoutBlockSheet()), null, mushroomModel, 1.0F, 1.0F, 1.0F, light, overlay);
            }
        }
    }
}