package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.util.dfu.BovinesDataFixer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NbtUtils.class)
public class NbtUtilsMixin {
    @Inject(method = "addCurrentDataVersion", at = @At("RETURN"))
    private static void bovinesandbuttercups$addBovinesDataVersion(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        BovinesDataFixer.setModDataVersion(tag);
    }
}
