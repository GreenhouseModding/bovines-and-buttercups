package house.greenhouse.bovinesandbuttercups.client.renderer.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.MoobloomModel;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.MoobloomRenderState;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelLayers;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.MoobloomFlowerLayer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.CowLayersLayer;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.WardenRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.warden.Warden;

import java.util.function.Function;

public class MoobloomRenderer extends AgeableMobRenderer<Moobloom, MoobloomRenderState, MoobloomModel> {
    private final Function<ModelLayerLocation, ModelPart> bakeLayerFunction;

    public MoobloomRenderer(EntityRendererProvider.Context context) {
        super(context, new MoobloomModel(context.bakeLayer(BovinesModelLayers.MOOBLOOM_MODEL_LAYER)), new MoobloomModel(context.bakeLayer(BovinesModelLayers.BABY_MOOBLOOM_MODEL_LAYER)), 0.7F);
        this.addLayer(new CowLayersLayer(this));
        this.addLayer(new MoobloomFlowerLayer(this, context.getBlockRenderDispatcher()));
        bakeLayerFunction = context::bakeLayer;
    }

    @Override
    public ResourceLocation getTextureLocation(MoobloomRenderState entity) {
        return BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/moobloom/missing_moobloom.png");
    }

    @Override
    public MoobloomRenderState createRenderState() {
        return new MoobloomRenderState(modelLayerLocation -> new MoobloomModel(bakeLayerFunction.apply(modelLayerLocation)));
    }

    @Override
    public void extractRenderState(Moobloom moobloom, MoobloomRenderState state, float partialTicks) {
        super.extractRenderState(moobloom, state, partialTicks);
        state.extractDefaultRenderStates(moobloom);
        state.extractModel(this, moobloom);
        state.getUpAnimationState.copyFrom(moobloom.getUpAnimationState);
        state.layDownAnimationState.copyFrom(moobloom.layDownAnimationState);
    }
}
