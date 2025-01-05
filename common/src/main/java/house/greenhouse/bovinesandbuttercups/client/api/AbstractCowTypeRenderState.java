package house.greenhouse.bovinesandbuttercups.client.api;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.mixin.client.AgeableMobRendererAccessor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractCowTypeRenderState<T extends LivingEntity, C extends CowTypeConfiguration, M extends EntityModel<?>> extends LivingEntityRenderState implements CowTypeRenderState<T, C, M> {
    public Holder<CowType<C>> cowType;
    private final Map<RenderStateObject.Type<Object>, Object> renderStateObject = new HashMap<>();
    private final Map<CowModelType, Pair<M, M>> models = new HashMap<>();
    private final Function<ModelLayerLocation, M> bakeLayerFunction;

    public AbstractCowTypeRenderState(Function<ModelLayerLocation, M> bakeLayerFunction) {
        this.bakeLayerFunction = bakeLayerFunction;
    }

    public void extractDefaultRenderStates(T entity) {
        RenderStateObject.setupGlobalObjects(renderStateObject, entity);
    }

    public void extractModel(LivingEntityRenderer<T, ?, M> renderer, T entity) {
        if (renderer instanceof AgeableMobRendererAccessor accessor) {
            CowTypeAttachment attachment = BovinesAndButtercups.getHelper().getCowTypeAttachment(entity);
            CowModelType cowModel = null;
            if (attachment != null)
                cowModel = attachment.cowType().value().configuration().model();
            if (cowModel == null)
                cowModel = BovinesCowModelTypes.DEFAULT;
            ResourceLocation namedEntityTypeLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
            if (cowModel.namespaceOverride() != null)
                namedEntityTypeLocation = ResourceLocation.fromNamespaceAndPath(cowModel.namespaceOverride(), namedEntityTypeLocation.getPath());
            if (cowModel.pathOverride() != null)
                namedEntityTypeLocation = namedEntityTypeLocation.withPath(cowModel.pathOverride());
            if (!models.containsKey(cowModel)) {
                M adultModel = bakeLayerFunction.apply(new ModelLayerLocation(namedEntityTypeLocation, "main"));
                M babyModel = bakeLayerFunction.apply(new ModelLayerLocation(namedEntityTypeLocation.withSuffix(BovinesCowModelTypes.DEFAULT.babySuffix()), "main"));
                models.put(cowModel, Pair.of(adultModel, babyModel));
            }
            accessor.bovinesandbuttercups$setAdultModel(models.get(cowModel).getFirst());
            accessor.bovinesandbuttercups$setBabyModel(models.get(cowModel).getSecond());
        }
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
