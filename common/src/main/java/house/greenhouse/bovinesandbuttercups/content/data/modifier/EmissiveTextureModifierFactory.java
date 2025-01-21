package house.greenhouse.bovinesandbuttercups.content.data.modifier;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.client.renderer.modifier.EmissiveTextureModifier;

public class EmissiveTextureModifierFactory extends TextureModifierFactory<EmissiveTextureModifier> {
    public static final MapCodec<TextureModifierFactory<?>> CODEC = MapCodec.unit(EmissiveTextureModifierFactory::new);

    @Override
    protected EmissiveTextureModifier createProvider() {
        return new EmissiveTextureModifier();
    }

    @Override
    public MapCodec<? extends TextureModifierFactory<?>> codec() {
        return CODEC;
    }

}
