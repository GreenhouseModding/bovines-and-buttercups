package house.greenhouse.bovinesandbuttercups.client.renderer.modifier;

import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public record TranslucentTextureModifier(float speed, float min, float max) implements TextureModifier {
    @Override
    public int color(LivingEntity entity, int previousColor) {
        float current = (entity.tickCount + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false)) * speed;
        return FastColor.ARGB32.color((int) ((Mth.lerp(Mth.abs(Mth.cos(current)), min, max)) * 255), previousColor);
    }

    @Override
    public RenderType renderType(ResourceLocation location, RenderType previous) {
        return RenderType.entityTranslucent(location);
    }
}
