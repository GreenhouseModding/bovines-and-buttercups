package house.greenhouse.bovinesandbuttercups.content.advancement.criterion;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class BovinesCriteriaTriggers {

    public static void registerAll() {
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, BreedCowWithVariantTrigger.ID, BreedCowWithVariantTrigger.INSTANCE);
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, ConvertMoobloomFromSculkTrigger.ID, ConvertMoobloomFromSculkTrigger.INSTANCE);
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, LockEffectTrigger.ID, LockEffectTrigger.INSTANCE);
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, PreventEffectTrigger.ID, PreventEffectTrigger.INSTANCE);
    }
}
