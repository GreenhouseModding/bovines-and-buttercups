package house.greenhouse.bovinesandbuttercups.content.advancement.criterion;

import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;

public class BovinesCriteriaTriggers {

    public static void registerAll(RegistrationCallback<CriterionTrigger<?>> callback) {
        callback.register(BuiltInRegistries.TRIGGER_TYPES, BreedCowWithTypeTrigger.ID, BreedCowWithTypeTrigger.INSTANCE);
        callback.register(BuiltInRegistries.TRIGGER_TYPES, LockEffectTrigger.ID, LockEffectTrigger.INSTANCE);
        callback.register(BuiltInRegistries.TRIGGER_TYPES, PreventEffectTrigger.ID, PreventEffectTrigger.INSTANCE);
    }
}
