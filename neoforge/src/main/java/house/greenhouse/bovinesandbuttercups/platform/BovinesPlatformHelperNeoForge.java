package house.greenhouse.bovinesandbuttercups.platform;

import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.entity.MoobloomNeoForge;
import house.greenhouse.bovinesandbuttercups.registry.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.util.PottedBlockMapUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BovinesPlatformHelperNeoForge implements BovinesPlatformHelper {

    @Override
    public BovinesPlatform getPlatform() {
        return BovinesPlatform.NEOFORGE;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }

    @Override
    public Map<Block, Block> getPottedBlockMap() {
        return PottedBlockMapUtil.getPottedContentMap();
    }

    @Override
    public LockdownAttachment getLockdownAttachment(LivingEntity entity) {
        return entity.getData(BovinesAttachments.LOCKDOWN);
    }

    @Override
    public CowTypeAttachment getCowTypeAttachment(LivingEntity entity) {
        return entity.getExistingData(BovinesAttachments.COW_TYPE).orElse(null);
    }

    @Override
    public void setCowTypeAttachment(LivingEntity entity, CowTypeAttachment attachment) {
        entity.setData(BovinesAttachments.COW_TYPE, attachment);
    }

    @Override
    public void sendClientboundPacket(ServerPlayer player, CustomPacketPayload... payloads) {
        List<CustomPacketPayload> extraPayloads = new ArrayList<>(Arrays.asList(payloads));
        extraPayloads.removeFirst();
        PacketDistributor.sendToPlayer(player, payloads[0], extraPayloads.toArray(CustomPacketPayload[]::new));
    }

    @Override
    public void sendTrackingClientboundPacket(Entity entity, CustomPacketPayload... payloads) {
        List<CustomPacketPayload> extraPayloads = new ArrayList<>(Arrays.asList(payloads));
        extraPayloads.removeFirst();
        PacketDistributor.sendToPlayersTrackingEntity(entity, payloads[0], extraPayloads.toArray(CustomPacketPayload[]::new));
    }

    @Override
    public boolean producesRichHoney(BeehiveBlockEntity blockEntity) {
        return blockEntity.getExistingData(BovinesAttachments.PRODUCES_RICH_HONEY).orElse(false);
    }

    @Override
    public boolean producesRichHoney(Entity bee) {
        return bee.getExistingData(BovinesAttachments.PRODUCES_RICH_HONEY).orElse(false);
    }

    @Override
    public void setProducesRichHoney(BeehiveBlockEntity blockEntity, boolean value) {
        if (!value) {
            blockEntity.removeData(BovinesAttachments.PRODUCES_RICH_HONEY);
            return;
        }
        blockEntity.setData(BovinesAttachments.PRODUCES_RICH_HONEY, true);
    }

    @Override
    public void setProducesRichHoney(Entity bee, boolean value) {
        if (!value) {
            bee.removeData(BovinesAttachments.PRODUCES_RICH_HONEY);
            return;
        }
        bee.setData(BovinesAttachments.PRODUCES_RICH_HONEY, true);
    }

    @Override
    public Optional<UUID> getPollinatingMoobloom(Bee bee) {
        return bee.getExistingData(BovinesAttachments.POLLINATING_MOOBLOOM);
    }

    @Override
    public void setPollinatingMoobloom(Bee bee, @Nullable UUID uuid) {
        if (uuid == null) {
            bee.removeData(BovinesAttachments.POLLINATING_MOOBLOOM);
            return;
        }
        bee.setData(BovinesAttachments.POLLINATING_MOOBLOOM, uuid);
    }

    @Override
    public Map<Holder<CowType<?>>, List<Vec3>> getParticlePositions(LivingEntity entity) {
        return entity.getExistingData(BovinesAttachments.BABY_PARTICLE_POSITIONS).orElse(Map.of());
    }

    @Override
    public void addParticlePosition(LivingEntity entity, Holder<CowType<?>> type, Vec3 pos) {
        entity.getData(BovinesAttachments.BABY_PARTICLE_POSITIONS).computeIfAbsent(type, holder -> new ArrayList<>()).add(pos);
    }

    @Override
    public void clearParticlePositions(LivingEntity entity) {
        entity.removeData(BovinesAttachments.BABY_PARTICLE_POSITIONS);
    }

    @Override
    public Moobloom createMoobloom(EntityType<Moobloom> entityType, Level level) {
        return new MoobloomNeoForge(entityType, level);
    }

    @Override
    public boolean canStickToRichHoney(BlockState richHoneyState, BlockState otherState) {
        if (richHoneyState.is(BovinesBlocks.RICH_HONEY_BLOCK) && !otherState.is(BovinesTags.BlockTags.DOES_NOT_STICK_RICH_HONEY_BLOCK))
            return false;
        return Blocks.HONEY_BLOCK.defaultBlockState().canStickTo(otherState);
    }
}