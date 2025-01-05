package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import house.greenhouse.bovinesandbuttercups.access.EntityRendererLayerBakerAccess;
import house.greenhouse.bovinesandbuttercups.access.MushroomCowRenderStateLayerBakerAccess;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin extends MobRenderer<MushroomCow, MushroomCowRenderState, CowModel> {

    public MushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/MushroomCow;Lnet/minecraft/client/renderer/entity/state/MushroomCowRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;getVariant()Lnet/minecraft/world/entity/animal/MushroomCow$Variant;"))
    private void bovinesandbuttercups$extractMooshroomRenderState(MushroomCow entity, MushroomCowRenderState state, float partialTick, CallbackInfo ci) {
        ((CowTypeRenderState)state).extractModel((MushroomCowRenderer)(Object)this, entity);
        ((CowTypeRenderState)state).extractDefaultRenderStates(entity);
    }

    @ModifyReturnValue(method = "createRenderState()Lnet/minecraft/client/renderer/entity/state/EntityRenderState;", at = @At("RETURN"))
    private EntityRenderState bovinesandbuttercups$addBakeContextToRenderState(EntityRenderState original) {
        if (this instanceof EntityRendererLayerBakerAccess bakerAccess && original instanceof MushroomCowRenderStateLayerBakerAccess access)
            access.bovinesandbuttercups$setLayerBakeFunction(bakerAccess.bovinesandbuttercups$getMooshroomLayerBakeFunction());
        return original;
    }
}