package house.greenhouse.bovinesandbuttercups.api.cowtype.modifier;

import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public interface TextureModifier {
    default int color(CowVariantRenderState<?, ?, ?> entity, int previous) {
        return previous;
    }

    default RenderType renderType(ResourceLocation location, RenderType previous) {
        return previous;
    }

    default boolean canDisplay(CowVariantRenderState<?, ?, ?> state) {
        return true;
    }
}
