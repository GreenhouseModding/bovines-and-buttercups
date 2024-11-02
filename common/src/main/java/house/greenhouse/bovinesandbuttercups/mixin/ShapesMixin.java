package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.access.VoxelShapeFunctionAccess;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Shapes.class)
public class ShapesMixin {
    @ModifyReturnValue(method = "join", at = @At("RETURN"))
    private static VoxelShape bovinesandbuttercups$joinShapes(VoxelShape original, @Local(argsOnly = true) BooleanOp function) {
        ((VoxelShapeFunctionAccess)original).bovinesandbuttercups$setJoinFunction(function);
        return original;
    }
}
