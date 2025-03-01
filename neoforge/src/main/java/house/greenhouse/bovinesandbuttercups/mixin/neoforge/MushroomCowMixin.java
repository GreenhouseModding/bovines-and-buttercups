package house.greenhouse.bovinesandbuttercups.mixin.neoforge;

import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.mixin.CowSuperMixin;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends CowSuperMixin {
    protected MushroomCowMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(method = "lambda$shear$2", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;dropFromShearingLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/BiConsumer;)V"))
    private ResourceKey<LootTable> bovinesandbuttercups$modifyShearItem(ResourceKey<LootTable> original) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (cow.hasData(BovinesAttachments.COW_VARIANT) && cow.getData(BovinesAttachments.COW_VARIANT).cowVariant().value().configuration() instanceof MooshroomConfiguration mc && mc.lootTable().isPresent()) {
            return ResourceKey.create(Registries.LOOT_TABLE, mc.lootTable().get());
        }
        return original;
    }
}