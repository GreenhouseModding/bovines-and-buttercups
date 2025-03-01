package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import house.greenhouse.bovinesandbuttercups.access.EntityRendererLayerBakerAccess;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.client.access.LivingEntityRenderStateAccess;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.CowRenderStateExtension;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.CowConfiguration;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CowRenderer.class)
public class CowRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/Cow;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void bovinesandbuttercups$extractMooshroomRenderState(Cow cow, LivingEntityRenderState state, float p_360614_, CallbackInfo ci) {
        CowRenderStateExtension<Cow, CowConfiguration, CowModel> extension = ((LivingEntityRenderStateAccess)state).getRenderStateExtension();
        if (extension != null) {
            extension.setCowVariant(cow, BovinesCowTypes.COW_TYPE);
            extension.extractDefaultRenderStates(cow);
            extension.extractModel((CowRenderer) (Object) this, cow);
        }
    }

    @ModifyReturnValue(method = "createRenderState()Lnet/minecraft/client/renderer/entity/state/EntityRenderState;", at = @At("RETURN"))
    private EntityRenderState bovinesandbuttercups$addBakeContextToRenderState(EntityRenderState original) {
        if (this instanceof EntityRendererLayerBakerAccess bakerAccess && original instanceof LivingEntityRenderStateAccess access)
            access.setRenderStateExtension(new CowRenderStateExtension<>(bakerAccess.bovinesandbuttercups$getMooshroomLayerBakeFunction()));
        return original;
    }
}
