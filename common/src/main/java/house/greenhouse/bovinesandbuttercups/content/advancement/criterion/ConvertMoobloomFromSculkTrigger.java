package house.greenhouse.bovinesandbuttercups.content.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class ConvertMoobloomFromSculkTrigger extends SimpleCriterionTrigger<ConvertMoobloomFromSculkTrigger.TriggerInstance> {
    public static final ConvertMoobloomFromSculkTrigger INSTANCE = new ConvertMoobloomFromSculkTrigger();
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("convert_moobloom_from_sculk");
    public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
    ).apply(inst, TriggerInstance::new));

    private ConvertMoobloomFromSculkTrigger() {}

    public void trigger(ServerPlayer player) {
        trigger(player, triggerInstance -> true);
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
    }
}
