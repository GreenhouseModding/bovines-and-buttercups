package house.greenhouse.bovinesandbuttercups.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncMooshroomExtrasClientboundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public record CowExtrasAttachment(boolean allowConversion) {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("cow_extras");
    public static final CowExtrasAttachment DEFAULT = new CowExtrasAttachment();
    public static final Codec<CowExtrasAttachment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.BOOL.optionalFieldOf("allow_conversion", true).forGetter(CowExtrasAttachment::allowConversion)
    ).apply(inst, CowExtrasAttachment::new));

    public CowExtrasAttachment() {
        this(true);
    }

    public static void sync(LivingEntity entity) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(entity, new SyncMooshroomExtrasClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(entity), false));
    }

    public static void syncToPlayer(LivingEntity entity, ServerPlayer player) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncMooshroomExtrasClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(entity), false));
    }
}
