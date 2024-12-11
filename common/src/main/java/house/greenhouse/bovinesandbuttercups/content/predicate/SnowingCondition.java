package house.greenhouse.bovinesandbuttercups.content.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

public class SnowingCondition implements LootItemCondition {
    public static final MapCodec<SnowingCondition> CODEC = MapCodec.unit(SnowingCondition::new);
    public static final LootItemConditionType TYPE = new LootItemConditionType(CODEC);

    public static SnowingCondition.Builder snowing() {
        return new SnowingCondition.Builder();
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 pos = context.getParameter(LootContextParams.ORIGIN);
        BlockPos blockPos = BlockPos.containing(pos);
        return context.getLevel().isRaining() && context.getLevel().getBiome(blockPos).isBound() && context.getLevel().getBiome(blockPos).value().getPrecipitationAt(blockPos, context.getLevel().getSeaLevel()) == Biome.Precipitation.SNOW;
    }

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    public static class Builder implements LootItemCondition.Builder {
        @Override
        public LootItemCondition build() {
            return new SnowingCondition();
        }
    }
}
