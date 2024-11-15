package house.greenhouse.bovinesandbuttercups.mixin.neoforge;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomMushroom;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.mixin.CowSuperMixin;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends CowSuperMixin {
    protected MushroomCowMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "thunderHit", at = @At(value = "HEAD"), cancellable = true)
    private void bovinesandbuttercups$useSuperThunderWhenNotSpecified(ServerLevel level, LightningBolt lightning, CallbackInfo ci) {
        boolean bl = BovinesAndButtercups.convertedByBovines;
        if (!bl && CowTypeAttachment.getCowTypeFromEntity(this, BovinesCowTypeTypes.MOOSHROOM_TYPE) != null && CowTypeAttachment.getCowTypeFromEntity(this, BovinesCowTypeTypes.MOOSHROOM_TYPE).configuration().vanillaType().isEmpty() && !hasData(BovinesAttachments.MOOSHROOM_EXTRAS) || getExistingData(BovinesAttachments.MOOSHROOM_EXTRAS).isPresent() && getData(BovinesAttachments.MOOSHROOM_EXTRAS).allowConversion()) {
            bovinesandbuttercups$thunderHit(level, lightning);
            ci.cancel();
        }
    }

    @WrapWithCondition(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;setVariant(Lnet/minecraft/world/entity/animal/MushroomCow$MushroomType;)V"))
    private boolean bovinesandbuttercups$cancelThunderConversion(MushroomCow instance, MushroomCow.MushroomType variant) {
        boolean bl = BovinesAndButtercups.convertedByBovines;
        if (bl || (instance.hasData(BovinesAttachments.MOOSHROOM_EXTRAS) && !instance.getData(BovinesAttachments.MOOSHROOM_EXTRAS).allowConversion())) {
            BovinesAndButtercups.convertedByBovines = false;
            return false;
        }
        return true;
    }

    @ModifyArg(method = "shear", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemStack bovinesandbuttercups$modifyShearItem(ItemStack stack) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (cow.hasData(BovinesAttachments.COW_TYPE) && cow.getData(BovinesAttachments.COW_TYPE).cowType().value().configuration() instanceof MooshroomConfiguration mc) {
            if (mc.mushroom().blockState().isPresent()) {
                return new ItemStack(mc.mushroom().blockState().get().getBlock());
            } else if (mc.mushroom().customType().isPresent()) {
                ItemStack itemStack = new ItemStack(BovinesItems.CUSTOM_MUSHROOM);
                itemStack.set(BovinesDataComponents.CUSTOM_MUSHROOM, new ItemCustomMushroom(mc.mushroom().customType().get()));
                return itemStack;
            }
        }
        return stack;
    }
}