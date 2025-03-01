package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.access.EntityRendererLayerBakerAccess;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.api.variant.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Unique
    private final Map<CowModelType, M> bovinesandbuttercups$models = new HashMap<>();

    @Shadow protected M model;

    @ModifyVariable(method = "getRenderType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation bovinesandbuttercups$modifyTextureLocation(ResourceLocation value, T living) {
        CowVariantAttachment attachment = BovinesAndButtercups.getHelper().getCowVariantAttachment(living);
        if (attachment != null && attachment.cowVariant().isBound() && attachment.cowVariant().value().type().isApplicable(living))
            return BovinesAndButtercupsClient.getCachedTextures(attachment.cowVariant(), value);
        return value;
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void bovinesandbuttercups$modifyRenderLayerBakerAccessModel(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (this instanceof EntityRendererLayerBakerAccess<?> access) {
            var attachment = BovinesAndButtercups.getHelper().getCowVariantAttachment(entity);
            CowModelType cowModel = null;
            if (attachment != null)
                cowModel = attachment.cowVariant().value().configuration().settings().model();
            if (cowModel == null)
                cowModel = BovinesRegistries.COW_TYPE.stream().filter(cowType -> cowType.isApplicable(entity)).map(cowType -> cowType.defaultConfig(Minecraft.getInstance().level.registryAccess()).value().configuration().settings().model()).findFirst().orElse(BovinesCowModelTypes.TEMPERATE);
            ResourceLocation namedEntityTypeLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
            if (cowModel.namespaceOverride() != null)
                namedEntityTypeLocation = ResourceLocation.fromNamespaceAndPath(cowModel.namespaceOverride(), namedEntityTypeLocation.getPath());
            if (cowModel.pathOverride() != null)
                namedEntityTypeLocation = namedEntityTypeLocation.withPath(cowModel.pathOverride());
            if (!bovinesandbuttercups$models.containsKey(cowModel)) {
                M bakedModel = (M) access.bovinesandbuttercups$getLayerBakeFunction().apply(new ModelLayerLocation(namedEntityTypeLocation, "main"));
                bovinesandbuttercups$models.put(cowModel, bakedModel);
            }
            model = bovinesandbuttercups$models.get(cowModel);
        }
    }
}
