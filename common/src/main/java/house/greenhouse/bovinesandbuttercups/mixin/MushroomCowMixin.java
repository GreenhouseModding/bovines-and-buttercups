package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.access.MooshroomInitializedTypeAccess;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.util.MooshroomChildTypeUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = MushroomCow.class)
public abstract class MushroomCowMixin implements MooshroomInitializedTypeAccess {
    @Shadow public abstract MushroomCow.MushroomType getVariant();

    @Nullable
    @Unique
    private MushroomCow.MushroomType bovineandbuttercups$initializedType;

    @Inject(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;I)Z"))
    private void bovinesandbuttercups$setInitializedType(CompoundTag compound, CallbackInfo ci) {
         if (compound.contains("Type"))
             bovineandbuttercups$initializedType = getVariant();
    }

    @Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/MushroomCow;", at = @At(value = "RETURN"))
    private void bovinesandbuttercups$setDataDrivenMooshroomOffspringType(ServerLevel serverLevel, AgeableMob ageableMob, CallbackInfoReturnable<MushroomCow> cir, @Local(ordinal = 1) MushroomCow baby) {
        var pair = MooshroomChildTypeUtil.chooseMooshroomBabyType((MushroomCow)(Object)this, (MushroomCow)ageableMob, baby, ((Animal)(Object)this).getLoveCause());
        if (pair == null)
            return;
        CowTypeAttachment.setCowType(baby, pair.getFirst(), pair.getSecond());
    }

    @ModifyExpressionValue(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;getVariant()Lnet/minecraft/world/entity/animal/MushroomCow$MushroomType;"))
    private MushroomCow.MushroomType bovinesandbuttercups$allowMooshroomToEatFlowers(MushroomCow.MushroomType original) {
        @Nullable CowType<MooshroomConfiguration> cowType = CowTypeAttachment.getCowTypeFromEntity((MushroomCow)(Object)this, BovinesCowTypeTypes.MOOSHROOM_TYPE);
        if (cowType != null) {
            if (cowType.configuration().canEatFlowers().isPresent() && cowType.configuration().canEatFlowers().get() && original == MushroomCow.MushroomType.RED)
                return MushroomCow.MushroomType.BROWN;
            else if (cowType.configuration().canEatFlowers().isPresent() && !cowType.configuration().canEatFlowers().get() && original == MushroomCow.MushroomType.BROWN)
                return MushroomCow.MushroomType.RED;
        }
        return original;
    }

    @Override
    public MushroomCow.MushroomType bovinesandbuttercups$initialType() {
        return bovineandbuttercups$initializedType;
    }

    @Override
    public void bovinesandbuttercups$clearInitialType() {
        bovineandbuttercups$initializedType = null;
    }
}
