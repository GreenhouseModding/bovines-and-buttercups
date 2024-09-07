package house.greenhouse.bovinesandbuttercups.registry;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class BovinesLootContextParams {
    public static final LootContextParam<Entity> PARTNER = new LootContextParam<>(BovinesAndButtercups.asResource("partner"));
    public static final LootContextParam<Entity> CHILD = new LootContextParam<>(BovinesAndButtercups.asResource("child"));
    public static final LootContextParam<Holder<CowType<?>>> BREEDING_TYPE = new LootContextParam<>(BovinesAndButtercups.asResource("breeding_type"));
}
