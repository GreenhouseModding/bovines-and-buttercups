package house.greenhouse.bovinesandbuttercups.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

// TODO: Document me.
public class CowTypeType<C extends CowTypeConfiguration> {
    public static final Codec<Holder<CowTypeType<?>>> CODEC = RegistryFixedCodec.create(BovinesRegistryKeys.COW_TYPE_TYPE);

    private final MapCodec<CowType<C>> configuredCodec;
    private final List<EntityType<?>> entityTypes;
    private final ResourceKey<CowType<?>> defaultKey;
    private final String fallbackTexturePath;
    private final Function<RegistryOps.RegistryInfoLookup, C> defaultConfigFunction;
    private C defaultConfig;

    public CowTypeType(MapCodec<C> codec, List<EntityType<?>> entityTypes,
                       ResourceKey<CowType<?>> defaultKey, String fallbackTexturePath, Function<RegistryOps.RegistryInfoLookup, C> defaultConfigFunction) {
        this.configuredCodec = RecordCodecBuilder.mapCodec(inst -> inst.group(
                codec.forGetter(CowType::configuration)
        ).apply(inst, (ctc) -> new CowType<>(this, ctc)));
        this.entityTypes = entityTypes;
        this.defaultKey = defaultKey;
        this.fallbackTexturePath = fallbackTexturePath;
        this.defaultConfigFunction = defaultConfigFunction;
    }

    public MapCodec<CowType<C>> cowCodec() {
        return configuredCodec;
    }

    public String fallbackTexturePath() {
        return fallbackTexturePath;
    }

    public boolean isApplicable(Entity entity) {
        return isApplicable(entity.getType());
    }

    public boolean isApplicable(EntityType<?> entityType) {
        return entityTypes.contains(entityType);
    }

    public ResourceKey<CowType<?>> defaultKey() {
        return defaultKey;
    }

    @Nullable
    public C defaultConfig() {
        return defaultConfig;
    }

    public C createDefaultConfig(RegistryOps.RegistryInfoLookup lookup) {
        defaultConfig = defaultConfigFunction.apply(lookup);
        return defaultConfig;
    }

}