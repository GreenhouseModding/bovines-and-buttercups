package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
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
        Holder<CowType<MooshroomConfiguration>> cowType = CowTypeAttachment.getCowTypeHolderFromEntity(entity, BovinesCowTypeTypes.MOOSHROOM_TYPE);
        if (cowType == null)
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
