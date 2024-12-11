package house.greenhouse.bovinesandbuttercups.client.api;

import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;

public interface CowTypeRenderState<T extends Entity, C extends CowTypeConfiguration> {
    Holder<CowType<C>> getCowType();

    void extractDefaultRenderStates(T t);

    <E> E getRenderStateObject(RenderStateObject.Type<E> value);
}
