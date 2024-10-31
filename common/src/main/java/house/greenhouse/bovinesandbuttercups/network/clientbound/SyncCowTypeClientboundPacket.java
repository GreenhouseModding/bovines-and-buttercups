package house.greenhouse.bovinesandbuttercups.network.clientbound;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record SyncCowTypeClientboundPacket(int entityId, CowTypeAttachment attachment) implements CustomPacketPayload {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("sync_cow_type");
    public static final Type<SyncCowTypeClientboundPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCowTypeClientboundPacket> STREAM_CODEC = StreamCodec.of(SyncCowTypeClientboundPacket::write, SyncCowTypeClientboundPacket::new);

    public SyncCowTypeClientboundPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), CowTypeAttachment.DIRECT_CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, buf.registryAccess()), buf.readNbt()).getOrThrow().getFirst());
    }

    public static void write(RegistryFriendlyByteBuf buf, SyncCowTypeClientboundPacket packet) {
        buf.writeInt(packet.entityId);
        buf.writeNbt(CowTypeAttachment.DIRECT_CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, buf.registryAccess()), packet.attachment).getOrThrow());
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (!(entity instanceof LivingEntity living))
                return;
            BovinesAndButtercups.getHelper().setCowTypeAttachment(living, attachment);
            for (CowModelLayer layer : BovinesAndButtercups.getHelper().getCowTypeAttachment(living).cowType().value().configuration().layers())
                for (TextureModifierFactory<?> modifier : layer.textureModifiers())
                    modifier.init(living);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
