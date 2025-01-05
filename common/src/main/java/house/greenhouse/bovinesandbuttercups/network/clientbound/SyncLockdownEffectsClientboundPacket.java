package house.greenhouse.bovinesandbuttercups.network.clientbound;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public record SyncLockdownEffectsClientboundPacket(int entityId, LockdownAttachment attachment, boolean onTracking) implements CustomPacketPayload {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("sync_lockdown_effects");
    public static final Type<SyncLockdownEffectsClientboundPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLockdownEffectsClientboundPacket> STREAM_CODEC = StreamCodec.of(SyncLockdownEffectsClientboundPacket::write, SyncLockdownEffectsClientboundPacket::new);

    public SyncLockdownEffectsClientboundPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), LockdownAttachment.CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, buf.registryAccess()), buf.readNbt()).getOrThrow().getFirst(), buf.readBoolean());
    }

    public static void write(RegistryFriendlyByteBuf buf, SyncLockdownEffectsClientboundPacket packet) {
        buf.writeInt(packet.entityId);
        buf.writeNbt(LockdownAttachment.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, buf.registryAccess()), packet.attachment).getOrThrow());
        buf.writeBoolean(packet.onTracking);
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (!(entity instanceof LivingEntity living)) {
                if (onTracking)
                    RETRIES.put(Pair.of(entityId, attachment), 0);
                return;
            }
            BovinesAndButtercups.getHelper().getLockdownAttachment(living).setLockdownMobEffects(attachment.effects());
        });
    }

    private static final Object2IntArrayMap<Pair<Integer, LockdownAttachment>> RETRIES = new Object2IntArrayMap<>();

    public static void retry(ClientLevel level) {
        if (RETRIES.isEmpty() || level.getLevelData().getGameTime() % 5 != 0)
            return;

        for (Map.Entry<Pair<Integer, LockdownAttachment>, Integer> pair : RETRIES.object2IntEntrySet()) {
            Entity entity = Minecraft.getInstance().level.getEntity(pair.getKey().getFirst());
            if (!(entity instanceof LivingEntity living)) {
                RETRIES.put(pair.getKey(), pair.getValue() + 1);
                continue;
            }
            BovinesAndButtercups.getHelper().getLockdownAttachment(living).setLockdownMobEffects(pair.getKey().getSecond().effects());
            for (CowModelLayer layer : BovinesAndButtercups.getHelper().getCowVariantAttachment(living).cowVariant().value().configuration().layers())
                for (TextureModifierFactory<?> modifier : layer.textureModifiers())
                    modifier.init(living);
            RETRIES.object2IntEntrySet().removeIf(p -> pair.getKey() == p.getKey());
        }
        RETRIES.values().removeIf(integer -> {
            boolean bl = integer > 5;
            if (bl)
                BovinesAndButtercups.LOG.warn("Failed to sync Lockdown Attachment on entity.");
            return bl;
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
