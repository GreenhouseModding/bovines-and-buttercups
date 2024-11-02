package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.access.VoxelShapeFunctionAccess;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VoxelShape.class)
public class VoxelShapeMixin implements VoxelShapeFunctionAccess {
    @Unique
    private BooleanOp bovinesandbuttercups$function;


    @Override
    public BooleanOp bovinesandbuttercups$getJoinFunction() {
        return bovinesandbuttercups$function;
    }

    @Override
    public void bovinesandbuttercups$setJoinFunction(BooleanOp function) {
        bovinesandbuttercups$function = function;
    }
}
