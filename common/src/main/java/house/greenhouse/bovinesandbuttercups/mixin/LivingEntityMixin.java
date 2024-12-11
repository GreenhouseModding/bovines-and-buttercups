package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean canEquipWithDispenser(ItemStack stack);

    @Shadow public abstract boolean canUseSlot(EquipmentSlot slot);

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    protected LivingEntityMixin(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(method = "canEquipWithDispenser", at = @At("RETURN"))
    private boolean bovinesandbuttercups$equipFlowerCrownFromDispenser(boolean original, @Local(argsOnly = true) ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable != null && equippable.dispensable()) {
            EquipmentSlot slot = equippable.slot();
            return original || stack.is(BovinesItems.FLOWER_CROWN) && getType().is(BovinesTags.EntityTypeTags.WILL_EQUIP_FLOWER_CROWN) && canUseSlot(slot) && getItemBySlot(slot).isEmpty() && canEquipWithDispenser(stack);
        }
        return original;
    }
}
