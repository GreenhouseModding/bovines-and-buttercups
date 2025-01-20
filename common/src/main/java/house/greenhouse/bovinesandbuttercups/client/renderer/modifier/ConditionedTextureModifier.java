package house.greenhouse.bovinesandbuttercups.client.renderer.modifier;

import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifier;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import house.greenhouse.bovinesandbuttercups.client.api.RenderStateObject;
import net.minecraft.resources.ResourceLocation;

public class ConditionedTextureModifier implements TextureModifier {
    private final ResourceLocation id;

    public ConditionedTextureModifier(ResourceLocation id) {
        this.id = id;
    }

    public boolean canDisplay(CowVariantRenderState<?, ?, ?> state) {
        return state.getRenderStateObject(RenderStateObject.ACTIVE_CONDITIONS).contains(id);
    }
}
