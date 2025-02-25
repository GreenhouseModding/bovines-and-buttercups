package house.greenhouse.bovinesandbuttercups.api;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.api.codec.BovinesCodecs;
import house.greenhouse.bovinesandbuttercups.api.variant.ConvertData;
import house.greenhouse.bovinesandbuttercups.api.variant.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.variant.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.api.variant.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * The generic cow variant interface, it's mostly here to make sure that
 * the game knows that your cow types are cow types.
 */
public interface BaseCowConfiguration {
    default void tick(Entity entity) {
        settings().layers().forEach(cowModelLayer -> cowModelLayer.tickTextureModifiers(entity));
    }

    default boolean hasSnow(Entity entity) {
        return false;
    }

    default boolean allowsConversion(Entity entity) {
        return false;
    }

    default void preConversion(Entity entity) {}

    default void postConversion(Entity oldEntity, @Nullable Entity newEntity, @Nullable LightningBolt bolt) {}

    /**
     * @return The settings of the cow variant, null if not set.
     */
    @Nullable
    default Settings settings() {
        return null;
    }

    /**
     * Optional settings for your cow variant, they are not in the base class as you may not want them.
     *
     * @param cowTexture A {@link ResourceLocation} for where in the assets the cow's texture is located.
     *                   <br>
     *                   If not set, it'll default to a hardcoded value depending on the cow.
     * @param model A model variant to use for this cow variant.
     * @param particle A list of model layers that will render above the base texture.
     * @param biomes A set of weighted biomes to determine whether the entity can spawn in the specified biome.
     * @param thunderConverts A list of weighted cow types that this cow will/have a chance to convert into upon being struck by lightning.
     *                        <br>
     *                        Can be Optional.empty() to keep the default thunder behavior.
     * @param offspringConditions A group of predicates required to have this cow be bred for from two parent cows. Will default to either parent's variant if unspecified.
     * @see BovinesCowModelTypes
     */
    record Settings(Optional<ResourceLocation> cowTexture,
                    CowModelType model,
                    List<CowModelLayer> layers,
                    SimpleWeightedRandomList<HolderSet<Biome>> biomes,
                    SimpleWeightedRandomList<ConvertData> thunderConverts,
                    Optional<ParticleOptions> particle,
                    OffspringConditions offspringConditions) {
        public static final MapCodec<Settings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("texture_location").forGetter(Settings::cowTexture),
                BovinesRegistries.MODEL_TYPE.byNameCodec().optionalFieldOf("model", BovinesCowModelTypes.TEMPERATE).forGetter(Settings::model),
                CowModelLayer.CODEC.listOf().optionalFieldOf("layers", List.of()).forGetter(Settings::layers),
                BovinesCodecs.weightedEntryCodec(RegistryCodecs.homogeneousList(Registries.BIOME), "biomes").optionalFieldOf("natural_spawns",SimpleWeightedRandomList.empty()).forGetter(Settings::biomes),
                BovinesCodecs.weightedEntryCodec(ConvertData.CODEC, "type").optionalFieldOf("thunder_conversion_types", SimpleWeightedRandomList.empty()).forGetter(Settings::thunderConverts),
                ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(Settings::particle),
                OffspringConditions.CODEC.optionalFieldOf("offspring_conditions", OffspringConditions.EMPTY).forGetter(Settings::offspringConditions)
        ).apply(instance, Settings::new));
    }
}
