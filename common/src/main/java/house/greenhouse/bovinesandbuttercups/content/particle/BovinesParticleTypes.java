package house.greenhouse.bovinesandbuttercups.content.particle;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class BovinesParticleTypes {
    public static final ParticleType<ModelLocationParticleOptions> MODEL_LOCATION = register(BovinesAndButtercups.asResource("model_location"), create(false, type -> ModelLocationParticleOptions.CODEC, type -> ModelLocationParticleOptions.STREAM_CODEC));
    public static final ParticleType<ColorParticleOption> BLOOM =  register(BovinesAndButtercups.asResource("bloom"), create(false, ColorParticleOption::codec, ColorParticleOption::streamCodec));
    public static final ParticleType<ColorParticleOption> SHROOM =  register(BovinesAndButtercups.asResource("shroom"), create(false, ColorParticleOption::codec, ColorParticleOption::streamCodec));

    public static void registerAll() {}

    private static <T extends ParticleOptions> ParticleType<T> create(boolean alwaysShow, Function<ParticleType<T>, MapCodec<T>> codec, Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodec) {
        return new ParticleType<T>(alwaysShow) {
            public MapCodec<T> codec() {
                return codec.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec.apply(this);
            }
        };
    }

    private static <T extends ParticleOptions> ParticleType<T> register(ResourceLocation id, ParticleType<T> particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, particleType);
    }
}