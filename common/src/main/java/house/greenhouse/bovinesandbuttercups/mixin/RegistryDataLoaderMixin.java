package house.greenhouse.bovinesandbuttercups.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Decoder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.api.block.PlaceableEdibleType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
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
        if (registry.key() == (ResourceKey) BovinesRegistryKeys.COW_TYPE)
            for (Map.Entry<ResourceKey<CowTypeType<?>>, CowTypeType<?>> entry : BovinesRegistries.COW_TYPE_TYPE.entrySet())
                registry.register((ResourceKey<E>)entry.getValue().defaultKey(), (E) new CowType(entry.getValue(), entry.getValue().defaultConfig()), RegistrationInfo.BUILT_IN);

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.CUSTOM_FLOWER_TYPE)
            registry.register((ResourceKey<E>) CustomFlowerType.MISSING_KEY, (E) CustomFlowerType.MISSING, RegistrationInfo.BUILT_IN);

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE)
            registry.register((ResourceKey<E>) CustomMushroomType.MISSING_KEY, (E) CustomMushroomType.MISSING, RegistrationInfo.BUILT_IN);

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.EDIBLE_BLOCK_TYPE)
            registry.register((ResourceKey<E>) PlaceableEdibleType.MISSING_KEY, (E) PlaceableEdibleType.createMissing(lookup), RegistrationInfo.BUILT_IN);
    }

    @Inject(method = "loadElementFromResource", at = @At("HEAD"), cancellable = true)
    private static <E> void bovinesandbuttercups$loadContents(WritableRegistry<E> registry, Decoder<E> decoder, RegistryOps<JsonElement> ops, ResourceKey<E> key, Resource resource, RegistrationInfo info, CallbackInfo ci) {
        if (info == NETWORK_REGISTRATION_INFO)
            return;

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.COW_TYPE) {
            Optional<CowTypeType<?>> optional = BovinesRegistries.COW_TYPE_TYPE.stream().filter(k -> k.defaultKey().location().equals(key.location())).findFirst();
            if (optional.isPresent()) {
                BovinesAndButtercups.LOG.error("Attempted modification of default cow type '{}'. (Skipping).", optional.get().defaultKey().location());
                ci.cancel();
            }
        }

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.CUSTOM_FLOWER_TYPE && key.location().equals(CustomFlowerType.MISSING_KEY.location())) {
            BovinesAndButtercups.LOG.error("Attempted modification of default custom flower type '{}'. (Skipping).", CustomFlowerType.MISSING_KEY.location());
            ci.cancel();
        }

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE && key.location().equals(CustomMushroomType.MISSING_KEY.location())){
            BovinesAndButtercups.LOG.error("Attempted modification of default custom mushroom type '{}'. (Skipping).", CustomMushroomType.MISSING_KEY.location());
            ci.cancel();
        }

        if (registry.key() == (ResourceKey) BovinesRegistryKeys.EDIBLE_BLOCK_TYPE && key.location().equals(PlaceableEdibleType.MISSING_KEY.location())){
            BovinesAndButtercups.LOG.error("Attempted modification of default edible block type '{}'. (Skipping).", PlaceableEdibleType.MISSING_KEY.location());
            ci.cancel();
        }
    }
}
