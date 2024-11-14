package house.greenhouse.bovinesandbuttercups.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncLockdownEffectsClientboundPacket;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public record LockdownAttachment(Map<Holder<MobEffect>, Integer> effects) {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("lockdown");
    public static final Codec<LockdownAttachment> CODEC = Codec.simpleMap(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec(), Codec.INT, Keyable.forStrings(() -> Stream.of("effect", "duration"))
    ).codec().xmap(LockdownAttachment::new, LockdownAttachment::effects);

    public LockdownAttachment(Map<Holder<MobEffect>, Integer> effects) {
        this.effects = new HashMap<>(effects);
    }

    public boolean addLockdownMobEffect(Holder<MobEffect> effect, int duration) {
        if (effects.containsKey(effect) && (duration != -1 && effects.getOrDefault(effect, -1) > duration))
            return false;
        effects.put(effect, duration);
        return true;
    }

    public boolean removeLockdownMobEffect(Holder<MobEffect> effect) {
        if (!effects.containsKey(effect))
            return false;
        effects.remove(effect);
        return true;
    }

    public void setLockdownMobEffects(Map<Holder<MobEffect>, Integer> map) {
        effects.clear();
        effects.putAll(map);
    }

    public static void sync(LivingEntity entity) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(entity, new SyncLockdownEffectsClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getLockdownAttachment(entity), false));
    }

    public static void syncToPlayer(LivingEntity entity, ServerPlayer player) {
        if (entity.level().isClientSide())
            return;
        BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncLockdownEffectsClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getLockdownAttachment(entity), false));
    }
}
