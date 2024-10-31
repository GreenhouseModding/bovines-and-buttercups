package house.greenhouse.bovinesandbuttercups.content.worldgen;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BovinesBiomeModifierSerializers {
    public static void registerAll(RegistrationCallback<MapCodec<? extends BiomeModifier>> callback) {
        callback.register(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS, AddCowTypeSpawnsModifier.ID, AddCowTypeSpawnsModifier.CODEC);
    }
}
