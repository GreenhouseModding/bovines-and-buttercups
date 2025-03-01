package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.client.access.LivingEntityRenderStateAccess;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.CowRenderStateExtension;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateAccess {
    CowRenderStateExtension<?, ?, ?> stateExtension;

    @Override
    public <T extends Cow, C extends BaseCowConfiguration, M extends EntityModel<?>> CowRenderStateExtension<T, C, M> getRenderStateExtension() {
        return (CowRenderStateExtension) stateExtension;
    }

    @Override
    public <T extends Cow, C extends BaseCowConfiguration, M extends EntityModel<?>> void setRenderStateExtension(CowRenderStateExtension<T, C, M> extension) {
        stateExtension = extension;
    }
}
