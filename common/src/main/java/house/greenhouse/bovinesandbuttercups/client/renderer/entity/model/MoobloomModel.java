package house.greenhouse.bovinesandbuttercups.client.renderer.entity.model;

import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.animation.MoobloomAnimations;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.MoobloomRenderState;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;

public class MoobloomModel extends QuadrupedModel<MoobloomRenderState> {
    public MoobloomModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(MoobloomRenderState state) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (animateLayingDown(state)) {
            if (state.layDownAnimationState.isStarted()) {
                head.xRot = state.xRot * (float) (Math.PI / 180.0);
                head.yRot = state.yRot * (float) (Math.PI / 180.0);
            }
            return;
        }
        super.setupAnim(state);
    }

    protected boolean animateLayingDown(MoobloomRenderState state) {
        if (state.getUpAnimationState.isStarted() || state.layDownAnimationState.isStarted()) {
            animate(state.getUpAnimationState, MoobloomAnimations.MOOBLOOM_GET_UP, state.ageInTicks, 1.0F);
            animate(state.layDownAnimationState, MoobloomAnimations.MOOBLOOM_LAY_DOWN, state.ageInTicks, 1.0F);
            return true;
        }
        return false;
    }

    public ModelPart getHead() {
        return head;
    }

    public ModelPart getBody() {
        return body;
    }
}