package house.greenhouse.bovinesandbuttercups.content.loot;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.mixin.LootContextParamSetsAccessor;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class BovinesLootContextParamSets {
    public static final ContextKeySet BREEDING = new ContextKeySet.Builder()
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .required(BovinesLootContextParams.PARTNER)
            .required(BovinesLootContextParams.CHILD)
            .optional(BovinesLootContextParams.BREEDING_TYPE)
            .build();

    public static final ContextKeySet ENTITY = new ContextKeySet.Builder()
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .build();

    public static final ContextKeySet SPAWN = new ContextKeySet.Builder()
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.BLOCK_STATE)
            .optional(LootContextParams.BLOCK_ENTITY)
            .build();

    public static void registerAll() {
        LootContextParamSetsAccessor.bovinesandbuttercups$getREGISTRY().put(BovinesAndButtercups.asResource("breeding"), BREEDING);
        LootContextParamSetsAccessor.bovinesandbuttercups$getREGISTRY().put(BovinesAndButtercups.asResource("entity"), ENTITY);
        LootContextParamSetsAccessor.bovinesandbuttercups$getREGISTRY().put(BovinesAndButtercups.asResource("spawn"), SPAWN);
    }
}
