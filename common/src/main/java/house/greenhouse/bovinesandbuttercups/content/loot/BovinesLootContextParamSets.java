package house.greenhouse.bovinesandbuttercups.content.loot;

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
}
