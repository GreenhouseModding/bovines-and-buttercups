package house.greenhouse.bovinesandbuttercups.content.data.modifier;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;

public class BovinesTextureModifierFactories {

    public static void registerAll(RegistrationCallback<MapCodec<? extends TextureModifierFactory<?>>> callback) {
        callback.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("conditioned"), ConditionedTextureModifierFactory.CODEC);
        callback.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("emissive"), EmissiveTextureModifierFactory.CODEC);
        callback.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("fallback"), FallbackTextureModifierFactory.CODEC);
        callback.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("grass_tint"), GrassTintTextureModifierFactory.CODEC);
        callback.register(BovinesRegistries.TEXTURE_MODIFIER, BovinesAndButtercups.asResource("translucent"), TranslucentTextureModifierFactory.CODEC);
    }
}
