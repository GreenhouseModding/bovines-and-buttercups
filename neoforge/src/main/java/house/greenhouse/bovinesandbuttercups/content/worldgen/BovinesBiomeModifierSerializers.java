package house.greenhouse.bovinesandbuttercups.content.worldgen;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BovinesBiomeModifierSerializers {
    public static void registerAll() {
        Registry.register(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS, AddCowTypeSpawnsModifier.ID, AddCowTypeSpawnsModifier.CODEC);
    }
}
