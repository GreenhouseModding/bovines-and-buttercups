package house.greenhouse.bovinesandbuttercups.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncCowTypeClientboundPacket;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncMooshroomExtrasClientboundPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;

import java.util.stream.Stream;

public record MooshroomExtrasAttachment(boolean hasSnow, boolean snowLayerPersistent, boolean allowShearing, boolean allowConversion) {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("mooshroom_extras");
    public static final MooshroomExtrasAttachment DEFAULT = new MooshroomExtrasAttachment();
    public static final Codec<MooshroomExtrasAttachment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.BOOL.optionalFieldOf("has_snow", false).forGetter(MooshroomExtrasAttachment::hasSnow),
            Codec.BOOL.optionalFieldOf("snow_layer_persistent", false).forGetter(MooshroomExtrasAttachment::snowLayerPersistent),
            Codec.BOOL.optionalFieldOf("allow_shearing", true).forGetter(MooshroomExtrasAttachment::allowShearing),
            Codec.BOOL.optionalFieldOf("allow_conversion", true).forGetter(MooshroomExtrasAttachment::allowConversion)
    ).apply(inst, MooshroomExtrasAttachment::new));

    public MooshroomExtrasAttachment() {
        this(false, false, true, true);
    }

    public static void sync(LivingEntity entity) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(entity, new SyncMooshroomExtrasClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(entity)));
    }

    public static void syncToPlayer(LivingEntity entity, ServerPlayer player) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncMooshroomExtrasClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(entity)));
    }
}
