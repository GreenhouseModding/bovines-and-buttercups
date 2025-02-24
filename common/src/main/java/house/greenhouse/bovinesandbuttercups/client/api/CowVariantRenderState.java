package house.greenhouse.bovinesandbuttercups.client.api;

import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;

public interface CowVariantRenderState<T extends LivingEntity, C extends BaseCowConfiguration, M extends EntityModel<?>> {
    Holder<CowVariant<C>> getCowVariant();

    void extractDefaultRenderStates(T t);

    void extractModel(LivingEntityRenderer<T, ?, M> renderer, T t);

    <E> E getRenderStateObject(RenderStateObject.Type<E> value);
}
