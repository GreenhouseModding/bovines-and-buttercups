package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {
    @Inject(method = "addOccupant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee;stopRiding()V"))
    private void bovinesandbuttercups$setToProduceRichHoney(Bee bee, CallbackInfo ci) {
        if (BovinesAndButtercups.getHelper().producesRichHoney(bee)) {
            BovinesAndButtercups.getHelper().setProducesRichHoney(bee, false);
            BovinesAndButtercups.getHelper().setProducesRichHoney((BeehiveBlockEntity)(Object)this, true);
        }
    }
}
