package house.greenhouse.bovinesandbuttercups.util;

import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MooshroomSpawnUtil {
    public static int getTotalSpawnWeight(LevelAccessor level, BlockPos pos) {
        int totalWeight = 0;

        for (Holder.Reference<CowType<?>> cowType : level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).registryKeySet().stream().map(key -> level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).getOrThrow(key)).filter(configuredCowType -> configuredCowType.isBound() && configuredCowType.value().configuration() instanceof MooshroomConfiguration).toList()) {
            if (!(cowType.value().configuration() instanceof MooshroomConfiguration configuration)) continue;

            Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
            if (biome.isPresent())
                totalWeight += biome.get().weight().asInt();
        }
        return totalWeight;
    }

    public static Holder<CowType<MooshroomConfiguration>> getMostCommonMooshroomSpawnType(LevelAccessor level, MushroomCow.Variant mushroomType) {
        int largestWeight = 0;
        Holder<CowType<MooshroomConfiguration>> finalCowType = getMooshroomTypeFromMushroomType(level, mushroomType);

        for (Holder<CowType<?>> cowType : level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).registryKeySet().stream().map(key -> level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).getOrThrow(key)).filter(cowType -> cowType.isBound() && cowType.value().configuration() instanceof MooshroomConfiguration mc && !mc.settings().biomes().isEmpty()).toList()) {
            if (!(cowType.value().configuration() instanceof MooshroomConfiguration configuration)) continue;

            int max = configuration.settings().biomes().unwrap().stream().map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
            if (max > largestWeight) {
                finalCowType = (Holder)cowType;
                largestWeight = max;
            }
        }

        return finalCowType;
    }

    public static Holder<CowType<MooshroomConfiguration>> getMooshroomTypeFromMushroomType(LevelAccessor level, MushroomCow.Variant mushroomType) {
        var registry = level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE);
        return (Holder)registry.registryKeySet().stream().map(registry::getOrThrow).filter(cowTypeReference -> cowTypeReference.value().configuration() instanceof MooshroomConfiguration mc && mc.vanillaType().isPresent() && mc.vanillaType().get() == mushroomType).findFirst().orElse(registry.getOrThrow(BovinesCowTypes.MooshroomKeys.MISSING_MOOSHROOM));
    }

    public static Holder<CowType<MooshroomConfiguration>> getMooshroomSpawnTypeDependingOnBiome(LevelAccessor level, BlockPos pos, RandomSource random) {
        List<Holder<CowType<MooshroomConfiguration>>> moobloomList = new ArrayList<>();
        int totalWeight = 0;

        for (Holder.Reference<CowType<?>> cowType : level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).registryKeySet().stream().map(key -> level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).getOrThrow(key)).filter(cowType -> cowType.isBound() && cowType.value().configuration() instanceof MooshroomConfiguration && cowType.value().configuration() != cowType.value().type().defaultConfig()).toList()) {
            if (!(cowType.value().configuration() instanceof MooshroomConfiguration configuration)) continue;

            Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
            if (biome.isPresent()) {
                moobloomList.add((Holder) cowType);
                totalWeight += biome.get().weight().asInt();
            }
        }

        if (moobloomList.size() == 1) {
            return moobloomList.getFirst();
        } else if (!moobloomList.isEmpty()) {
            int r = Mth.nextInt(random, 0, totalWeight - 1);
            for (Holder<CowType<MooshroomConfiguration>> cowType : moobloomList) {
                int max = cowType.value().configuration().settings().biomes().unwrap().stream().filter(wrapper -> wrapper.data().contains(level.getBiome(pos))).map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
                r -= max;
                if (r < 0.0)
                    return cowType;
            }
        }
        return (Holder)level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).getOrThrow(BovinesCowTypes.MooshroomKeys.MISSING_MOOSHROOM);
    }

}
