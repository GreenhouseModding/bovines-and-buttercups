package house.greenhouse.bovinesandbuttercups.api.variant.modifier;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface TextureModifier {
    default int color(LivingEntity entity, int previous) {
        return previous;
    }

    default RenderType renderType(ResourceLocation location, RenderType previous) {
        return previous;
    }
}
