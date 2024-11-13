package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.access.MobEffectInstanceLockdownDataAccess;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.util.LockdownData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Consumer;

@Mixin(PotionContents.class)
public class PotionContentsMixin {
    @WrapWithCondition(method = "addPotionTooltip(Ljava/lang/Iterable;Ljava/util/function/Consumer;FF)V", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0))
    private static <T> boolean bovinesandbuttercups$updatePotionTooltip(Consumer<Component> instance, T t, @Local(argsOnly = true, ordinal = 0) float durationFactor, @Local(argsOnly = true, ordinal = 1) float ticksPerSecond, @Local MobEffectInstance effectInstance) {
        if (effectInstance.is(BovinesEffects.LOCKDOWN)) {
            List<LockdownData> list =  ((MobEffectInstanceLockdownDataAccess)effectInstance).bovinesandbuttercups$getLockdownData();
            for (LockdownData data : list) {
                MobEffectInstance dataInstance = new MobEffectInstance(data.linkedEffect(), data.duration().orElse(effectInstance.getDuration()));
                if (!dataInstance.endsWithin(20))
                    instance.accept(Component.translatable("potion.bovinesandbuttercups.lockdown", Component.translatable(dataInstance.getDescriptionId()), MobEffectUtil.formatDuration(dataInstance, durationFactor, ticksPerSecond)).withStyle(effectInstance.getEffect().value().getCategory().getTooltipFormatting()));
            }
            return false;
        }
        return true;
    }
}
