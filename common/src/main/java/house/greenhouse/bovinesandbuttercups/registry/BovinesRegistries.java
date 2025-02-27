package house.greenhouse.bovinesandbuttercups.registry;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.variant.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifierFactory;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BovinesRegistries {
    public static final Registry<CowType<?>> COW_TYPE = create(BovinesRegistryKeys.COW_TYPE);
    public static final Registry<CowModelType> MODEL_TYPE = create(BovinesRegistryKeys.MODEL_TYPE);
    public static final Registry<MapCodec<? extends TextureModifierFactory<?>>> TEXTURE_MODIFIER = create(BovinesRegistryKeys.TEXTURE_MODIFIER);

    public static void init() {
    }

    private static <T> Registry<T> create(ResourceKey<Registry<T>> registryKey) {
        return new MappedRegistry<>(registryKey, Lifecycle.stable(), false);
    }

}
