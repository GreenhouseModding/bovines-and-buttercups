package house.greenhouse.bovinesandbuttercups.content.data.configuration;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import house.greenhouse.bovinesandbuttercups.api.block.BlockReference;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.GrassTintTextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record MoobloomConfiguration(Settings settings,
                                    BlockReference<Holder<CustomFlowerType>> flower,
                                    BlockReference<Holder<CustomFlowerType>> bud,
                                    CowModelType model,
                                    List<CowModelLayer> layers,
                                    Optional<ItemStack> nectar,
                                    Optional<ResourceLocation> lootTable,
                                    OffspringConditions offspringConditions) implements CowTypeConfiguration {
    public static final MapCodec<MoobloomConfiguration> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Settings.CODEC.forGetter(MoobloomConfiguration::settings),
            BlockReference.createCodec(CustomFlowerType.CODEC, "custom_flower").fieldOf("flower").forGetter(MoobloomConfiguration::flower),
            BlockReference.createCodec(CustomFlowerType.CODEC, "custom_flower").fieldOf("bud").forGetter(MoobloomConfiguration::bud),
            BovinesRegistries.MODEL_TYPE.byNameCodec().optionalFieldOf("model", BovinesCowModelTypes.DEFAULT).forGetter(MoobloomConfiguration::model),
            CowModelLayer.CODEC.listOf().optionalFieldOf("layers", List.of()).forGetter(MoobloomConfiguration::layers),
            ItemStack.STRICT_SINGLE_ITEM_CODEC.optionalFieldOf("nectar").forGetter(MoobloomConfiguration::nectar),
            ResourceLocation.CODEC.optionalFieldOf("shearing_loot_table").forGetter(MoobloomConfiguration::lootTable),
            OffspringConditions.CODEC.optionalFieldOf("offspring_conditions", OffspringConditions.EMPTY).forGetter(MoobloomConfiguration::offspringConditions)
    ).apply(builder, MoobloomConfiguration::new));

    public void tick(Entity entity) {
        layers.forEach(cowModelLayer -> cowModelLayer.tickTextureModifiers(entity));
    }

    public boolean hasSnow(Entity entity) {
        return entity instanceof Moobloom moobloom && moobloom.hasSnow();
    }

    public boolean allowsConversion(Entity entity) {
        return entity instanceof Moobloom moobloom && moobloom.shouldAllowConversion();
    }

    public static MoobloomConfiguration createMissing(RegistryOps.RegistryInfoLookup lookup) {
        return new MoobloomConfiguration(new CowTypeConfiguration.Settings(Optional.of(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/missing_moobloom")), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.empty(), Optional.empty()), new BlockReference<>(Optional.empty(), Optional.empty(), Optional.of(lookup.lookup(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE).orElseThrow().getter().getOrThrow(CustomFlowerType.MISSING_KEY))), new BlockReference<>(Optional.empty(), Optional.empty(), Optional.of(lookup.lookup(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE).orElseThrow().getter().getOrThrow(CustomFlowerType.MISSING_KEY))), BovinesCowModelTypes.DEFAULT, List.of(new CowModelLayer(BovinesAndButtercups.asResource("bovinesandbuttercups/moobloom/moobloom_grass_layer"), List.of(new GrassTintTextureModifierFactory()))), Optional.empty(), Optional.empty(), OffspringConditions.EMPTY);
    }
}
