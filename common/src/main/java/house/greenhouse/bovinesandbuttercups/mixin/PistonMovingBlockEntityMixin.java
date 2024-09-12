package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlocks;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonMovingBlockEntity.class)
public class PistonMovingBlockEntityMixin {
    @Shadow private BlockState movedState;

    @ModifyReturnValue(method = "isStickyForEntities", at = @At("RETURN"))
    private boolean bovinesandbuttercups$makeRichHoneyStickyForEntitiesOnPistons(boolean original) {
        return original || movedState.is(BovinesBlocks.RICH_HONEY_BLOCK);
    }
}
