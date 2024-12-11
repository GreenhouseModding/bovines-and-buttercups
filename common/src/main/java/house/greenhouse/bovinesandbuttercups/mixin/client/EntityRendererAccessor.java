package house.greenhouse.bovinesandbuttercups.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor<T extends Entity, S extends EntityRenderState> {
    @Accessor("reusedState")
    S bovinesandbuttercups$getReusedState();
}
