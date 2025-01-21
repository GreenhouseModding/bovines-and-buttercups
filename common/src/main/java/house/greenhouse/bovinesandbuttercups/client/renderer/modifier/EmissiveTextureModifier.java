package house.greenhouse.bovinesandbuttercups.client.renderer.modifier;

import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class EmissiveTextureModifier implements TextureModifier {
    @Override
    public RenderType renderType(ResourceLocation location, RenderType previous) {
        return RenderType.entityTranslucentEmissive(location);
    }
}
