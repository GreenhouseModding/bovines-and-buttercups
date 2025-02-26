package house.greenhouse.bovinesandbuttercups.platform;

import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.recipe.ingredient.RemainderIngredient;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface BovinesPlatformHelper {

    BovinesPlatform getPlatform();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    String getAttachmentKey();

    LockdownAttachment getLockdownAttachment(LivingEntity entity);

    CowVariantAttachment getCowVariantAttachment(LivingEntity entity);

    void setCowVariantAttachment(LivingEntity entity, CowVariantAttachment attachment);


    CowExtrasAttachment getCowExtrasAttachment(Cow entity);

    void setCowExtrasAttachment(Cow entity, CowExtrasAttachment attachment);

    boolean hasCowExtrasAttachment(Cow entity);

    MooshroomExtrasAttachment getMooshroomExtrasAttachment(MushroomCow entity);

    void setMooshroomExtrasAttachment(MushroomCow entity, MooshroomExtrasAttachment attachment);

    boolean hasMooshroomExtrasAttachment(MushroomCow entity);

    Moobloom getAvoidingMoobloom(LivingEntity entity);

    void setAvoidingMoobloom(LivingEntity entity, Moobloom moobloom);

    int getAvoidingMoobloomStartTime(Bee entity);

    void setAvoidingMoobloomStartTime(Bee entity, int time);

    void sendClientboundPacket(ServerPlayer player, CustomPacketPayload... payloads);

    void sendTrackingClientboundPacket(Entity entity, CustomPacketPayload... payloads);

    void sendTrackingClientboundPacket(BlockEntity entity, CustomPacketPayload... payloads);

    boolean producesRichHoney(BeehiveBlockEntity blockEntity);

    boolean producesRichHoney(Entity bee);

    void setProducesRichHoney(BeehiveBlockEntity blockEntity, boolean value);

    void setProducesRichHoney(Entity bee, boolean value);

    Optional<UUID> getPollinatingMoobloom(Bee bee);

    void setPollinatingMoobloom(Bee bee, @Nullable UUID uuid);

    Map<Holder<CowVariant<?>>, List<Vec3>> getParticlePositions(LivingEntity entity);

    void addParticlePosition(LivingEntity entity, Holder<CowVariant<?>> type, Vec3 pos);

    void clearParticlePositions(LivingEntity entity);

    Moobloom createMoobloom(EntityType<Moobloom> entityType, Level level);

    boolean canStickToRichHoney(BlockState richHoneyState, BlockState otherState);

    @Nullable
    RemainderIngredient getRemainderIngredient(Ingredient ingredient);

    default void runNeoForgeConversionEventPost(LivingEntity entity, LivingEntity outcome) {}
}