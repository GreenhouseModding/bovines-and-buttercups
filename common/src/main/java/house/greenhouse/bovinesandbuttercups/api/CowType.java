package house.greenhouse.bovinesandbuttercups.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// TODO: Document me.
public class CowType<C extends BaseCowConfiguration> {
    public static final Codec<Holder<CowType<?>>> CODEC = RegistryFixedCodec.create(BovinesRegistryKeys.COW_TYPE);

    private final MapCodec<CowVariant<C>> configuredCodec;
    private final List<EntityType<? extends Mob>> entityTypes;
    private final ResourceKey<CowVariant<?>> defaultKey;
    private final String fallbackTexturePath;
    private final boolean isLightningLogicIndirect;

    public CowType(MapCodec<C> codec, List<EntityType<? extends Mob>> entityTypes,
                   ResourceKey<CowVariant<?>> defaultKey, String fallbackTexturePath) {
        this(codec, entityTypes, defaultKey, fallbackTexturePath, false);
    }

    public CowType(MapCodec<C> codec, List<EntityType<? extends Mob>> entityTypes,
                   ResourceKey<CowVariant<?>> defaultKey, String fallbackTexturePath,
                   boolean isLightningLogicIndirect) {
        this.configuredCodec = RecordCodecBuilder.mapCodec(inst -> inst.group(
                codec.forGetter(CowVariant::configuration)
        ).apply(inst, (ctc) -> new CowVariant<>(this, ctc)));
        this.entityTypes = entityTypes;
        this.defaultKey = defaultKey;
        this.fallbackTexturePath = fallbackTexturePath;
        this.isLightningLogicIndirect = isLightningLogicIndirect;
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

    public EntityType<? extends Mob> getDefaultEntityType() {
        return entityTypes.getFirst();
    }

    public ResourceKey<CowVariant<?>> defaultKey() {
        return defaultKey;
    }

    @Nullable
    public Holder<CowVariant<?>> defaultConfig(RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).getOrThrow(defaultKey);
    }

    public boolean isLightningLogicIndirect() {
        return isLightningLogicIndirect;
    }
}