package house.greenhouse.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "canTakeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;canPickUpLoot()Z"))
    private boolean bovinesandbuttercups$allowEquippingFlowerCrown(boolean original, ItemStack stack) {
        return original || stack.is(BovinesItems.FLOWER_CROWN) && getType().is(BovinesTags.EntityTypeTags.WILL_EQUIP_FLOWER_CROWN);
    }
}
