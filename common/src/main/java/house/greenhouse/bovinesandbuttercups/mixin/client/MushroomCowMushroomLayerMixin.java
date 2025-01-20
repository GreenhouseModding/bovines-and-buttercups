package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCowMushroomLayer.class)
public class MushroomCowMushroomLayerMixin {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/MushroomCowRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    private void bovinesandbuttercups$cancelMushroomRenderIfNotDefault(PoseStack stack, MultiBufferSource bufferSource, int light, MushroomCowRenderState state, float f, float g, CallbackInfo ci) {
        Holder<CowVariant<MooshroomConfiguration>> cowVariant = ((CowVariantRenderState)state).getCowVariant();
        if (cowVariant == null || !cowVariant.isBound())
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
