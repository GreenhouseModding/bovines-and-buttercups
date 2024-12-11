package house.greenhouse.bovinesandbuttercups.client.renderer.modifier;

import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifier;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
import house.greenhouse.bovinesandbuttercups.client.api.RenderStateObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;

public class GrassTintTextureModifier implements TextureModifier {
    @Override
    public int color(CowTypeRenderState<?, ?> renderState, int previousColor) {
        int argb = BiomeColors.getAverageGrassColor(Minecraft.getInstance().level, renderState.getRenderStateObject(RenderStateObject.BLOCK_POS));
        return (argb & 0xffffff) | (255 << 24);
    }
}
