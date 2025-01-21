package house.greenhouse.bovinesandbuttercups.content.data.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.client.renderer.modifier.FallbackTextureModifier;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FallbackTextureModifierFactory extends TextureModifierFactory<FallbackTextureModifier> {
    public static final MapCodec<FallbackTextureModifierFactory> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(FallbackTextureModifierFactory::conditions)
    ).apply(inst, FallbackTextureModifierFactory::new));

    private final List<ResourceLocation> conditions;

    public FallbackTextureModifierFactory(List<ResourceLocation> conditions) {
        this.conditions = conditions;
    }

    public List<ResourceLocation> conditions() {
        return conditions;
    }

    @Override
    protected FallbackTextureModifier createProvider() {
        return new FallbackTextureModifier(conditions);
    }

    @Override
    public MapCodec<? extends TextureModifierFactory<?>> codec() {
        return CODEC;
    }

}
