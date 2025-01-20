package house.greenhouse.bovinesandbuttercups.client.renderer.modifier;

import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifier;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public record TranslucentTextureModifier(float speed, float min, float max) implements TextureModifier {
    @Override
    public int color(CowVariantRenderState<?, ?, ?> renderState, int previousColor) {
        float current = (((LivingEntityRenderState)renderState).ageInTicks + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)) * speed;
        return ARGB.color((int) ((Mth.lerp(Mth.abs(Mth.cos(current)), min, max)) * 255), previousColor);
    }

    @Override
    public RenderType renderType(ResourceLocation location, RenderType previous) {
        return RenderType.entityTranslucent(location);
    }
}
