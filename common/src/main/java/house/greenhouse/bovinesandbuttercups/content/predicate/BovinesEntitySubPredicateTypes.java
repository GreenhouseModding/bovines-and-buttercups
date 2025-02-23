package house.greenhouse.bovinesandbuttercups.content.predicate;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class BovinesEntitySubPredicateTypes {
    public static void registerAll() {
        Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, BovinesAndButtercups.asResource("cow"), CowSubPredicate.CODEC);
    }
}