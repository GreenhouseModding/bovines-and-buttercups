package house.greenhouse.bovinesandbuttercups.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface RegistrationCallback<T> {
    void register(Registry<T> registry, ResourceLocation id, T object);
}