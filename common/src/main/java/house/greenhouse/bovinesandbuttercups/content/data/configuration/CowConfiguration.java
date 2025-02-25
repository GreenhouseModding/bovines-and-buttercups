package house.greenhouse.bovinesandbuttercups.content.data.configuration;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.api.variant.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;

import java.util.List;
import java.util.Optional;

public record CowConfiguration(Settings settings) implements BaseCowConfiguration {
    public static final MapCodec<CowConfiguration> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Settings.CODEC.forGetter(CowConfiguration::settings)
    ).apply(inst, CowConfiguration::new));

    @Override
    public void preConversion(Entity entity) {
        entity.playSound(BovinesSoundEvents.COW_CONVERT, 2.0F, 1.0F);
    }

    public boolean allowsConversion(Entity entity) {
        return entity instanceof Cow cow && BovinesAndButtercups.getHelper().getCowExtrasAttachment(cow).allowConversion();
    }

    public static CowConfiguration createMissing(RegistryOps.RegistryInfoLookup lookup) {
        return new CowConfiguration(new Settings(Optional.of(BovinesAndButtercups.asResource("cow/missing_cow")), BovinesCowModelTypes.TEMPERATE,
                List.of(), SimpleWeightedRandomList.empty(), SimpleWeightedRandomList.empty(), Optional.empty(), OffspringConditions.EMPTY));
    }
}
