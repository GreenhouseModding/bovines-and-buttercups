package house.greenhouse.bovinesandbuttercups.util;

import house.greenhouse.bovinesandbuttercups.api.BovinesCowVariants;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.CowConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CowSpawnUtil {
    public static int getTotalSpawnWeight(LevelAccessor level, BlockPos pos) {
        int totalWeight = 0;

        for (Holder.Reference<CowVariant<?>> cowVariant : level.registryAccess().registryOrThrow(BovinesRegistryKeys.COW_VARIANT).holders().filter(cowVariant -> cowVariant.isBound() && cowVariant.value().configuration() instanceof CowConfiguration).toList()) {
            if (!(cowVariant.value().configuration() instanceof CowConfiguration configuration)) continue;

            Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
            if (biome.isPresent())
                totalWeight += biome.get().weight().asInt();
        }
        return totalWeight;
    }

    public static Holder<CowVariant<CowConfiguration>> getMostCommonCowSpawnVariant(LevelAccessor level) {
        int largestWeight = 0;
        Holder<CowVariant<CowConfiguration>> finalCowVariant = (Holder)level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).getOrThrow(BovinesCowVariants.CowKeys.MISSING_COW);

        for (Holder<CowVariant<?>> cowVariant : level.registryAccess().registryOrThrow(BovinesRegistryKeys.COW_VARIANT).holders().filter(cowVariant -> cowVariant.isBound() && cowVariant.value().configuration() instanceof CowConfiguration cc && !cc.settings().biomes().isEmpty()).toList()) {
            if (!(cowVariant.value().configuration() instanceof CowConfiguration configuration)) continue;

            int max = configuration.settings().biomes().unwrap().stream().map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
            if (max > largestWeight) {
                finalCowVariant = (Holder)cowVariant;
                largestWeight = max;
            }
        }

        return finalCowVariant;
    }

    public static Holder<CowVariant<CowConfiguration>> getCowSpawnVariantDependingOnBiome(LevelAccessor level, BlockPos pos, RandomSource random) {
        List<Holder<CowVariant<CowConfiguration>>> cowVariantList = new ArrayList<>();
        int totalWeight = 0;

        for (Holder.Reference<CowVariant<?>> cowVariant : level.registryAccess().registryOrThrow(BovinesRegistryKeys.COW_VARIANT).holders().filter(cowVariant -> cowVariant.isBound() && cowVariant.value().configuration() instanceof MooshroomConfiguration && cowVariant.value().configuration() != cowVariant.value().type().defaultConfig()).toList()) {
            if (!(cowVariant.value().configuration() instanceof CowConfiguration configuration)) continue;

            Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(wrapper -> wrapper.data().size() == 0 || wrapper.data().contains(level.getBiome(pos))).findFirst();
            if (biome.isPresent()) {
                cowVariantList.add((Holder) cowVariant);
                totalWeight += biome.get().weight().asInt();
            }
        }

        if (cowVariantList.size() == 1) {
            return cowVariantList.getFirst();
        } else if (!cowVariantList.isEmpty()) {
            int r = Mth.nextInt(random, 0, totalWeight - 1);
            for (Holder<CowVariant<CowConfiguration>> cowVariant : cowVariantList) {
                int max = cowVariant.value().configuration().settings().biomes().unwrap().stream().filter(wrapper -> wrapper.data().size() == 0 || wrapper.data().contains(level.getBiome(pos))).map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
                r -= max;
                if (r < 0.0)
                    return cowVariant;
            }
        }
        return (Holder)level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).getOrThrow(BovinesCowVariants.CowKeys.MISSING_COW);
    }

}