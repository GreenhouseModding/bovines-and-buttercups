package house.greenhouse.bovinesandbuttercups.client.access;

import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface FlowerCrownRenderStateAccess {
    @Nullable
    ItemStack bovinesandbuttercups$getFlowerCrown();
    void bovinesandbuttercups$setFlowerCrown(@Nullable ItemStack flowerCrown);
}
