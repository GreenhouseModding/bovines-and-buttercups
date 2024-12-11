package house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifier;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CowLayersLayer<C extends CowTypeConfiguration, T extends LivingEntityRenderState & CowTypeRenderState<Entity, C>, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public CowLayersLayer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T renderState, float yRot, float xRot) {
        Holder<CowType<C>> attachment = renderState.getCowType();
        if (renderState.isInvisible || attachment == null || !renderState.getCowType().isBound() || renderState.getCowType().value().configuration().layers().isEmpty())
            return;

        loop: for (CowModelLayer cowLayer : renderState.getCowType().value().configuration().layers()) {
            ResourceLocation mappedTextureLocation = cowLayer.textureLocation().withPath(string -> "textures/entity/" + string + ".png");
            RenderType renderType = RenderType.entityTranslucent(mappedTextureLocation);
            int color = 0xFFFFFFFF;
            for (TextureModifierFactory<?> factory : cowLayer.textureModifiers()) {
                TextureModifier provider = factory.getOrCreateProvider();
                if (!provider.canDisplay(renderState))
                    continue loop;
                color = provider.color(renderState, color);
                renderType = provider.renderType(mappedTextureLocation, renderType);
            }

            this.getParentModel().renderToBuffer(poseStack, bufferSource.getBuffer(renderType), packedLight, LivingEntityRenderer.getOverlayCoords(renderState, 0.0F), color);
        }
    }
}
