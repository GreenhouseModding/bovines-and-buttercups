package house.greenhouse.bovinesandbuttercups.util;

import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
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

        for (Holder.Reference<CowVariant<?>> cowVariant : level.registryAccess().registryOrThrow(BovinesRegistryKeys.COW_VARIANT).holders().filter(cowVariant -> cowVariant.isBound() && cowVariant.value().configuration() instanceof MooshroomConfiguration).toList()) {
            if (!(cowVariant.value().configuration() instanceof MooshroomConfiguration configuration)) continue;

            Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
            if (biome.isPresent())
                totalWeight += biome.get().weight().asInt();
        }
        return totalWeight;
    }

    public static Holder<CowVariant<MooshroomConfiguration>> getMostCommonMooshroomSpawnVariant(LevelAccessor level, MushroomCow.MushroomType mushroomType) {
        int largestWeight = 0;
        Holder<CowVariant<MooshroomConfiguration>> finalCowVariant = getMooshroomVariantFromMushroomType(level, mushroomType);

        for (Holder<CowVariant<?>> cowVariant : level.registryAccess().registry(BovinesRegistryKeys.COW_VARIANT).orElseThrow().holders().filter(cowVariant -> cowVariant.isBound() && cowVariant.value().configuration() instanceof MooshroomConfiguration mc && !mc.settings().biomes().isEmpty()).toList()) {
            if (!(cowVariant.value().configuration() instanceof MooshroomConfiguration configuration)) continue;

            int max = configuration.settings().biomes().unwrap().stream().map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
            if (max > largestWeight) {
                finalCowVariant = (Holder)cowVariant;
                largestWeight = max;
            }
        }

        return finalCowVariant;
    }

    public static Holder<CowVariant<MooshroomConfiguration>> getMooshroomVariantFromMushroomType(LevelAccessor level, MushroomCow.MushroomType mushroomType) {
        var registry = level.registryAccess().registryOrThrow(BovinesRegistryKeys.COW_VARIANT);
        return (Holder)registry.holders().filter(cowVariant -> cowVariant.value().configuration() instanceof MooshroomConfiguration mc && mc.vanillaType().isPresent() && mc.vanillaType().get() == mushroomType).findFirst().orElse((Holder.Reference) BovinesCowTypes.MOOBLOOM_TYPE.defaultConfig(level.registryAccess()));
    }

    public static Holder<CowVariant<MooshroomConfiguration>> getMooshroomSpawnVariantDependingOnBiome(LevelAccessor level, BlockPos pos, RandomSource random) {
        List<Holder<CowVariant<MooshroomConfiguration>>> moobloomList = new ArrayList<>();
        int totalWeight = 0;

        for (Holder.Reference<CowVariant<?>> cowVariant : level.registryAccess().registry(BovinesRegistryKeys.COW_VARIANT).orElseThrow().holders().filter(cowVariant -> cowVariant.isBound() && cowVariant.value().configuration() instanceof MooshroomConfiguration && cowVariant.value().configuration() != cowVariant.value().type().defaultConfig(level.registryAccess())).toList()) {
            if (!(cowVariant.value().configuration() instanceof MooshroomConfiguration configuration)) continue;

            Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
            if (biome.isPresent()) {
                moobloomList.add((Holder) cowVariant);
                totalWeight += biome.get().weight().asInt();
            }
        }

        if (moobloomList.size() == 1) {
            return moobloomList.getFirst();
        } else if (!moobloomList.isEmpty()) {
            int r = Mth.nextInt(random, 0, totalWeight - 1);
            for (Holder<CowVariant<MooshroomConfiguration>> cowVariant : moobloomList) {
                int max = cowVariant.value().configuration().settings().biomes().unwrap().stream().filter(wrapper -> wrapper.data().contains(level.getBiome(pos))).map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
                r -= max;
                if (r < 0.0)
                    return cowVariant;
            }
        }
        return (Holder) BovinesCowTypes.MOOSHROOM_TYPE.defaultConfig(level.registryAccess());
    }

}
