package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.client.access.FlowerCrownRenderStateAccess;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(IllagerRenderState.class)
public class IllagerRenderStateMixin implements FlowerCrownRenderStateAccess {
    @Unique
    private ItemStack bovinesandbuttercups$flowerCrown;

    @Override
    public @Nullable ItemStack bovinesandbuttercups$getFlowerCrown() {
        return bovinesandbuttercups$flowerCrown;
    }

    @Override
    public void bovinesandbuttercups$setFlowerCrown(ItemStack flowerCrown) {
        bovinesandbuttercups$flowerCrown = flowerCrown;
    }
}