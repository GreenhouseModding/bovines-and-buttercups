package house.greenhouse.bovinesandbuttercups.util;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncMoobloomSnowLayerClientboundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;

public class SnowLayerUtil {
    public static InteractionResult removeSnowIfShovel(Entity entity, Player player, InteractionHand hand, ItemStack stack) {
        if (stack.is(ItemTags.SHOVELS) && !WeatherUtil.isInSnowyWeather(entity) && hasSnow(entity) && !isSnowLayerPersistent(entity)) {
            entity.gameEvent(GameEvent.ENTITY_INTERACT);
            if (!entity.level().isClientSide) {
                removeSnowLayer((ServerLevel)entity.level(), entity, SoundSource.PLAYERS);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static void removeSnowLayer(ServerLevel level, Entity entity, SoundSource source) {
        entity.level().playSound(null, entity, SoundEvents.SNOW_BREAK, source, 1.0F, 1.0F);
        setSnow(entity, false);
        int snowAmount = entity instanceof AgeableMob ageable && ageable.isBaby() ? 1 : 2;

        for (int j = 0; j < snowAmount; j++) {
            ItemEntity item = entity.spawnAtLocation(level, Items.SNOWBALL, 1);
            if (item != null) {
                item.setDeltaMovement(
                        item.getDeltaMovement()
                                .add(
                                        (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.1F,
                                        entity.getRandom().nextFloat() * 0.05F,
                                        (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.1F
                                )
                );
                item.setNoPickUpDelay();
            }
        }
    }

    public static boolean hasSnow(Entity entity) {
        if (entity instanceof Moobloom moobloom)
            return moobloom.hasSnow();
        if (entity.getType() == EntityType.MOOSHROOM && entity instanceof MushroomCow mooshroom)
            return BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(mooshroom).hasSnow();
        return false;
    }

    public static boolean isSnowLayerPersistent(Entity entity) {
        if (entity instanceof Moobloom moobloom)
            return moobloom.isSnowLayerPersistent();
        if (entity.getType() == EntityType.MOOSHROOM && entity instanceof MushroomCow mooshroom)
            return BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(mooshroom).snowLayerPersistent();
        return false;
    }

    public static void setSnow(Entity entity, boolean value) {
        if (entity instanceof Moobloom moobloom)
            moobloom.setSnow(value);
        if (entity.getType() == EntityType.MOOSHROOM && entity instanceof MushroomCow mooshroom) {
            MooshroomExtrasAttachment attachment = BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(mooshroom);
            BovinesAndButtercups.getHelper().setMooshroomExtrasAttachment(mooshroom, new MooshroomExtrasAttachment(value, attachment.snowLayerPersistent(), attachment.allowShearing(), attachment.allowConversion()));
        }
    }
}
