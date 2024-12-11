package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
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
        Holder<CowType<MooshroomConfiguration>> cowType = ((CowTypeRenderState)state).getCowType();
        if (cowType == null || !cowType.isBound())
            return;
        if (
                cowType.value().configuration().vanillaType().isEmpty()
                        || cowType.value().configuration().mushroom().modelSet().isPresent()
                        || cowType.value().configuration().mushroom().customType().isPresent()
                        || cowType.value().configuration().mushroom().blockState().isEmpty() || !cowType.value().configuration().mushroom().blockState().get().equals(cowType.value().configuration().vanillaType().get().getBlockState())
        )
            ci.cancel();
    }
}
