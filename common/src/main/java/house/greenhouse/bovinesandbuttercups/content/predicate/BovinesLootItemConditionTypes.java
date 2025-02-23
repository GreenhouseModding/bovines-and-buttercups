package house.greenhouse.bovinesandbuttercups.content.predicate;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BovinesLootItemConditionTypes {
    public static void registerAll() {
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, BovinesAndButtercups.asResource("block_in_radius"), BlockInRadiusCondition.TYPE);
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, BovinesAndButtercups.asResource("snowing"), SnowingCondition.TYPE);
    }
}
