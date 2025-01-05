package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCowMushroomLayer.class)
public class MushroomCowMushroomLayerMixin<T extends MushroomCow> {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/MushroomCow;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void bovinesandbuttercups$cancelMushroomRenderIfNotDefault(PoseStack stack, MultiBufferSource bufferSource, int light, T entity, float f, float g, float h, float i, float j, float k, CallbackInfo ci) {
        Holder<CowVariant<MooshroomConfiguration>> cowVariant = CowVariantAttachment.getCowVariantHolderFromEntity(entity, BovinesCowTypes.MOOSHROOM_TYPE);
        if (cowVariant == null)
            return;
        if (
                cowVariant.value().configuration().vanillaType().isEmpty()
                        || cowVariant.value().configuration().mushroom().modelSet().isPresent()
                        || cowVariant.value().configuration().mushroom().customType().isPresent()
                        || cowVariant.value().configuration().mushroom().blockState().isEmpty() || !cowVariant.value().configuration().mushroom().blockState().get().equals(cowVariant.value().configuration().vanillaType().get().getBlockState())
        )
            ci.cancel();
    }
}
