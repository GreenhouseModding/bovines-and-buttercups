package house.greenhouse.bovinesandbuttercups.network.clientbound;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowExtrasAttachment;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;

import java.util.Map;

public record SyncCowExtrasClientboundPacket(int entityId, CowExtrasAttachment attachment, boolean onTracking) implements CustomPacketPayload {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("sync_mooshroom_extras");
    public static final Type<SyncCowExtrasClientboundPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCowExtrasClientboundPacket> STREAM_CODEC = StreamCodec.of(SyncCowExtrasClientboundPacket::write, SyncCowExtrasClientboundPacket::new);

    public SyncCowExtrasClientboundPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), ByteBufCodecs.fromCodec(CowExtrasAttachment.CODEC).decode(buf), buf.readBoolean());
    }

    public static void write(RegistryFriendlyByteBuf buf, SyncCowExtrasClientboundPacket packet) {
        buf.writeInt(packet.entityId);
        ByteBufCodecs.fromCodec(CowExtrasAttachment.CODEC).encode(buf, packet.attachment);
        buf.writeBoolean(packet.onTracking);
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (!(entity instanceof LivingEntity)) {
                if (onTracking)
                    RETRIES.put(Pair.of(entityId, attachment), 0);
                return;
            }
            if (entity.getType() == EntityType.MOOSHROOM && entity instanceof MushroomCow mooshroom)
                BovinesAndButtercups.getHelper().setCowExtrasAttachment(mooshroom, attachment);
        });
    }

    private static final Object2IntArrayMap<Pair<Integer, CowExtrasAttachment>> RETRIES = new Object2IntArrayMap<>();

    public static void retry(ClientLevel level) {
        if (RETRIES.isEmpty() || level.getLevelData().getGameTime() % 5 != 0)
            return;

        for (Map.Entry<Pair<Integer, CowExtrasAttachment>, Integer> pair : RETRIES.object2IntEntrySet()) {
            Entity entity = Minecraft.getInstance().level.getEntity(pair.getKey().getFirst());
            if (!(entity instanceof Cow cow)) {
                RETRIES.put(pair.getKey(), pair.getValue() + 1);
                continue;
            }
            BovinesAndButtercups.getHelper().setCowExtrasAttachment(cow, pair.getKey().getSecond());
            RETRIES.object2IntEntrySet().removeIf(p -> pair.getKey() == p.getKey());
        }
        RETRIES.values().removeIf(integer -> {
            boolean bl = integer > 5;
            if (bl)
                BovinesAndButtercups.LOG.warn("Failed to sync Cow Extras Attachment on entity.");
            return bl;
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}