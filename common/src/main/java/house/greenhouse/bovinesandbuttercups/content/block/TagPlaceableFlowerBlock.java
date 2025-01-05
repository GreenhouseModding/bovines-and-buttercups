package house.greenhouse.bovinesandbuttercups.content.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TagPlaceableFlowerBlock extends FlowerBlock {
    public static final MapCodec<TagPlaceableFlowerBlock> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            EFFECTS_FIELD.forGetter(TagPlaceableFlowerBlock::getSuspiciousEffects),
            TagKey.codec(Registries.BLOCK).optionalFieldOf("placeable_on").forGetter(TagPlaceableFlowerBlock::optionalPlaceableOn),
            propertiesCodec()
    ).apply(inst, TagPlaceableFlowerBlock::new));

    @Nullable
    private final TagKey<Block> placeableOn;

    public TagPlaceableFlowerBlock(SuspiciousStewEffects suspiciousStewEffects, Optional<TagKey<Block>> placeableOn, BlockBehaviour.Properties properties) {
        super(suspiciousStewEffects, properties);
        this.placeableOn = placeableOn.orElse(null);
    }

    public TagPlaceableFlowerBlock(Holder<MobEffect> effect, float seconds, TagKey<Block> placeableOn, Properties properties) {
        super(effect, seconds, properties);
        this.placeableOn = placeableOn;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.getBlock() instanceof FarmBlock || (level instanceof LevelReader reader && placeableOn != null && reader.registryAccess().lookupOrThrow(Registries.BLOCK).get(placeableOn).map(holders -> holders.contains(state.getBlockHolder())).orElse(false));
    }

    @Nullable
    public TagKey<Block> placeableOn() {
        return placeableOn;
    }

    public Optional<TagKey<Block>> optionalPlaceableOn() {
        return Optional.ofNullable(placeableOn);
    }

    public MapCodec<TagPlaceableFlowerBlock> codec() {
        return CODEC;
    }
}
