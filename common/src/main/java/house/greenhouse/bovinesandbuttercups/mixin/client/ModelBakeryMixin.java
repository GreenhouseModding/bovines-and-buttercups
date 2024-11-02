package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
    @Shadow @Final public static ResourceLocation MISSING_MODEL_LOCATION;

    @Inject(method = "loadBlockModel", at = @At("HEAD"))
    private void barricade$captureModelBakery(ResourceLocation location, CallbackInfoReturnable<BlockModel> cir) {
        if (location.equals(MISSING_MODEL_LOCATION))
            BovinesAndButtercupsClient.setModelBakery((ModelBakery)(Object)this);
    }
}