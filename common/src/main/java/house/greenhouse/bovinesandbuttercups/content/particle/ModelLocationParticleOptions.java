package house.greenhouse.bovinesandbuttercups.content.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ModelLocationParticleOptions(ResourceLocation modelKey, String modelVariant) implements ParticleOptions {
    public static final MapCodec<ModelLocationParticleOptions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("key").forGetter(ModelLocationParticleOptions::modelKey),
            Codec.STRING.fieldOf("variant").forGetter(ModelLocationParticleOptions::modelVariant)
    ).apply(inst, ModelLocationParticleOptions::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ModelLocationParticleOptions> STREAM_CODEC = StreamCodec.of(ModelLocationParticleOptions::write, ModelLocationParticleOptions::new);

    public ModelLocationParticleOptions(FriendlyByteBuf buf) {
        this(buf.readResourceLocation(), buf.readUtf());
    }

    public static void write(FriendlyByteBuf buf, ModelLocationParticleOptions options) {
        buf.writeResourceLocation(options.modelKey);
        buf.writeUtf(options.modelVariant);
    }

    public ParticleType<ModelLocationParticleOptions> getType() {
        return BovinesParticleTypes.MODEL_LOCATION;
    }
}
