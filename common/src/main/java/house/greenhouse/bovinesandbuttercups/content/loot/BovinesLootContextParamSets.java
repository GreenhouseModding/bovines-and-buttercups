package house.greenhouse.bovinesandbuttercups.content.loot;

import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class BovinesLootContextParamSets {
    public static final LootContextParamSet BREEDING = LootContextParamSet.builder()
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .required(BovinesLootContextParams.PARTNER)
            .required(BovinesLootContextParams.CHILD)
            .optional(BovinesLootContextParams.BREEDING_TYPE)
            .build();

    public static final LootContextParamSet ENTITY = LootContextParamSet.builder()
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .build();
}
