package house.greenhouse.bovinesandbuttercups.content.effect;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.HolderRegistrationCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class BovinesEffects {
    public static Holder<MobEffect> LOCKDOWN;

    public static void registerAll(HolderRegistrationCallback<MobEffect> callback) {
        LOCKDOWN = callback.register(BuiltInRegistries.MOB_EFFECT, BovinesAndButtercups.asResource("lockdown"), new LockdownEffect());
    }
}
