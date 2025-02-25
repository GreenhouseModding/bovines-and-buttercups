package house.greenhouse.bovinesandbuttercups.content.data.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.api.block.BlockReference;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.api.variant.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.variant.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.mixin.MushroomCowAccessor;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record MooshroomConfiguration(Settings settings,
                                     BlockReference<Holder<CustomMushroomType>> mushroom,
                                     Optional<Boolean> canEatFlowers,
                                     Optional<MushroomCow.Variant> vanillaType,
                                     Optional<ResourceLocation> lootTable) implements BaseCowConfiguration {

    public MooshroomConfiguration {
        if (canEatFlowers.isEmpty() && vanillaType.isEmpty())
            throw new IllegalArgumentException("Cannot create Mooshroom Cow Variant without specifying either the 'can_eat_flowers' or 'vanilla_type' fields");
    }

    public static final MapCodec<MooshroomConfiguration> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Settings.CODEC.forGetter(MooshroomConfiguration::settings),
            BlockReference.createCodec(CustomMushroomType.CODEC, "custom_mushroom").fieldOf("mushroom").forGetter(MooshroomConfiguration::mushroom),
            Codec.BOOL.optionalFieldOf("can_eat_flowers").forGetter(MooshroomConfiguration::canEatFlowers),
            MushroomCow.Variant.CODEC.optionalFieldOf("vanilla_type").forGetter(MooshroomConfiguration::vanillaType),
            ResourceLocation.CODEC.optionalFieldOf("loot_table").forGetter(MooshroomConfiguration::lootTable)
    ).apply(inst, MooshroomConfiguration::new));

    @Override
    public void postConversion(Entity oldEntity, @Nullable Entity newEntity, @Nullable LightningBolt bolt) {
        if (newEntity instanceof MushroomCow mushroomCow && bolt != null)
            ((MushroomCowAccessor)mushroomCow).bovinesandbuttercups$setLastLightningBoltUUID(bolt.getUUID());
    }

    public boolean hasSnow(Entity entity) {
        return entity instanceof MushroomCow mooshroom && BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(mooshroom).hasSnow();
    }

    public boolean allowsConversion(Entity entity) {
        return entity instanceof MushroomCow mooshroom && BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(mooshroom).allowConversion();
    }

    public static MooshroomConfiguration createMissing(RegistryOps.RegistryInfoLookup lookup) {
        return new MooshroomConfiguration(new Settings(Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/missing_mooshroom")),
                BovinesCowModelTypes.TEMPERATE,
                List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/mooshroom/mooshroom_mycelium_layer"), List.of())),
                SimpleWeightedRandomList.empty(),
                SimpleWeightedRandomList.empty(),
                Optional.empty(),
                OffspringConditions.EMPTY),
                new BlockReference<>(Optional.empty(), Optional.empty(), Optional.of(lookup.lookup(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE).orElseThrow().getter().getOrThrow(CustomMushroomType.MISSING_KEY))),
                Optional.of(false),
                Optional.empty(),
                Optional.empty());
    }
}
