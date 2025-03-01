package house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.api.variant.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import house.greenhouse.bovinesandbuttercups.client.api.RenderStateObject;
import house.greenhouse.bovinesandbuttercups.mixin.client.AgeableMobRendererAccessor;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CowRenderStateExtension<T extends Cow, C extends BaseCowConfiguration, M extends EntityModel<?>> implements CowVariantRenderState<T, C, M> {
    public Holder<CowVariant<C>> cowVariant;
    private final Map<RenderStateObject.Type<Object>, Object> renderStateObject = new HashMap<>();
    private final Map<CowModelType, Pair<M, M>> models = new HashMap<>();
    private final Function<ModelLayerLocation, M> bakeLayerFunction;

    public CowRenderStateExtension(Function<ModelLayerLocation, M> bakeLayerFunction) {
        this.bakeLayerFunction = bakeLayerFunction;
    }

    public void setCowVariant(T entity, CowType<C> cowType) {
        cowVariant = CowVariantAttachment.getCowVariantHolderFromEntity(entity, cowType);
    }

    public void extractDefaultRenderStates(T entity) {
        RenderStateObject.setupGlobalObjects(renderStateObject, entity);
    }

    @Override
    public Holder<CowVariant<C>> getCowVariant() {
        return cowVariant;
    }

    @Override
    public void extractModel(LivingEntityRenderer<T, ?, M> renderer, T entity) {
        if (renderer instanceof AgeableMobRendererAccessor accessor) {
            CowModelType cowModel;
            if (cowVariant == null)
                cowModel = BovinesRegistries.COW_TYPE.stream().filter(cowType -> cowType.isApplicable(entity)).map(cowType -> cowType.defaultConfig(Minecraft.getInstance().level.registryAccess()).value().configuration().settings().model()).findFirst().orElse(BovinesCowModelTypes.TEMPERATE);
            else
                cowModel = cowVariant.value().configuration().settings().model();
            ResourceLocation namedEntityTypeLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
            if (cowModel.namespaceOverride() != null)
                namedEntityTypeLocation = ResourceLocation.fromNamespaceAndPath(cowModel.namespaceOverride(), namedEntityTypeLocation.getPath());
            if (cowModel.pathOverride() != null)
                namedEntityTypeLocation = namedEntityTypeLocation.withPath(cowModel.pathOverride());
            if (!models.containsKey(cowModel)) {
                M adultModel = bakeLayerFunction.apply(new ModelLayerLocation(namedEntityTypeLocation, "main"));
                M babyModel = bakeLayerFunction.apply(new ModelLayerLocation(namedEntityTypeLocation.withSuffix(BovinesCowModelTypes.TEMPERATE.babySuffix()), "main"));
                models.put(cowModel, Pair.of(adultModel, babyModel));
            }
            accessor.bovinesandbuttercups$setAdultModel(models.get(cowModel).getFirst());
            accessor.bovinesandbuttercups$setBabyModel(models.get(cowModel).getSecond());
        }
    }

    @Override
    public <E> E getRenderStateObject(RenderStateObject.Type<E> value) {
        return (E) renderStateObject.get(value);
    }
}
