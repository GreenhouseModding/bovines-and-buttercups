package house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelLayers;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.FlowerCrownModel;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesAtlases;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FlowerCrownLayer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> extends RenderLayer<T, M> {
    private final TextureAtlas petalsTextureAtlas;
    private final FlowerCrownModel<T> model;

    public FlowerCrownLayer(RenderLayerParent<T, M> renderer, Function<ModelLayerLocation, ModelPart> baker, ModelManager modelManager) {
        super(renderer);
        this.model = new FlowerCrownModel<>(baker.apply(getModelLayerLocation()));
        petalsTextureAtlas = modelManager.getAtlas(BovinesAtlases.FLOWER_CROWN_SHEET);
    }

    private ModelLayerLocation getModelLayerLocation() {
        return getParentModel() instanceof PiglinModel ? BovinesModelLayers.PIGLIN_FLOWER_CROWN_MODEL_LAYER : BovinesModelLayers.FLOWER_CROWN_MODEL_LAYER;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = BovinesAndButtercupsClient.getHelper().getEquippedFlowerCrownForRendering(livingEntity);
        if (stack.isEmpty())
            return;
        FlowerCrown component = stack.get(BovinesDataComponents.FLOWER_CROWN);

        getParentModel().copyPropertiesTo(model);
        model.getHead().copyFrom(getParentModel().getHead());

        renderPart(model, poseStack, bufferSource, packedLight, 0, component);
        renderPart(model, poseStack, bufferSource, packedLight, 1, component);
        renderPart(model, poseStack, bufferSource, packedLight, 2, component);
        renderPart(model, poseStack, bufferSource, packedLight, 3, component);

        renderPart(model, poseStack, bufferSource, packedLight, 4, component);
        renderPart(model, poseStack, bufferSource, packedLight, 5, component);
        renderPart(model, poseStack, bufferSource, packedLight, 6, component);
        renderPart(model, poseStack, bufferSource, packedLight, 7, component);
    }

    private void renderPart(FlowerCrownModel<T> model, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int index, @Nullable FlowerCrown crown) {
        TextureAtlasSprite sprite = this.petalsTextureAtlas.getSprite(getSpriteTexture(index));
        if (crown != null)
            sprite = this.petalsTextureAtlas.getSprite(crown.getEquippedTexture(index));
        VertexConsumer consumer = sprite.wrap(buffer.getBuffer(RenderType.armorCutoutNoCull(BovinesAtlases.FLOWER_CROWN_SHEET)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    private static ResourceLocation getSpriteTexture(int index) {
        return switch (index) {
            case 0 -> BovinesAndButtercups.asResource("bovinesandbuttercups/flower_crown/models/center_left");
            case 1 -> BovinesAndButtercups.asResource("bovinesandbuttercups/flower_crown/models/top_left");
            case 2 -> BovinesAndButtercups.asResource("bovinesandbuttercups/flower_crown/models/top");
            case 3 -> BovinesAndButtercups.asResource("bovinesandbuttercups/flower_crown/models/top_right");
            case 4 -> BovinesAndButtercups.asResource("bovinesandbuttercups/flower_crown/models/center_right");
            case 5 -> BovinesAndButtercups.asResource("bovinesandbuttercups/flower_crown/models/bottom_right");
            case 6 -> BovinesAndButtercups.asResource("bovinesandbuttercups/flower_crown/models/bottom");
            case 7 -> BovinesAndButtercups.asResource("bovinesandbuttercups/flower_crown/models/bottom_left");
            default -> throw new UnsupportedOperationException("Could not get index outside of range 0-7.");
        };
    }
}