package house.greenhouse.bovinesandbuttercups.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.mixin.AnimalAccessor;
import house.greenhouse.bovinesandbuttercups.mixin.CowSuperMixin;
import house.greenhouse.bovinesandbuttercups.util.MooshroomSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin {
    @ModifyReturnValue(method = "checkMushroomSpawnRules", at = @At("RETURN"))
    private static boolean bovinesandbuttercups$allowSpawning(boolean original, EntityType<MushroomCow> entityType, LevelAccessor levelAccessor, EntitySpawnReason reason, BlockPos blockPos, RandomSource randomSource) {
        return (original || !levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && AnimalAccessor.bovinesandbuttercups$invokeIsBrightEnoughToSpawn(levelAccessor, blockPos)) && MooshroomSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) > 0;
    }

    @Inject(method = "method_63648", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;dropFromShearingLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/BiConsumer;)V"), cancellable = true)
    private void bovinesandbuttercups$cancelItemDroppingIfUnnecessary(ServerLevel serverLevel, ItemStack itemStack, Cow cow, CallbackInfo ci) {
        MushroomCow mushroomCow = (MushroomCow)(Object)this;
        if (mushroomCow.hasAttached(BovinesAttachments.COW_VARIANT) && mushroomCow.getAttached(BovinesAttachments.COW_VARIANT).cowVariant().value().configuration() instanceof MooshroomConfiguration mc && mc.mushroom().blockState().isEmpty() && mc.mushroom().customType().isEmpty())
            ci.cancel();
    }

    @ModifyArg(method = "method_63648", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;dropFromShearingLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/BiConsumer;)V"))
    private ResourceKey<LootTable> bovinesandbuttercups$modifyShearItem(ResourceKey<LootTable> original) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (cow.hasAttached(BovinesAttachments.COW_VARIANT) && cow.getAttached(BovinesAttachments.COW_VARIANT).cowVariant().value().configuration() instanceof MooshroomConfiguration mc && mc.lootTable().isPresent()) {
            return ResourceKey.create(Registries.LOOT_TABLE, mc.lootTable().get());
        }
        return original;
    }
}