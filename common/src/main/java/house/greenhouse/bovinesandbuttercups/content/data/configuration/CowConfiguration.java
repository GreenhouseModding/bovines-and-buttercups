package house.greenhouse.bovinesandbuttercups.content.data.configuration;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;

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
}
