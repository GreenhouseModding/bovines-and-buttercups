package house.greenhouse.bovinesandbuttercups.content.data.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import house.greenhouse.bovinesandbuttercups.api.block.BlockReference;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;

import java.util.List;
import java.util.Optional;

public record MooshroomConfiguration(Settings settings,
                                     BlockReference<Holder<CustomMushroomType>> mushroom,
                                     List<CowModelLayer> layers,
                                     Optional<Boolean> canEatFlowers,
                                     Optional<MushroomCow.MushroomType> vanillaType,
                                     OffspringConditions offspringConditions) implements CowTypeConfiguration {

    public MooshroomConfiguration(Settings settings,
                                  BlockReference<Holder<CustomMushroomType>> mushroom,
                                  List<CowModelLayer> layers,
                                  Optional<Boolean> canEatFlowers,
                                  Optional<MushroomCow.MushroomType> vanillaType,
                                  OffspringConditions offspringConditions) {
        this.settings = settings;
        this.mushroom = mushroom;
        this.layers = layers;
        if (canEatFlowers.isEmpty() && vanillaType.isEmpty())
            throw new IllegalArgumentException("Cannot create Mooshroom Cow Type without specifying either the 'can_eat_flowers' or 'vanilla_type' fields");
        this.canEatFlowers = canEatFlowers;
        this.vanillaType = vanillaType;
        this.offspringConditions = offspringConditions;
    }
    public static final MapCodec<MooshroomConfiguration> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Settings.CODEC.forGetter(MooshroomConfiguration::settings),
            BlockReference.createCodec(CustomMushroomType.CODEC, "custom_mushroom").fieldOf("mushroom").forGetter(MooshroomConfiguration::mushroom),
            CowModelLayer.CODEC.listOf().optionalFieldOf("layers", List.of()).forGetter(MooshroomConfiguration::layers),
            Codec.BOOL.optionalFieldOf("can_eat_flowers").forGetter(MooshroomConfiguration::canEatFlowers),
            MushroomCow.MushroomType.CODEC.optionalFieldOf("vanilla_type").forGetter(MooshroomConfiguration::vanillaType),
            OffspringConditions.CODEC.optionalFieldOf("offspring_conditions", OffspringConditions.EMPTY).forGetter(MooshroomConfiguration::offspringConditions)
    ).apply(inst, MooshroomConfiguration::new));

    public void tick(Entity entity) {
        layers.forEach(cowModelLayer -> cowModelLayer.tickTextureModifiers(entity));
    }

    public static MooshroomConfiguration createMissing(RegistryOps.RegistryInfoLookup lookup) {
        return new MooshroomConfiguration(new Settings(Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/missing_mooshroom")), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.empty(), Optional.empty()), new BlockReference<>(Optional.empty(), Optional.empty(), Optional.of(lookup.lookup(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE).orElseThrow().getter().getOrThrow(CustomMushroomType.MISSING_KEY))), List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/mooshroom/mooshroom_mycelium_layer"), List.of())), Optional.of(false), Optional.empty(), OffspringConditions.EMPTY);
    }
}
