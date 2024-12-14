package house.greenhouse.bovinesandbuttercups.client.api;

import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface CowTypeRenderState<T extends LivingEntity, C extends CowTypeConfiguration, M extends EntityModel<?>> {
    Holder<CowType<C>> getCowType();

    void extractDefaultRenderStates(T t);

    void extractModel(LivingEntityRenderer<T, ?, M> renderer, T t);

    <E> E getRenderStateObject(RenderStateObject.Type<E> value);
}
