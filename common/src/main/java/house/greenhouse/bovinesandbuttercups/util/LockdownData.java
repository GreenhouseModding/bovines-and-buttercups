package house.greenhouse.bovinesandbuttercups.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;

import java.util.List;
import java.util.Optional;

public record LockdownData(Holder<MobEffect> linkedEffect, Optional<Integer> duration) {
    public static final Codec<LockdownData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            MobEffect.CODEC.fieldOf("linked_effect").validate(holder -> {
                if (holder.is(BovinesEffects.LOCKDOWN)) {
                    return DataResult.error(() -> "Cannot create a lockdown effect that locks down lockdown.");
                }
                return DataResult.success(holder);
            }).forGetter(LockdownData::linkedEffect),
            Codec.INT.optionalFieldOf("duration").forGetter(LockdownData::duration)
    ).apply(inst, LockdownData::new));
    public static final Codec<List<LockdownData>> ADDITIONAL_CODEC = CODEC.listOf().optionalFieldOf("bovinesandbuttercups:lockdown_data", List.of()).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, LockdownData> STREAM_CODEC = StreamCodec.composite(
            MobEffect.STREAM_CODEC,
            LockdownData::linkedEffect,
            ByteBufCodecs.optional(ByteBufCodecs.INT),
            LockdownData::duration,
            LockdownData::new
    );
}