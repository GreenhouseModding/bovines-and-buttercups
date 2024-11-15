package house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifier;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CowLayersLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public CowLayersLayer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        CowTypeAttachment attachment = BovinesAndButtercups.getHelper().getCowTypeAttachment(entity);
        if (entity.isInvisible() || attachment == null || !attachment.cowType().isBound() || attachment.cowType().value().configuration().layers().isEmpty())
            return;

        loop: for (CowModelLayer cowLayer : attachment.cowType().value().configuration().layers()) {
            ResourceLocation mappedTextureLocation = cowLayer.textureLocation().withPath(string -> "textures/entity/" + string + ".png");
            RenderType renderType = RenderType.entityTranslucent(mappedTextureLocation);
            int color = 0xFFFFFFFF;
            for (TextureModifierFactory<?> factory : cowLayer.textureModifiers()) {
                if (!factory.canDisplay(entity))
                    continue loop;
                TextureModifier provider = factory.getOrCreateProvider();
                color = provider.color(entity, color);
                renderType = provider.renderType(mappedTextureLocation, renderType);
            }

            this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(renderType), light, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), color);
        }
    }
}
