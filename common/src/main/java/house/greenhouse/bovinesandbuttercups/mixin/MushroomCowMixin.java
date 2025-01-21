package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.access.MooshroomInitializedTypeAccess;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.item.CustomFlowerItem;
import house.greenhouse.bovinesandbuttercups.util.MooshroomChildTypeUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin implements MooshroomInitializedTypeAccess {
    @Shadow public abstract MushroomCow.Variant getVariant();

    @Nullable
    @Unique
    private MushroomCow.Variant bovineandbuttercups$initializedType;

    @Inject(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;I)Z"))
    private void bovinesandbuttercups$setInitializedType(CompoundTag compound, CallbackInfo ci) {
         if (compound.contains("Type"))
             bovineandbuttercups$initializedType = getVariant();
    }

    @Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/MushroomCow;", at = @At(value = "RETURN"))
    private void bovinesandbuttercups$setDataDrivenMooshroomOffspringType(ServerLevel serverLevel, AgeableMob ageableMob, CallbackInfoReturnable<MushroomCow> cir, @Local(ordinal = 1) MushroomCow baby) {
        var pair = MooshroomChildTypeUtil.chooseMooshroomBabyVariant((MushroomCow)(Object)this, (MushroomCow)ageableMob, baby, ((Animal)(Object)this).getLoveCause());
        if (pair == null)
            return;
        CowVariantAttachment.setCowVariant(baby, pair.getFirst(), pair.getSecond());
    }

    @ModifyExpressionValue(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;getVariant()Lnet/minecraft/world/entity/animal/MushroomCow$Variant;"))
    private MushroomCow.Variant bovinesandbuttercups$allowMooshroomToEatFlowers(MushroomCow.Variant original) {
        @Nullable CowVariant<MooshroomConfiguration> cowVariant = CowVariantAttachment.getCowVariantFromEntity((MushroomCow)(Object)this, BovinesCowTypes.MOOSHROOM_TYPE);
        if (cowVariant != null) {
            if (cowVariant.configuration().canEatFlowers().isPresent() && cowVariant.configuration().canEatFlowers().get() && original == MushroomCow.Variant.RED)
                return MushroomCow.Variant.BROWN;
            else if (cowVariant.configuration().canEatFlowers().isPresent() && !cowVariant.configuration().canEatFlowers().get() && original == MushroomCow.Variant.BROWN)
                return MushroomCow.Variant.RED;
        }
        return original;
    }

    @ModifyVariable(method = "mobInteract", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/animal/MushroomCow;getEffectsFromItemStack(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;"))
    private Optional<SuspiciousStewEffects> bovinesandbuttercups$getSuspiciousEffectsFromCustomFlower(Optional<SuspiciousStewEffects> value, @Local ItemStack stack) {
        if (value.isEmpty() && stack.is(BovinesItems.CUSTOM_FLOWER) && stack.has(BovinesDataComponents.CUSTOM_FLOWER))
            return CustomFlowerItem.getSuspiciousStewEffects(stack);

        return value;
    }

    @Override
    public MushroomCow.Variant bovinesandbuttercups$initialType() {
        return bovineandbuttercups$initializedType;
    }

    @Override
    public void bovinesandbuttercups$clearInitialType() {
        bovineandbuttercups$initializedType = null;
    }
}
