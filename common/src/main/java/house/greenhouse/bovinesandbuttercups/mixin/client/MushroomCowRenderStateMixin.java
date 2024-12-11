package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
import house.greenhouse.bovinesandbuttercups.client.api.RenderStateObject;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(MushroomCowRenderState.class)
public class MushroomCowRenderStateMixin implements CowTypeRenderState<MushroomCow, MooshroomConfiguration> {
    @Unique
    public Holder<CowType<MooshroomConfiguration>> bovinesandbuttercups$cowType;
    @Unique
    private final Map<RenderStateObject.Type<Object>, Object> bovinesandbuttercups$renderStateObject = new HashMap<>();

    @Override
    public Holder<CowType<MooshroomConfiguration>> getCowType() {
        return bovinesandbuttercups$cowType;
    }

    @Override
    public void extractDefaultRenderStates(MushroomCow mushroomCow) {
        bovinesandbuttercups$cowType = CowTypeAttachment.getCowTypeHolderFromEntity(mushroomCow, BovinesCowTypeTypes.MOOSHROOM_TYPE);
        RenderStateObject.setupGlobalObjects(bovinesandbuttercups$renderStateObject, mushroomCow);
    }

    @Override
    public <E> E getRenderStateObject(RenderStateObject.Type<E> value) {
        return (E) bovinesandbuttercups$renderStateObject.get(value);
    }
}
