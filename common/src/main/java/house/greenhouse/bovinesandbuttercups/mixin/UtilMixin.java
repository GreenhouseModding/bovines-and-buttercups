package house.greenhouse.bovinesandbuttercups.mixin;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import house.greenhouse.bovinesandbuttercups.util.dfu.BovinesDataFixer;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Util.class)
public class UtilMixin {
    @Inject(method = "doFetchChoiceType", at = @At("HEAD"), cancellable = true)
    private static void bovinesandbuttercups$fetchBovinesChoiceType(DSL.TypeReference type, String choiceName, CallbackInfoReturnable<Type<?>> cir) {
        if (choiceName.startsWith("bovinesandbuttercups:")) {
            try {
                Type<?> returnValue = BovinesDataFixer.get().fixer().getSchema(BovinesDataFixer.CURRENT_VERSION).getChoiceType(type, choiceName);
                if (returnValue != null)
                    cir.setReturnValue(returnValue);
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
