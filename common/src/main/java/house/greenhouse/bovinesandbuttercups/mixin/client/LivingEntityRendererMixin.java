package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.access.EntityRendererLayerBakerAccess;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
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
    private final Map<CowModelType, CowModel<MushroomCow>> bovinesandbuttercups$models = new HashMap<>();

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
        var attachment = BovinesAndButtercups.getHelper().getCowVariantAttachment(entity);
        if (this instanceof EntityRendererLayerBakerAccess access && attachment != null && attachment.cowVariant().isBound() && attachment.cowVariant().value().type().isApplicable(entity)) {
            CowModelType cowModel = attachment.cowVariant().value().configuration().model();
            if (!bovinesandbuttercups$models.containsKey(cowModel)) {
                ResourceLocation namedEntityTypeLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                if (cowModel != null && cowModel.namespaceOverride() != null)
                    namedEntityTypeLocation = ResourceLocation.fromNamespaceAndPath(cowModel.namespaceOverride(), namedEntityTypeLocation.getPath());
                if (cowModel != null && cowModel.pathOverride() != null)
                    namedEntityTypeLocation = namedEntityTypeLocation.withPath(cowModel.pathOverride());
                CowModel<MushroomCow> model = access.bovinesandbuttercups$getMooshroomLayerBakeFunction().apply(new ModelLayerLocation(namedEntityTypeLocation, "main"));
                bovinesandbuttercups$models.put(cowModel, model);
            }
            model = (M) bovinesandbuttercups$models.get(cowModel);
        }
    }
}
