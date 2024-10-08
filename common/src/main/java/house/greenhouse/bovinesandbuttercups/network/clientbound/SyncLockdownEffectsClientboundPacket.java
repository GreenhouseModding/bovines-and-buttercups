package house.greenhouse.bovinesandbuttercups.network.clientbound;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record SyncLockdownEffectsClientboundPacket(int entityId, LockdownAttachment attachment) implements CustomPacketPayload {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("sync_lockdown_effects");
    public static final Type<SyncLockdownEffectsClientboundPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLockdownEffectsClientboundPacket> STREAM_CODEC = StreamCodec.of(SyncLockdownEffectsClientboundPacket::write, SyncLockdownEffectsClientboundPacket::new);

    public SyncLockdownEffectsClientboundPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), LockdownAttachment.CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, buf.registryAccess()), buf.readNbt()).getOrThrow().getFirst());
    }

    public static void write(RegistryFriendlyByteBuf buf, SyncLockdownEffectsClientboundPacket packet) {
        buf.writeInt(packet.entityId);
        buf.writeNbt(LockdownAttachment.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, buf.registryAccess()), packet.attachment).getOrThrow());
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (!(entity instanceof LivingEntity living))
                return;
            BovinesAndButtercups.getHelper().getLockdownAttachment(living).setLockdownMobEffects(attachment.effects());
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
