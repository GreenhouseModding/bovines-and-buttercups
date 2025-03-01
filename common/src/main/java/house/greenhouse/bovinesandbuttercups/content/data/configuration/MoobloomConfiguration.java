package house.greenhouse.bovinesandbuttercups.content.data.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.api.block.BlockReference;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.codec.BovinesCodecs;
import house.greenhouse.bovinesandbuttercups.api.variant.ConvertData;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record MoobloomConfiguration(Settings settings,
                                    BlockReference<Holder<CustomFlowerType>> flower,
                                    BlockReference<Holder<CustomFlowerType>> bud,
                                    boolean warnsBees,
                                    Optional<ItemStack> nectar,
                                    Optional<ResourceLocation> lootTable,
                                    SimpleWeightedRandomList<ConvertData> sculkConverts) implements BaseCowConfiguration {
    public static final MapCodec<MoobloomConfiguration> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Settings.CODEC.forGetter(MoobloomConfiguration::settings),
            BlockReference.createCodec(CustomFlowerType.CODEC, "custom_flower").fieldOf("flower").forGetter(MoobloomConfiguration::flower),
            BlockReference.createCodec(CustomFlowerType.CODEC, "custom_flower").fieldOf("bud").forGetter(MoobloomConfiguration::bud),
            Codec.BOOL.optionalFieldOf("warns_bees", false).forGetter(MoobloomConfiguration::warnsBees),
            ItemStack.STRICT_SINGLE_ITEM_CODEC.optionalFieldOf("nectar").forGetter(MoobloomConfiguration::nectar),
            ResourceLocation.CODEC.optionalFieldOf("shearing_loot_table").forGetter(MoobloomConfiguration::lootTable),
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
}
