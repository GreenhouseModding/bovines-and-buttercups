package house.greenhouse.bovinesandbuttercups.network.clientbound;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.ConditionedTextureModifierFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record SyncConditionedTextureModifier(int entityId, ResourceLocation conditionId, boolean value) implements CustomPacketPayload {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("sync_conditioned_texture_modifier");
    public static final Type<SyncConditionedTextureModifier> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncConditionedTextureModifier> STREAM_CODEC = StreamCodec.of(SyncConditionedTextureModifier::write, SyncConditionedTextureModifier::new);

    public SyncConditionedTextureModifier(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), buf.readResourceLocation(), buf.readBoolean());
    }

    public static void write(RegistryFriendlyByteBuf buf, SyncConditionedTextureModifier packet) {
        buf.writeInt(packet.entityId);
        buf.writeResourceLocation(packet.conditionId);
        buf.writeBoolean(packet.value);
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (!(entity instanceof LivingEntity living))
                return;
            CowTypeAttachment cowVariant = BovinesAndButtercups.getHelper().getCowVariantAttachment(living);
            if (cowVariant == null || !cowVariant.cowVariant().isBound())
                return;
            cowVariant.cowVariant().value().configuration().layers().stream().flatMap(cowModelLayer -> cowModelLayer.textureModifiers().stream()).filter(textureModifierFactory -> {
                if (textureModifierFactory instanceof ConditionedTextureModifierFactory conditioned)
                    return conditioned.getConditionId().equals(conditionId);
                return false;
            }).findFirst().ifPresent(condition -> ((ConditionedTextureModifierFactory)condition).setConditionValue(entity, value));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
