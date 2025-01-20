package house.greenhouse.bovinesandbuttercups.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

// TODO: Document me.
public class CowType<C extends CowConfiguration> {
    public static final Codec<Holder<CowType<?>>> CODEC = RegistryFixedCodec.create(BovinesRegistryKeys.COW_TYPE);

    private final MapCodec<CowVariant<C>> configuredCodec;
    private final List<EntityType<?>> entityTypes;
    private final ResourceKey<CowVariant<?>> defaultKey;
    private final String fallbackTexturePath;
    private final Function<RegistryOps.RegistryInfoLookup, C> defaultConfigFunction;
    private C defaultConfig;

    public CowType(MapCodec<C> codec, List<EntityType<?>> entityTypes,
                   ResourceKey<CowVariant<?>> defaultKey, String fallbackTexturePath, Function<RegistryOps.RegistryInfoLookup, C> defaultConfigFunction) {
        this.configuredCodec = RecordCodecBuilder.mapCodec(inst -> inst.group(
                codec.forGetter(CowVariant::configuration)
        ).apply(inst, (ctc) -> new CowVariant<>(this, ctc)));
        this.entityTypes = entityTypes;
        this.defaultKey = defaultKey;
        this.fallbackTexturePath = fallbackTexturePath;
        this.defaultConfigFunction = defaultConfigFunction;
    }

    public MapCodec<CowVariant<C>> cowCodec() {
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

    public ResourceKey<CowVariant<?>> defaultKey() {
        return defaultKey;
    }

    @Nullable
    public C defaultConfig() {
        return defaultConfig;
    }

    @ApiStatus.Internal
    public void setFromRegistries(RegistryAccess registryAccess) {
        defaultConfig = (C) registryAccess.lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).getOrThrow(defaultKey).value().configuration();
    }

    public C createDefaultConfig(RegistryOps.RegistryInfoLookup lookup) {
        defaultConfig = defaultConfigFunction.apply(lookup);
        return defaultConfig;
    }

}