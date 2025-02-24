package house.greenhouse.bovinesandbuttercups.mixin;

import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.ConversionType;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ConversionType.class)
public interface ConversionTypeInvoker {
    @Invoker("convert")
    void bovinesandbuttercups$invokeConvert(Mob oldMob, Mob newMob, ConversionParams params);
}
