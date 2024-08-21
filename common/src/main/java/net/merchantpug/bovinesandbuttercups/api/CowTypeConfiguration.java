package net.merchantpug.bovinesandbuttercups.api;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.codec.BovinesCodecs;
import net.merchantpug.bovinesandbuttercups.api.cowtype.CowModelLayer;
import net.merchantpug.bovinesandbuttercups.registry.BovinesRegistries;
import net.merchantpug.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * The generic cow type interface, it's mostly here to make sure that
 * the game knows that your cow types are cow types.
 */
public interface CowTypeConfiguration {
    default void tick(Entity entity) {}

    default List<CowModelLayer> layers() {
        return List.of();
    }

    /**
     * @return The settings of the cow type, null if not set.
     */
    @Nullable
    default Settings settings() {
        return null;
    }

    /**
     * Optional settings for your cow type, they are not in the base class as you may not want them.
     *
     * @param cowTexture         A {@link ResourceLocation} for where in the assets the cow's texture is located,
     *                           if not set, it'll default to a hardcoded value depending on the cow.
     * @param biomes             A set of weighted biomes to determine whether the entity can spawn in the specified biome.
     * @param thunderConverts    A list of weighted cow types that this cow will/have a chance to convert into upon being struck by lightning.
     *                           Can be Optional.empty() to keep the default thunder behavior.
     */
    record Settings(Optional<ResourceLocation> cowTexture,
                    List<WeightedEntry.Wrapper<HolderSet<Biome>>> biomes,
                    List<WeightedEntry.Wrapper<Holder<CowType<?>>>> thunderConverts,
                    Optional<ParticleOptions> particle) {
        public static final MapCodec<Settings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("texture_location").forGetter(Settings::cowTexture),
                BovinesCodecs.wrapperCodec(HolderSetCodec.create(Registries.BIOME, Biome.CODEC, false), "biomes").listOf().fieldOf("natural_spawns").forGetter(Settings::biomes),
                BovinesCodecs.wrapperCodec(RegistryFixedCodec.create(BovinesRegistryKeys.COW_TYPE), "type").listOf().optionalFieldOf("thunder_conversion_types", List.of()).forGetter(Settings::thunderConverts),
                ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(Settings::particle)
        ).apply(instance, Settings::new));

        public <C extends CowTypeConfiguration, T extends CowTypeType<C>> List<WeightedEntry.Wrapper<Holder<CowType<C>>>> filterThunderConverts(T type) {
            return (List)thunderConverts.stream().filter(holderWrapper -> {
                boolean bl = holderWrapper.data().isBound() && holderWrapper.data().value().type() == type;
                if (!bl)
                    BovinesAndButtercups.LOG.error("Attempted to add CowType '{}', which does not have CowTypeType '{}' to thunder conversion list.", holderWrapper.data().unwrapKey().map(key -> key.location()).orElse(null), BovinesRegistries.COW_TYPE_TYPE.getKey(type));
                return bl;
            }).toList();
        }
    }
}
