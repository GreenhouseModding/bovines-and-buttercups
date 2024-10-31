package house.greenhouse.bovinesandbuttercups.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireChargeItem.class)
public class FireChargeItemMixin {
    @ModifyExpressionValue(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CandleCakeBlock;canLight(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private boolean bovinesandbuttercups$allowLightingOfCandleCupcakes(boolean original, UseOnContext context) {
//        return original || CandleCupcakeBlock.canLight(context.getLevel().getBlockState(context.getClickedPos()));
        return original;
    }
}
