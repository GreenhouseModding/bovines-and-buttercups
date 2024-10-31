package house.greenhouse.bovinesandbuttercups.content.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;

import java.util.List;
import java.util.Optional;

public class LockEffectTrigger extends SimpleCriterionTrigger<LockEffectTrigger.TriggerInstance> {
    public static final LockEffectTrigger INSTANCE = new LockEffectTrigger();
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("lock_effect");
    public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().listOf().optionalFieldOf("effect").forGetter(TriggerInstance::effect)
    ).apply(inst, TriggerInstance::new));

    private LockEffectTrigger() {}

    public void trigger(ServerPlayer serverPlayer, Holder<MobEffect> effect) {
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(effect));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<List<Holder<MobEffect>>> effect) implements SimpleInstance {
        public boolean matches(Holder<MobEffect> value) {
            return effect.isEmpty() || effect.get().contains(value);
        }
    }
}
