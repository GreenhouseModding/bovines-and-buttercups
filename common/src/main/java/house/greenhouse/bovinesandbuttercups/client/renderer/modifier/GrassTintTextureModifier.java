package house.greenhouse.bovinesandbuttercups.client.renderer.modifier;

import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifier;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
import house.greenhouse.bovinesandbuttercups.client.api.RenderStateObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.util.ARGB;

public class GrassTintTextureModifier implements TextureModifier {
    @Override
    public int color(CowTypeRenderState<?, ?, ?> renderState, int previousColor) {
        int rgb = BiomeColors.getAverageGrassColor(Minecraft.getInstance().level, renderState.getRenderStateObject(RenderStateObject.BLOCK_POS));
        return ARGB.color(255, rgb);
    }
}
