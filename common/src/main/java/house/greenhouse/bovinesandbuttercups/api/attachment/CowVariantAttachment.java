package house.greenhouse.bovinesandbuttercups.api.attachment;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncCowVariantClientboundPacket;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public record CowVariantAttachment(Holder<CowVariant<?>> cowVariant, Optional<Holder<CowVariant<?>>> previousCowVariant) {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("cow_variant");
    public static final Codec<CowVariantAttachment> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            CowVariant.CODEC.fieldOf("current").forGetter(CowVariantAttachment::cowVariant),
            CowVariant.CODEC.optionalFieldOf("previous").forGetter(CowVariantAttachment::previousCowVariant)
    ).apply(inst, CowVariantAttachment::new));
    public static final Codec<CowVariantAttachment> CODEC = Codec.either(CowVariant.CODEC, DIRECT_CODEC).flatComapMap(either -> either.map(current -> new CowVariantAttachment(current, Optional.empty()), Function.identity()), attachment -> {
        if (attachment.previousCowVariant().isEmpty())
            return DataResult.success(Either.left(attachment.cowVariant()));
        return DataResult.success(Either.right(attachment));
    });

    @Nullable
    public static <C extends BaseCowConfiguration, T extends CowType<C>> CowVariant<C> getCowVariantFromEntity(LivingEntity living, T cowType) {
        Holder<CowVariant<C>> type = getCowVariantHolderFromEntity(living, cowType);
        if (type != null && type.isBound())
            return type.value();
        return null;
    }

    @Nullable
    public static <C extends BaseCowConfiguration, T extends CowType<C>> Holder<CowVariant<C>> getCowVariantHolderFromEntity(LivingEntity living, T cowType) {
        CowVariantAttachment attachment = BovinesAndButtercups.getHelper().getCowVariantAttachment(living);
        if (attachment != null && attachment.cowVariant.isBound() && attachment.cowVariant.value().type() == cowType) {
            return (Holder)attachment.cowVariant;
        }
        return null;
    }

    @Nullable
    public static <C extends BaseCowConfiguration, T extends CowType<C>> CowVariant<C> getPreviousCowVariantFromEntity(LivingEntity living, T cowType) {
        Holder<CowVariant<C>> type = getPreviousCowVariantHolderFromEntity(living, cowType);
        if (type != null && type.isBound())
            return type.value();
        return null;
    }

    @Nullable
    public static <C extends BaseCowConfiguration, T extends CowType<C>> Holder<CowVariant<C>> getPreviousCowVariantHolderFromEntity(LivingEntity living, T cowType) {
        CowVariantAttachment attachment = BovinesAndButtercups.getHelper().getCowVariantAttachment(living);
        if (attachment != null && attachment.previousCowVariant.isPresent() && attachment.previousCowVariant.get().isBound() && attachment.previousCowVariant.get().value().type() == cowType) {
            return (Holder)attachment.previousCowVariant.get();
        }
        return null;
    }

    public static <C extends BaseCowConfiguration> void setCowVariant(LivingEntity entity, Holder<CowVariant<C>> cowVariant) {
        setCowVariant(entity, cowVariant, Optional.empty());
    }

    public static <C extends BaseCowConfiguration> void setCowVariant(LivingEntity entity, Holder<CowVariant<C>> cowVariant, Holder<CowVariant<C>> previousCowVariant) {
        setCowVariant(entity, cowVariant, Optional.of(previousCowVariant));
    }

    public static <C extends BaseCowConfiguration> void setCowVariant(LivingEntity entity, Holder<CowVariant<C>> cowVariant, Optional<Holder<CowVariant<C>>> previousCowVariant) {
        if (cowVariant.isBound() && cowVariant.value().type().isApplicable(entity)) {
            Optional<Holder<CowVariant<?>>> previousVariant = previousCowVariant.map(holder -> (Holder)holder);
            BovinesAndButtercups.getHelper().setCowVariantAttachment(entity, new CowVariantAttachment((Holder) cowVariant, previousVariant));
            if (entity.getType() == EntityType.MOOSHROOM && cowVariant.value().configuration() instanceof MooshroomConfiguration mc && mc.vanillaType().isPresent())
                ((MushroomCow)entity).setVariant(mc.vanillaType().get());
        }
    }

    public static void sync(LivingEntity entity) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(entity, new SyncCowVariantClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getCowVariantAttachment(entity), false));
    }

    public static void syncToPlayer(LivingEntity entity, ServerPlayer player) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncCowVariantClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getCowVariantAttachment(entity), false));
    }
}
