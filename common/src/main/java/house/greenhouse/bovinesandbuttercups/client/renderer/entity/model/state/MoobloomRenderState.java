package house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state;

import house.greenhouse.bovinesandbuttercups.client.api.AbstractCowTypeRenderState;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.MoobloomModel;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;

import java.util.function.Function;

public class MoobloomRenderState extends AbstractCowTypeRenderState<Moobloom, MoobloomConfiguration, MoobloomModel> {
    public final AnimationState layDownAnimationState = new AnimationState();
    public final AnimationState getUpAnimationState = new AnimationState();

    public MoobloomRenderState(Function<ModelLayerLocation, MoobloomModel> bakeLayerFunction) {
        super(bakeLayerFunction);
    }

    @Override
    public void extractDefaultRenderStates(Moobloom moobloom) {
        cowType = moobloom.getCowType();
        super.extractDefaultRenderStates(moobloom);
        layDownAnimationState.copyFrom(moobloom.layDownAnimationState);
        getUpAnimationState.copyFrom(moobloom.getUpAnimationState);
    }
}
