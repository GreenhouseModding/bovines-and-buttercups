package house.greenhouse.bovinesandbuttercups.platform;

import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.entity.MoobloomFabric;
import house.greenhouse.bovinesandbuttercups.content.recipe.ingredient.RemainderIngredient;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BovinesPlatformHelperFabric implements BovinesPlatformHelper {

    @Override
    public BovinesPlatform getPlatform() {
        return BovinesPlatform.FABRIC;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public String getAttachmentKey() {
        return "fabric:attachments";
    }

    @Override
    public LockdownAttachment getLockdownAttachment(LivingEntity entity) {
        return entity.getAttachedOrCreate(BovinesAttachments.LOCKDOWN);
    }

    @Override
    public CowVariantAttachment getCowVariantAttachment(LivingEntity entity) {
        return entity.getAttached(BovinesAttachments.COW_VARIANT);
    }

    @Override
    public void setCowVariantAttachment(LivingEntity entity, CowVariantAttachment attachment) {
        entity.setAttached(BovinesAttachments.COW_VARIANT, attachment);
    }

    @Override
    public boolean hasMooshroomExtrasAttachment(LivingEntity entity) {
        return entity.hasAttached(BovinesAttachments.MOOSHROOM_EXTRAS);
    }

    @Override
    public CowExtrasAttachment getCowExtrasAttachment(LivingEntity entity) {
        return entity.getAttachedOrElse(BovinesAttachments.COW_EXTRAS, CowExtrasAttachment.DEFAULT);
    }

    @Override
    public void setCowExtrasAttachment(LivingEntity entity, CowExtrasAttachment attachment) {
        entity.setAttached(BovinesAttachments.COW_EXTRAS, attachment);
    }

    @Override
    public boolean hasCowExtrasAttachment(LivingEntity entity) {
        return entity.hasAttached(BovinesAttachments.COW_EXTRAS);
    }

    @Override
    public MooshroomExtrasAttachment getMooshroomExtrasAttachment(LivingEntity entity) {
        return entity.getAttachedOrElse(BovinesAttachments.MOOSHROOM_EXTRAS, MooshroomExtrasAttachment.DEFAULT);
    }

    @Override
    public void setMooshroomExtrasAttachment(LivingEntity entity, MooshroomExtrasAttachment attachment) {
        entity.setAttached(BovinesAttachments.MOOSHROOM_EXTRAS, attachment);
    }

    @Override
    public void sendClientboundPacket(ServerPlayer player, CustomPacketPayload... payloads) {
        for (CustomPacketPayload pl : payloads)
            ServerPlayNetworking.send(player, pl);
    }

    @Override
    public void sendTrackingClientboundPacket(Entity entity, CustomPacketPayload... payloads) {
        for (ServerPlayer other : PlayerLookup.tracking(entity))
            for (CustomPacketPayload pl : payloads)
                ServerPlayNetworking.send(other, pl);

        if (entity instanceof ServerPlayer player)
            for (CustomPacketPayload pl : payloads)
                ServerPlayNetworking.send(player, pl);
    }

    @Override
    public void sendTrackingClientboundPacket(BlockEntity entity, CustomPacketPayload... payloads) {
        for (ServerPlayer other : PlayerLookup.tracking(entity))
            for (CustomPacketPayload pl : payloads)
                ServerPlayNetworking.send(other, pl);
    }

    @Override
    public boolean producesRichHoney(BeehiveBlockEntity blockEntity) {
        return blockEntity.getAttachedOrElse(BovinesAttachments.PRODUCES_RICH_HONEY, false);
    }

    @Override
    public boolean producesRichHoney(Entity bee) {
        return bee.getAttachedOrElse(BovinesAttachments.PRODUCES_RICH_HONEY, false);
    }

    @Override
    public void setProducesRichHoney(BeehiveBlockEntity blockEntity, boolean value) {
        if (!value) {
            blockEntity.removeAttached(BovinesAttachments.PRODUCES_RICH_HONEY);
            return;
        }
        blockEntity.setAttached(BovinesAttachments.PRODUCES_RICH_HONEY, true);
    }

    @Override
    public void setProducesRichHoney(Entity bee, boolean value) {
        if (!value) {
            bee.removeAttached(BovinesAttachments.PRODUCES_RICH_HONEY);
            return;
        }
        bee.setAttached(BovinesAttachments.PRODUCES_RICH_HONEY, true);
    }

    @Override
    public Optional<UUID> getPollinatingMoobloom(Bee bee) {
        return Optional.ofNullable(bee.getAttachedOrElse(BovinesAttachments.POLLINATING_MOOBLOOM, null));
    }

    @Override
    public void setPollinatingMoobloom(Bee bee, @Nullable UUID uuid) {
        if (uuid == null) {
            bee.removeAttached(BovinesAttachments.POLLINATING_MOOBLOOM);
            return;
        }
        bee.setAttached(BovinesAttachments.POLLINATING_MOOBLOOM, uuid);
    }

    @Override
    public Map<Holder<CowVariant<?>>, List<Vec3>> getParticlePositions(LivingEntity entity) {
        return entity.getAttachedOrElse(BovinesAttachments.BABY_PARTICLE_POSITIONS, Map.of());
    }

    @Override
    public void addParticlePosition(LivingEntity entity, Holder<CowVariant<?>> type, Vec3 pos) {
        entity.getAttachedOrCreate(BovinesAttachments.BABY_PARTICLE_POSITIONS).computeIfAbsent(type, holder -> new ArrayList<>()).add(pos);
    }

    @Override
    public void clearParticlePositions(LivingEntity entity) {
        entity.removeAttached(BovinesAttachments.BABY_PARTICLE_POSITIONS);
    }

    @Override
    public Moobloom createMoobloom(EntityType<Moobloom> entityType, Level level) {
        return new MoobloomFabric(entityType, level);
    }

    @Override
    public boolean canStickToRichHoney(BlockState richHoneyState, BlockState otherState) {
        throw new NotImplementedException("BovinesPlatformHelper#canStickToRichHoney is only supposed to be implemented on NeoForge.");
    }

    @Override
    public @Nullable RemainderIngredient getRemainderIngredient(Ingredient ingredient) {
        CustomIngredient customIngredient = ingredient.getCustomIngredient();
        if (customIngredient instanceof RemainderIngredient remainderIngredient)
            return remainderIngredient;
        return null;
    }
}
