package house.greenhouse.bovinesandbuttercups.api.attachment;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import house.greenhouse.bovinesandbuttercups.api.CowTypeType;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncCowTypeClientboundPacket;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public record CowTypeAttachment(Holder<CowType<?>> cowType, Optional<Holder<CowType<?>>> previousCowType) {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("cow_type");
    public static final Codec<CowTypeAttachment> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            CowType.CODEC.fieldOf("current").forGetter(CowTypeAttachment::cowType),
            CowType.CODEC.optionalFieldOf("previous").forGetter(CowTypeAttachment::previousCowType)
    ).apply(inst, CowTypeAttachment::new));
    public static final Codec<CowTypeAttachment> CODEC = Codec.either(CowType.CODEC, DIRECT_CODEC).flatComapMap(either -> either.map(current -> new CowTypeAttachment(current, Optional.empty()), Function.identity()), attachment -> {
        if (attachment.previousCowType().isEmpty())
            return DataResult.success(Either.left(attachment.cowType()));
        return DataResult.success(Either.right(attachment));
    });

    @Nullable
    public static <C extends CowTypeConfiguration, T extends CowTypeType<C>> CowType<C> getCowTypeFromEntity(LivingEntity living, T cowType) {
        Holder<CowType<C>> type = getCowTypeHolderFromEntity(living, cowType);
        if (type != null && type.isBound())
            return type.value();
        return null;
    }

    @Nullable
    public static <C extends CowTypeConfiguration, T extends CowTypeType<C>> Holder<CowType<C>> getCowTypeHolderFromEntity(LivingEntity living, T cowType) {
        CowTypeAttachment attachment = BovinesAndButtercups.getHelper().getCowTypeAttachment(living);
        if (attachment != null && attachment.cowType.isBound() && attachment.cowType.value().type() == cowType) {
            return (Holder)attachment.cowType;
        }
        return null;
    }

    @Nullable
    public static <C extends CowTypeConfiguration, T extends CowTypeType<C>> CowType<C> getPreviousCowTypeFromEntity(LivingEntity living, T cowType) {
        Holder<CowType<C>> type = getPreviousCowTypeHolderFromEntity(living, cowType);
        if (type != null && type.isBound())
            return type.value();
        return null;
    }

    @Nullable
    public static <C extends CowTypeConfiguration, T extends CowTypeType<C>> Holder<CowType<C>> getPreviousCowTypeHolderFromEntity(LivingEntity living, T cowType) {
        CowTypeAttachment attachment = BovinesAndButtercups.getHelper().getCowTypeAttachment(living);
        if (attachment != null && attachment.previousCowType.isPresent() && attachment.previousCowType.get().isBound() && attachment.previousCowType.get().value().type() == cowType) {
            return (Holder)attachment.previousCowType.get();
        }
        return null;
    }

    public static <C extends CowTypeConfiguration> void setCowType(LivingEntity entity, Holder<CowType<C>> cowType) {
        setCowType(entity, cowType, Optional.empty());
    }

    public static <C extends CowTypeConfiguration> void setCowType(LivingEntity entity, Holder<CowType<C>> cowType, Holder<CowType<C>> previousCowType) {
        setCowType(entity, cowType, Optional.of(previousCowType));
    }

    public static <C extends CowTypeConfiguration> void setCowType(LivingEntity entity, Holder<CowType<C>> cowType, Optional<Holder<CowType<C>>> previousCowType) {
        if (cowType.isBound() && cowType.value().type().isApplicable(entity)) {
            var previousAttachment = BovinesAndButtercups.getHelper().getCowTypeAttachment(entity);
            Optional<Holder<CowType<?>>> previousType = previousAttachment != null && previousAttachment.previousCowType().isPresent() ? BovinesAndButtercups.getHelper().getCowTypeAttachment(entity).previousCowType() : previousCowType.map(holder -> (Holder)holder);
            BovinesAndButtercups.getHelper().setCowTypeAttachment(entity, new CowTypeAttachment((Holder) cowType, previousType));
            if (entity.getType() == EntityType.MOOSHROOM && cowType.value().configuration() instanceof MooshroomConfiguration mc && mc.vanillaType().isPresent())
                ((MushroomCow)entity).setVariant(mc.vanillaType().get());
        }
    }

    public static void sync(LivingEntity entity) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(entity, new SyncCowTypeClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getCowTypeAttachment(entity), false));
    }

    public static void syncToPlayer(LivingEntity entity, ServerPlayer player) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncCowTypeClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getCowTypeAttachment(entity), false));
    }
}
