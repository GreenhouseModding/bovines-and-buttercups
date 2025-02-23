package house.greenhouse.bovinesandbuttercups.content.effect;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class BovinesEffects {
    public static final Holder<MobEffect> LOCKDOWN = register(BovinesAndButtercups.asResource("lockdown"), new LockdownEffect());

    public static void registerAll() {}

    private static Holder<MobEffect> register(ResourceLocation id, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id, effect);
    }
}
