package house.greenhouse.bovinesandbuttercups.mixin.neoforge;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.util.ConversionUtil;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.mixin.CowMixin;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends CowMixin {
    protected MushroomCowMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "thunderHit", at = @At(value = "HEAD"), cancellable = true)
    private void bovinesandbuttercups$useSuperThunderWhenNotSpecified(ServerLevel level, LightningBolt lightning, CallbackInfo ci) {
        boolean bl = ConversionUtil.CONVERTED_BY_BOVINES.contains(this);
        if (!bl && CowVariantAttachment.getCowVariantFromEntity(this, BovinesCowTypes.MOOSHROOM_TYPE) != null && CowVariantAttachment.getCowVariantFromEntity(this, BovinesCowTypes.MOOSHROOM_TYPE).configuration().vanillaType().isEmpty() && !hasData(BovinesAttachments.MOOSHROOM_EXTRAS) || getExistingData(BovinesAttachments.MOOSHROOM_EXTRAS).isPresent() && getData(BovinesAttachments.MOOSHROOM_EXTRAS).allowConversion()) {
            bovinesandbuttercups$superThunderHit(level, lightning);
            ci.cancel();
        }
    }

    @WrapWithCondition(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;setVariant(Lnet/minecraft/world/entity/animal/MushroomCow$Variant;)V"))
    private boolean bovinesandbuttercups$cancelThunderConversion(MushroomCow instance, MushroomCow.Variant variant) {
        boolean bl = ConversionUtil.CONVERTED_BY_BOVINES.contains(this);
        if (bl || (instance.hasData(BovinesAttachments.MOOSHROOM_EXTRAS) && !instance.getData(BovinesAttachments.MOOSHROOM_EXTRAS).allowConversion())) {
            ConversionUtil.CONVERTED_BY_BOVINES.remove(this);
            return false;
        }
        return true;
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