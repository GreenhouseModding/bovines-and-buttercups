package house.greenhouse.bovinesandbuttercups.api.cowtype.modifier;

import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public interface TextureModifier {
    default int color(CowTypeRenderState<?, ?> entity, int previous) {
        return previous;
    }

    default RenderType renderType(ResourceLocation location, RenderType previous) {
        return previous;
    }

    default boolean canDisplay(CowTypeRenderState<?, ?> state) {
        return true;
    }
}
