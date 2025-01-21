package house.greenhouse.bovinesandbuttercups.content.data.modifier;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.client.renderer.modifier.GrassTintTextureModifier;

public class GrassTintTextureModifierFactory extends TextureModifierFactory<GrassTintTextureModifier> {
    public static final MapCodec<TextureModifierFactory<?>> CODEC = MapCodec.unit(GrassTintTextureModifierFactory::new);

    @Override
    protected GrassTintTextureModifier createProvider() {
        return new GrassTintTextureModifier();
    }

    @Override
    public MapCodec<? extends TextureModifierFactory<?>> codec() {
        return CODEC;
    }

}
