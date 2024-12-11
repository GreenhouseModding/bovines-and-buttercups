package house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state;

import house.greenhouse.bovinesandbuttercups.client.api.AbstractCowTypeRenderState;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.world.entity.AnimationState;

public class MoobloomRenderState extends AbstractCowTypeRenderState<Moobloom, MoobloomConfiguration> {
    public final AnimationState layDownAnimationState = new AnimationState();
    public final AnimationState getUpAnimationState = new AnimationState();

    @Override
    public void extractDefaultRenderStates(Moobloom moobloom) {
        cowType = moobloom.getCowType();
        super.extractDefaultRenderStates(moobloom);
        layDownAnimationState.copyFrom(moobloom.layDownAnimationState);
        getUpAnimationState.copyFrom(moobloom.getUpAnimationState);
    }
}
