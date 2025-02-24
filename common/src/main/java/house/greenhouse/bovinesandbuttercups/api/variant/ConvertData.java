package house.greenhouse.bovinesandbuttercups.api.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public record ConvertData(Holder<CowVariant<?>> variant, List<LootItemCondition> conditions, boolean setPrevious) {
    public static final Codec<ConvertData> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            CowVariant.CODEC.fieldOf("variant").forGetter(ConvertData::variant),
            LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("this_conditions", List.of()).forGetter(ConvertData::conditions),
            Codec.BOOL.optionalFieldOf("set_previous", true).forGetter(ConvertData::setPrevious)
    ).apply(inst, ConvertData::new));
    public static final Codec<ConvertData> CODEC = Codec.withAlternative(DIRECT_CODEC, CowVariant.CODEC, holder -> new ConvertData(holder, List.of(), true));
}
