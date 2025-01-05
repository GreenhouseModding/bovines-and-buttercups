package house.greenhouse.bovinesandbuttercups.client.renderer.modifier;

import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifier;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GrassTintTextureModifier implements TextureModifier {
    @Override
    public int color(LivingEntity entity, int previousColor) {
        int argb = BiomeColors.getAverageGrassColor(entity.level(), entity.blockPosition());
        return (argb & 0xffffff) | (255 << 24);
    }
}
