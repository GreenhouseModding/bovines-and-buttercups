package house.greenhouse.bovinesandbuttercups.mixin.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AgeableMobRenderer.class)
public interface AgeableMobRendererAccessor<T extends Mob, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Mutable @Final
    @Accessor("adultModel")
    void bovinesandbuttercups$setAdultModel(M part);
    @Mutable @Final
    @Accessor("babyModel")
    void bovinesandbuttercups$setBabyModel(M part);
}
