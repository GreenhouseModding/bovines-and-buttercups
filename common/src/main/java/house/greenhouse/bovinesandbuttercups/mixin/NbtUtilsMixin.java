package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import house.greenhouse.bovinesandbuttercups.util.dfu.BovinesDataFixer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NbtUtils.class)
public class NbtUtilsMixin {
    @ModifyReturnValue(method = "addCurrentDataVersion", at = @At("RETURN"))
    private static CompoundTag bovinesandbuttercups$addBovinesDataVersion(CompoundTag original) {
        return BovinesDataFixer.setModDataVersion(original);
    }
}
