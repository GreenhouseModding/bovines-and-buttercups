package house.greenhouse.bovinesandbuttercups.api.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Optional;

public record BlockReference<T>(Optional<BlockState> blockState,
                                Optional<ResourceLocation> modelLocation,
                                Optional<T> customType) {

    public static <T> Codec<BlockReference<T>> createCodec(Codec<T> customCodec, String customTypeKey) {
        return RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.optionalFieldOf("block_state").forGetter(BlockReference::blockState),
                ResourceLocation.CODEC.optionalFieldOf("model_location").forGetter(BlockReference::modelLocation),
                customCodec.optionalFieldOf(customTypeKey).forGetter(BlockReference::customType)
        ).apply(builder, BlockReference::new));
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof BlockReference<?> other))
            return false;

        return other.blockState.equals(this.blockState) && other.modelLocation.equals(this.modelLocation) && other.customType.equals(this.customType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.blockState, this.modelLocation, this.customType);
    }


}