package house.greenhouse.bovinesandbuttercups.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Lifecycle;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Shadow @Final private static RegistrationInfo NETWORK_REGISTRATION_INFO;

    @Inject(method = "loadContentsFromManager", at = @At("TAIL"))
    private static <E> void bovinesandbuttercups$loadMissingTypes(ResourceManager manager, RegistryOps.RegistryInfoLookup lookup, WritableRegistry<E> registry, Decoder<E> decoder, Map<ResourceKey<?>, Exception> exceptionMap, CallbackInfo ci) {
        if (registry.key() == (ResourceKey) BovinesRegistryKeys.COW_VARIANT)
            for (Map.Entry<ResourceKey<CowType<?>>, CowType<?>> entry : BovinesRegistries.COW_TYPE.entrySet())
                registry.register((ResourceKey<E>)entry.getValue().defaultKey(), (E) new CowVariant(entry.getValue(), entry.getValue().createDefaultConfig(lookup)), RegistrationInfo.BUILT_IN);

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.CUSTOM_FLOWER_TYPE)
            registry.register((ResourceKey<E>) CustomFlowerType.MISSING_KEY, (E) CustomFlowerType.MISSING, RegistrationInfo.BUILT_IN);

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE)
            registry.register((ResourceKey<E>) CustomMushroomType.MISSING_KEY, (E) CustomMushroomType.MISSING, RegistrationInfo.BUILT_IN);

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.EDIBLE_BLOCK_TYPE)
            registry.register((ResourceKey<E>) EdibleBlockType.MISSING_KEY, (E) EdibleBlockType.missingEdible(new BootstrapContext<>() {
                @Override
                public Holder.Reference<EdibleBlockType> register(ResourceKey<EdibleBlockType> key, EdibleBlockType value, Lifecycle registryLifecycle) {
                    throw new RuntimeException("Cannot register value.");
                }

                @Override
                public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> registryKey) {
                    return lookup.lookup(registryKey).orElseThrow().getter();
                }
            }), RegistrationInfo.BUILT_IN);
    }

    @Inject(method = "loadElementFromResource", at = @At("HEAD"), cancellable = true)
    private static <E> void bovinesandbuttercups$loadContents(WritableRegistry<E> registry, Decoder<E> decoder, RegistryOps<JsonElement> ops, ResourceKey<E> key, Resource resource, RegistrationInfo info, CallbackInfo ci) {
        if (info == NETWORK_REGISTRATION_INFO)
            return;

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.COW_VARIANT) {
            Optional<CowType<?>> optional = BovinesRegistries.COW_TYPE.stream().filter(k -> k.defaultKey().location().equals(key.location())).findFirst();
            if (optional.isPresent()) {
                BovinesAndButtercups.LOG.error("Attempted modification of default cow variant '{}'. (Skipping).", optional.get().defaultKey().location());
                ci.cancel();
            }
        }

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.CUSTOM_FLOWER_TYPE && key.location().equals(CustomFlowerType.MISSING_KEY.location())) {
            BovinesAndButtercups.LOG.error("Attempted modification of default custom flower variant '{}'. (Skipping).", CustomFlowerType.MISSING_KEY.location());
            ci.cancel();
        }

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE && key.location().equals(CustomMushroomType.MISSING_KEY.location())){
            BovinesAndButtercups.LOG.error("Attempted modification of default custom mushroom variant '{}'. (Skipping).", CustomMushroomType.MISSING_KEY.location());
            ci.cancel();
        }

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.EDIBLE_BLOCK_TYPE && key.location().equals(EdibleBlockType.MISSING_KEY.location())){
            BovinesAndButtercups.LOG.error("Attempted modification of default edible block variant '{}'. (Skipping).", EdibleBlockType.MISSING_KEY.location());
            ci.cancel();
        }
    }
}
