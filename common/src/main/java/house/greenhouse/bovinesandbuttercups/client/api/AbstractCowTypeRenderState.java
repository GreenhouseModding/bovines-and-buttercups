package house.greenhouse.bovinesandbuttercups.client.api;

import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCowTypeRenderState<T extends Entity, C extends CowTypeConfiguration> extends LivingEntityRenderState implements CowTypeRenderState<T, C> {
    public Holder<CowType<C>> cowType;
    private final Map<RenderStateObject.Type<Object>, Object> renderStateObject = new HashMap<>();

    public void extractDefaultRenderStates(T t) {
        RenderStateObject.setupGlobalObjects(renderStateObject, t);
    }

    @Override
    public Holder<CowType<C>> getCowType() {
        return cowType;
    }

    @Override
    public <E> E getRenderStateObject(RenderStateObject.Type<E> value) {
        return (E) renderStateObject.get(value);
    }
}
