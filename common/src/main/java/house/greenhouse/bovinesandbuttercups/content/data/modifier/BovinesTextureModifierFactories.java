package house.greenhouse.bovinesandbuttercups.content.data.modifier;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.core.Registry;

public class BovinesTextureModifierFactories {

    public static void registerAll() {
        Registry.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("conditioned"), ConditionedTextureModifierFactory.CODEC);
        Registry.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("emissive"), EmissiveTextureModifierFactory.CODEC);
        Registry.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("fallback"), FallbackTextureModifierFactory.CODEC);
        Registry.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("grass_tint"), GrassTintTextureModifierFactory.CODEC);
        Registry.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("translucent"), TranslucentTextureModifierFactory.CODEC);
    }
}
