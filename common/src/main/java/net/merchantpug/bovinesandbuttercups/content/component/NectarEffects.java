package net.merchantpug.bovinesandbuttercups.content.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.attachment.LockdownAttachment;
import net.merchantpug.bovinesandbuttercups.registry.BovinesEffects;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Comparator;
import java.util.List;

public record NectarEffects(List<NectarEffects.Entry> effects) {
    public static final NectarEffects EMPTY = new NectarEffects(List.of());
    public static final Codec<NectarEffects> CODEC = NectarEffects.Entry.CODEC
            .listOf()
            .xmap(NectarEffects::new, NectarEffects::effects);
    public static final StreamCodec<RegistryFriendlyByteBuf, NectarEffects> STREAM_CODEC = NectarEffects.Entry.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(NectarEffects::new, NectarEffects::effects);

    public NectarEffects withEffectAdded(NectarEffects.Entry $$0) {
        return new NectarEffects(Util.copyAndAdd(this.effects, $$0));
    }

    public void applyEffectInstance(LivingEntity entity) {
        for (Entry entry : effects) {
            BovinesAndButtercups.getHelper().getLockdownAttachment(entity).addLockdownMobEffect(entry.effect, entry.duration);
        }
        entity.addEffect(new MobEffectInstance(BovinesEffects.LOCKDOWN, effects.stream().map(Entry::duration).max(Comparator.comparingInt(value -> value)).orElse(0)));
        LockdownAttachment.sync(entity);
    }

    public record Entry(Holder<MobEffect> effect, int duration) {
        public static final Codec<NectarEffects.Entry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("id").forGetter(NectarEffects.Entry::effect),
                Codec.INT.lenientOptionalFieldOf("duration", 600).forGetter(NectarEffects.Entry::duration)
        ).apply(inst, NectarEffects.Entry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, NectarEffects.Entry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT),
                NectarEffects.Entry::effect,
                ByteBufCodecs.VAR_INT,
                NectarEffects.Entry::duration,
                NectarEffects.Entry::new
        );
    }
}
