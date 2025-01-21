package house.greenhouse.bovinesandbuttercups.client.renderer.modifier;

import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifier;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import house.greenhouse.bovinesandbuttercups.client.api.RenderStateObject;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FallbackTextureModifier implements TextureModifier {
    private final List<ResourceLocation> conditions;

    public FallbackTextureModifier(List<ResourceLocation> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean canDisplay(CowVariantRenderState<?, ?, ?> entity) {
        List<ResourceLocation> activeConditions = entity.getRenderStateObject(RenderStateObject.ACTIVE_CONDITIONS);
        return activeConditions.stream().noneMatch(conditions::contains);
    }
}
