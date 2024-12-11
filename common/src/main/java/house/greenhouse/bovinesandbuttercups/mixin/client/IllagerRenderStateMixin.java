package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.client.access.FlowerCrownRenderStateAccess;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IllagerRenderState.class)
public class IllagerRenderStateMixin implements FlowerCrownRenderStateAccess {
    @Unique
    private FlowerCrown bovinesandbuttercups$flowerCrown;

    @Override
    public @Nullable FlowerCrown bovinesandbuttercups$getFlowerCrown() {
        return bovinesandbuttercups$flowerCrown;
    }

    @Override
    public void bovinesandbuttercups$setFlowerCrown(FlowerCrown flowerCrown) {
        bovinesandbuttercups$flowerCrown = flowerCrown;
    }
}