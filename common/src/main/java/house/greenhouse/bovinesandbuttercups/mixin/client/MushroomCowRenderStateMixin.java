package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.access.MushroomCowRenderStateLayerBakerAccess;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import house.greenhouse.bovinesandbuttercups.client.api.RenderStateObject;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mixin(MushroomCowRenderState.class)
public class MushroomCowRenderStateMixin implements CowVariantRenderState<MushroomCow, MooshroomConfiguration, CowModel>, MushroomCowRenderStateLayerBakerAccess {
    @Unique
    public Holder<CowVariant<MooshroomConfiguration>> bovinesandbuttercups$cowVariant;
    @Unique
    private final Map<RenderStateObject.Type<Object>, Object> bovinesandbuttercups$renderStateObject = new HashMap<>();
    @Unique
    private final Map<CowModelType, Pair<CowModel, CowModel>> bovinesandbuttercups$models = new HashMap<>();
    @Unique
    private Function<ModelLayerLocation, CowModel> bovinesandbuttercups$bakeLayerFunction;

    @Override
    public Holder<CowVariant<MooshroomConfiguration>> getCowVariant() {
        return bovinesandbuttercups$cowVariant;
    }

    @Override
    public void extractDefaultRenderStates(MushroomCow mushroomCow) {
        bovinesandbuttercups$cowVariant = CowVariantAttachment.getCowVariantHolderFromEntity(mushroomCow, BovinesCowTypes.MOOSHROOM_TYPE);
        RenderStateObject.setupGlobalObjects(bovinesandbuttercups$renderStateObject, mushroomCow);
    }

    @Override
    public void extractModel(LivingEntityRenderer<MushroomCow, ?, CowModel> renderer, MushroomCow entity) {
        bovinesandbuttercups$cowVariant = CowVariantAttachment.getCowVariantHolderFromEntity(entity, BovinesCowTypes.MOOSHROOM_TYPE);
        if (renderer instanceof AgeableMobRendererAccessor accessor && bovinesandbuttercups$cowVariant != null) {
            CowModelType cowModel = bovinesandbuttercups$cowVariant.value().configuration().model();
            if (!bovinesandbuttercups$models.containsKey(bovinesandbuttercups$cowVariant.value().configuration().model())) {
                ResourceLocation namedEntityTypeLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                if (cowModel != null && cowModel.namespaceOverride() != null)
                    namedEntityTypeLocation = ResourceLocation.fromNamespaceAndPath(cowModel.namespaceOverride(), namedEntityTypeLocation.getPath());
                if (cowModel != null && cowModel.pathOverride() != null)
                    namedEntityTypeLocation = namedEntityTypeLocation.withPath(cowModel.pathOverride());
                String babySuffix = cowModel != null ? cowModel.babySuffix() : BovinesCowModelTypes.DEFAULT.babySuffix();
                CowModel adultModel = bovinesandbuttercups$bakeLayerFunction.apply(new ModelLayerLocation(namedEntityTypeLocation, "main"));
                CowModel babyModel = bovinesandbuttercups$bakeLayerFunction.apply(new ModelLayerLocation(namedEntityTypeLocation.withSuffix(babySuffix), "main"));
                bovinesandbuttercups$models.put(cowModel, Pair.of(adultModel, babyModel));
            }
            accessor.bovinesandbuttercups$setAdultModel(bovinesandbuttercups$models.get(cowModel).getFirst());
            accessor.bovinesandbuttercups$setBabyModel(bovinesandbuttercups$models.get(cowModel).getSecond());
        }
    }

    @Override
    public <E> E getRenderStateObject(RenderStateObject.Type<E> value) {
        return (E) bovinesandbuttercups$renderStateObject.get(value);
    }

    @Override
    public void bovinesandbuttercups$setLayerBakeFunction(Function<ModelLayerLocation, CowModel> bakeFunction) {
        bovinesandbuttercups$bakeLayerFunction = bakeFunction;
    }
}
