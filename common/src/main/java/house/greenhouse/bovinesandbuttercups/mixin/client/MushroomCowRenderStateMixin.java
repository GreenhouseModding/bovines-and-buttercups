package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.access.MushroomCowRenderStateLayerBakerAccess;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
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
import java.util.Optional;
import java.util.function.Function;

@Mixin(MushroomCowRenderState.class)
public class MushroomCowRenderStateMixin implements CowTypeRenderState<MushroomCow, MooshroomConfiguration, CowModel>, MushroomCowRenderStateLayerBakerAccess {
    @Unique
    public Holder<CowType<MooshroomConfiguration>> bovinesandbuttercups$cowType;
    @Unique
    private final Map<RenderStateObject.Type<Object>, Object> bovinesandbuttercups$renderStateObject = new HashMap<>();
    @Unique
    private final Map<CowModelType, Pair<CowModel, CowModel>> bovinesandbuttercups$models = new HashMap<>();
    @Unique
    private Function<ModelLayerLocation, CowModel> bovinesandbuttercups$bakeLayerFunction;

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
    public void extractModel(LivingEntityRenderer<MushroomCow, ?, CowModel> renderer, MushroomCow entity) {
        bovinesandbuttercups$cowType = CowTypeAttachment.getCowTypeHolderFromEntity(entity, BovinesCowTypeTypes.MOOSHROOM_TYPE);
        if (renderer instanceof AgeableMobRendererAccessor accessor && bovinesandbuttercups$cowType != null) {
            CowModelType cowModel = bovinesandbuttercups$cowType.value().configuration().model();
            if (!bovinesandbuttercups$models.containsKey(bovinesandbuttercups$cowType.value().configuration().model())) {
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
