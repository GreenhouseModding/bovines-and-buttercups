package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.access.MobEffectInstanceLockdownDataAccess;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.util.LockdownData;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements MobEffectInstanceLockdownDataAccess {
    @Shadow
    public abstract Holder<MobEffect> getEffect();

    @Shadow @Final private Holder<MobEffect> effect;

    @Unique
    private List<LockdownData> bovinesandbuttercups$lockdownData;

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"))
    private static Codec<MobEffectInstance> bovinesandbuttercups$addLockdownDataToInstanceCodec(Codec<MobEffectInstance> original) {
        return Codec.pair(original, LockdownData.ADDITIONAL_CODEC).xmap(pair -> {
            MobEffectInstance instance = pair.getFirst();
            ((MobEffectInstanceLockdownDataAccess)instance).bovinesandbuttercups$setLockdownData(pair.getSecond());
            return instance;
        }, instance -> {
            List<LockdownData> data = ((MobEffectInstanceLockdownDataAccess) instance).bovinesandbuttercups$getLockdownData();
            return Pair.of(instance, data);
        });
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/StreamCodec;composite(Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Lnet/minecraft/network/codec/StreamCodec;Ljava/util/function/Function;Ljava/util/function/BiFunction;)Lnet/minecraft/network/codec/StreamCodec;"))
    private static StreamCodec<RegistryFriendlyByteBuf, MobEffectInstance> bovinesandbuttercups$addLockdownDataToStreamCodec(StreamCodec<RegistryFriendlyByteBuf, MobEffectInstance> original) {
        return StreamCodec.composite(
                original,
                instance -> instance,
                LockdownData.STREAM_CODEC.apply(ByteBufCodecs.list()),
                instance -> ((MobEffectInstanceLockdownDataAccess)instance).bovinesandbuttercups$getLockdownData(),
                (mobEffectInstance, lockdownData) -> {
                    ((MobEffectInstanceLockdownDataAccess)mobEffectInstance).bovinesandbuttercups$setLockdownData(lockdownData);
                    return mobEffectInstance;
                });
}

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"), cancellable = true)
    private void bovinesandbuttercups$lockDuration(LivingEntity living, Runnable runnable, CallbackInfoReturnable<Boolean> cir) {
        if (living.hasEffect(BovinesEffects.LOCKDOWN) && BovinesAndButtercups.getHelper().getLockdownAttachment(living).effects().containsKey(getEffect()))
            cir.setReturnValue(true);
    }

    @Inject(method = "setDetailsFrom", at = @At("TAIL"))
    private void bovinesandbuttercups$setLockdownDataFromOtherEffect(MobEffectInstance effectInstance, CallbackInfo ci) {
        if (effect.is(BovinesEffects.LOCKDOWN) && effectInstance.is(BovinesEffects.LOCKDOWN))
            bovinesandbuttercups$setLockdownData(((MobEffectInstanceLockdownDataAccess)effectInstance).bovinesandbuttercups$getLockdownData());
    }

    public List<LockdownData> bovinesandbuttercups$getLockdownData() {
        if (!effect.is(BovinesEffects.LOCKDOWN) || bovinesandbuttercups$lockdownData == null)
            return List.of();
        return bovinesandbuttercups$lockdownData;
    }

    public MobEffectInstance bovinesandbuttercups$setLockdownData(List<LockdownData> data) {
        if (effect.is(BovinesEffects.LOCKDOWN))
            bovinesandbuttercups$lockdownData = data;
        return (MobEffectInstance)(Object)this;
    }
}
