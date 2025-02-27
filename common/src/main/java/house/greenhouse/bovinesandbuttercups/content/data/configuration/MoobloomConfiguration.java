package house.greenhouse.bovinesandbuttercups.content.data.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.api.block.BlockReference;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.codec.BovinesCodecs;
import house.greenhouse.bovinesandbuttercups.api.variant.ConvertData;
import house.greenhouse.bovinesandbuttercups.api.variant.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.variant.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.GrassTintTextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.Nectar;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record MoobloomConfiguration(Settings settings,
                                    BlockReference<Holder<CustomFlowerType>> flower,
                                    BlockReference<Holder<CustomFlowerType>> bud,
                                    boolean warnsBees,
                                    Optional<Holder<Nectar>> nectar,
                                    SimpleWeightedRandomList<ConvertData> sculkConverts) implements BaseCowConfiguration {
    public static final MapCodec<MoobloomConfiguration> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Settings.CODEC.forGetter(MoobloomConfiguration::settings),
            BlockReference.createCodec(CustomFlowerType.CODEC, "custom_flower").fieldOf("flower").forGetter(MoobloomConfiguration::flower),
            BlockReference.createCodec(CustomFlowerType.CODEC, "custom_flower").fieldOf("bud").forGetter(MoobloomConfiguration::bud),
            Codec.BOOL.optionalFieldOf("warns_bees", false).forGetter(MoobloomConfiguration::warnsBees),
            Nectar.CODEC.optionalFieldOf("nectar").forGetter(MoobloomConfiguration::nectar),
            BovinesCodecs.weightedEntryCodec(ConvertData.CODEC, "type").optionalFieldOf("sculk_conversion_types", SimpleWeightedRandomList.empty()).forGetter(MoobloomConfiguration::sculkConverts)
    ).apply(builder, MoobloomConfiguration::new));

    @Override
    public void postConversion(Entity oldEntity, @Nullable Entity newEntity, @Nullable LightningBolt bolt) {
        if (newEntity instanceof Moobloom moobloom && bolt != null)
            moobloom.setLastLightningBoltUUID(bolt.getUUID());
    }

    public boolean hasSnow(Entity entity) {
        return entity instanceof Moobloom moobloom && moobloom.hasSnow();
    }

    public boolean allowsConversion(Entity entity) {
        return entity instanceof Moobloom moobloom && moobloom.shouldAllowConversion();
    }

    public static MoobloomConfiguration createMissing(RegistryOps.RegistryInfoLookup lookup) {
        return new MoobloomConfiguration(new BaseCowConfiguration.Settings(
                Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/missing_moobloom")),
                BovinesCowModelTypes.TEMPERATE,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory()))),
                SimpleWeightedRandomList.empty(),
                SimpleWeightedRandomList.empty(),
                Optional.empty(),
                OffspringConditions.EMPTY),
                new BlockReference<>(Optional.empty(), Optional.empty(), Optional.of(lookup.lookup(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE).orElseThrow().getter().getOrThrow(CustomFlowerType.MISSING_KEY))),
                new BlockReference<>(Optional.empty(), Optional.empty(), Optional.of(lookup.lookup(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE).orElseThrow().getter().getOrThrow(CustomFlowerType.MISSING_KEY))),
                false,
                Optional.empty(),
                SimpleWeightedRandomList.empty());
    }
}
