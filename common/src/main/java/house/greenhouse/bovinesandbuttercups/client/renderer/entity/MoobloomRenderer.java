package house.greenhouse.bovinesandbuttercups.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.variant.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.MoobloomModel;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelLayers;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.MoobloomFlowerLayer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.CowLayersLayer;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MoobloomRenderer extends MobRenderer<Moobloom, MoobloomModel> {
    private final Function<ModelLayerLocation, MoobloomModel> bakeLayerFunction;
    private final Map<CowModelType, MoobloomModel> models = new HashMap<>();

    public MoobloomRenderer(EntityRendererProvider.Context context) {
        super(context, new MoobloomModel(context.bakeLayer(BovinesModelLayers.MOOBLOOM_MODEL_LAYER)), 0.7F);
        this.addLayer(new CowLayersLayer<>(this));
        this.addLayer(new MoobloomFlowerLayer(this, context.getBlockRenderDispatcher()));
        bakeLayerFunction = modelLayerLocation -> new MoobloomModel(context.bakeLayer(modelLayerLocation));
    }

    @Override
    public void render(Moobloom moobloom, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        ResourceLocation namedEntityTypeLocation = BuiltInRegistries.ENTITY_TYPE.getKey(moobloom.getType());
        CowModelType cowModel = moobloom.getCowVariant().value().configuration().model();
        if (cowModel != null && cowModel.namespaceOverride() != null)
            namedEntityTypeLocation = ResourceLocation.fromNamespaceAndPath(cowModel.namespaceOverride(), namedEntityTypeLocation.getPath());
        if (cowModel != null && cowModel.pathOverride() != null)
            namedEntityTypeLocation = namedEntityTypeLocation.withPath(cowModel.pathOverride());
        if (!models.containsKey(cowModel)) {
            MoobloomModel model = bakeLayerFunction.apply(new ModelLayerLocation(namedEntityTypeLocation, "main"));
            models.put(cowModel, model);
        }
        model = models.get(cowModel);
        super.render(moobloom, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }


    @Override
    public ResourceLocation getTextureLocation(Moobloom entity) {
        return BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/moobloom/missing_moobloom.png");
    }
}
