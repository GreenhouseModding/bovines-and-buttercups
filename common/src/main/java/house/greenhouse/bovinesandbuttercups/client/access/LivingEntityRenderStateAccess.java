package house.greenhouse.bovinesandbuttercups.client.access;

import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.CowRenderStateExtension;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.animal.Cow;
import org.jetbrains.annotations.Nullable;

public interface LivingEntityRenderStateAccess {
    @Nullable
    <T extends Cow, C extends BaseCowConfiguration, M extends EntityModel<?>> CowRenderStateExtension<T, C, M> getRenderStateExtension();
    <T extends Cow, C extends BaseCowConfiguration, M extends EntityModel<?>> void setRenderStateExtension(CowRenderStateExtension<T, C, M> extension);
}
