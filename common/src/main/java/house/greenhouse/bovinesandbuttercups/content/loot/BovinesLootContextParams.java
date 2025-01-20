package house.greenhouse.bovinesandbuttercups.content.loot;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;

public class BovinesLootContextParams {
    public static final ContextKey<Entity> PARTNER = new ContextKey<>(BovinesAndButtercups.asResource("partner"));
    public static final ContextKey<Entity> CHILD = new ContextKey<>(BovinesAndButtercups.asResource("child"));
    public static final ContextKey<Holder<CowVariant<?>>> BREEDING_TYPE = new ContextKey<>(BovinesAndButtercups.asResource("breeding_type"));
}
