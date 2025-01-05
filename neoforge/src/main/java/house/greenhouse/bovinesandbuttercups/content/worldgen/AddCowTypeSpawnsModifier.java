package house.greenhouse.bovinesandbuttercups.content.worldgen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.MobSpawnSettingsBuilder;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record AddCowTypeSpawnsModifier(Holder<CowType<?>> cowType, Optional<HolderSet<Biome>> excludedBiomes, List<MobSpawnSettings.SpawnerData> spawners) implements BiomeModifier {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("add_cow_type_spawns");
    public static final MapCodec<AddCowTypeSpawnsModifier> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BovinesRegistries.COW_TYPE.holderByNameCodec().fieldOf("cow_type").forGetter(AddCowTypeSpawnsModifier::cowType),
            Biome.LIST_CODEC.optionalFieldOf("excluded_biomes").forGetter(AddCowTypeSpawnsModifier::excludedBiomes),
            Codec.either(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                    either -> either.map(Function.identity(), List::of),
                    list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
            ).fieldOf("spawners").forGetter(AddCowTypeSpawnsModifier::spawners)
    ).apply(builder, AddCowTypeSpawnsModifier::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!cowType.isBound()) return;
        if (ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(BovinesRegistryKeys.COW_VARIANT).stream().anyMatch(entry -> entry.type() == cowType.value() && entry.configuration().settings() != null && entry.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.data().contains(biome)) && entry.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.weight().asInt() > 0)) && phase == Phase.ADD && (this.excludedBiomes.isEmpty() || !this.excludedBiomes.get().contains(biome))) {
            MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            for (MobSpawnSettings.SpawnerData spawner : this.spawners) {
                EntityType<?> type = spawner.type;
                spawns.addSpawn(type.getCategory(), spawner);
            }
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
