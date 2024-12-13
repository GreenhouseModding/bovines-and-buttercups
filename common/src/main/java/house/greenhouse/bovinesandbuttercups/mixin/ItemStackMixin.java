package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomMushroom;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdible;
import house.greenhouse.bovinesandbuttercups.content.item.CustomFlowerItem;
import house.greenhouse.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import house.greenhouse.bovinesandbuttercups.content.item.CustomMushroomItem;
import house.greenhouse.bovinesandbuttercups.content.item.PlaceableEdibleItem;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow @Final
    PatchedDataComponentMap components;

    @Shadow
    public abstract Item getItem();

    @Inject(method = "set", at = @At("TAIL"))
    private <T> void bovinesandbuttercups$setDependencyComponents(DataComponentType<? super T> component, T value, CallbackInfoReturnable<T> cir) {
        DataComponentPatch.Builder patch = DataComponentPatch.builder();
        if (getItem() instanceof CustomFlowerItem && component == BovinesDataComponents.CUSTOM_FLOWER) {
            if (!this.components.hasNonDefault(DataComponents.ITEM_MODEL))
                patch.set(DataComponents.ITEM_MODEL, ((ItemCustomFlower)value).holder().value().itemModel());
        }

        if (component == BovinesDataComponents.CUSTOM_MUSHROOM && (getItem() instanceof CustomMushroomItem || getItem() instanceof CustomHugeMushroomItem)) {
            ItemCustomMushroom customMushroom = (ItemCustomMushroom) value;
            if ((!(getItem() instanceof CustomHugeMushroomItem) || customMushroom.holder().value().hasHugeBlock())) {
                ResourceLocation itemModel = getItem() instanceof CustomHugeMushroomItem ? customMushroom.holder().value(). hugeBlockItemModel().orElse(customMushroom.holder().value().itemModel()) : customMushroom.holder().value().itemModel();
                if (!this.components.hasNonDefault(DataComponents.ITEM_MODEL))
                    patch.set(DataComponents.ITEM_MODEL, itemModel);
            }
        }

        if (component == BovinesDataComponents.EDIBLE_TYPE && getItem() instanceof PlaceableEdibleItem) {
            ItemEdible edible = (ItemEdible) value;
            if (!this.components.hasNonDefault(DataComponents.ITEM_MODEL) && edible.holder().value().itemModel().isPresent())
                patch.set(DataComponents.ITEM_MODEL, edible.holder().value().itemModel().get());
            if (!this.components.hasNonDefault(DataComponents.MAX_STACK_SIZE))
                patch.set(DataComponents.MAX_STACK_SIZE, edible.holder().value().maxStackSize());
        }
        this.components.applyPatch(patch.build());
    }

    @Inject(method = "applyComponents(Lnet/minecraft/core/component/DataComponentPatch;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;verifyComponentsAfterLoad(Lnet/minecraft/world/item/ItemStack;)V"))
    private void bovinesandbuttercups$applyDependencyComponents(DataComponentPatch components, CallbackInfo ci) {
        // TODO: Eventually unhardcode this and put it in its own mod, but for now :shrug:
        DataComponentPatch.Builder patch = DataComponentPatch.builder();
        Optional<? extends ItemCustomFlower> customFlower = components.get(BovinesDataComponents.CUSTOM_FLOWER);
        if (getItem() instanceof CustomFlowerItem && customFlower != null) {
            if (customFlower.isPresent()) {
                if (!this.components.hasNonDefault(DataComponents.ITEM_MODEL))
                    patch.set(DataComponents.ITEM_MODEL, customFlower.get().holder().value().itemModel());
            } else if (this.components.hasNonDefault(DataComponents.ITEM_MODEL)) {
                ItemCustomFlower existing = this.components.get(BovinesDataComponents.CUSTOM_FLOWER);
                if (Optional.ofNullable(this.components.get(DataComponents.ITEM_MODEL)).map(rl -> existing != null && rl == existing.holder().value().itemModel()).orElse(false))
                    patch.remove(DataComponents.ITEM_MODEL);
            }
        }

        Optional<? extends ItemCustomMushroom> customMushroom = components.get(BovinesDataComponents.CUSTOM_MUSHROOM);
        if (customMushroom != null && (getItem() instanceof CustomMushroomItem || getItem() instanceof CustomHugeMushroomItem)) {
            if (customMushroom.isPresent() && (!(getItem() instanceof CustomHugeMushroomItem) || customMushroom.get().holder().value().hasHugeBlock())) {
                ResourceLocation itemModel = getItem() instanceof CustomHugeMushroomItem ? customMushroom.get().holder().value(). hugeBlockItemModel().orElse(customMushroom.get().holder().value().itemModel()) : customMushroom.get().holder().value().itemModel();
                if (!this.components.hasNonDefault(DataComponents.ITEM_MODEL)) {
                    patch.set(DataComponents.ITEM_MODEL, itemModel);
                }
            } else if (this.components.hasNonDefault(DataComponents.ITEM_MODEL)) {
                ItemCustomMushroom existing = this.components.get(BovinesDataComponents.CUSTOM_MUSHROOM);
                if (Optional.ofNullable(this.components.get(DataComponents.ITEM_MODEL)).map(rl -> existing != null && rl == existing.holder().value().itemModel()).orElse(false))
                    patch.remove(DataComponents.ITEM_MODEL);
            }
        }

        Optional<? extends ItemEdible> edibleType = components.get(BovinesDataComponents.EDIBLE_TYPE);
        if (edibleType != null && getItem() instanceof PlaceableEdibleItem) {
            if (edibleType.isPresent()) {
                if (!this.components.hasNonDefault(DataComponents.ITEM_MODEL) && edibleType.get().holder().value().itemModel().isPresent())
                    patch.set(DataComponents.ITEM_MODEL, edibleType.get().holder().value().itemModel().get());
                if (!this.components.hasNonDefault(DataComponents.MAX_STACK_SIZE))
                    patch.set(DataComponents.MAX_STACK_SIZE, edibleType.get().holder().value().maxStackSize());
            } else if (this.components.hasNonDefault(DataComponents.ITEM_MODEL)) {
                ItemEdible existing = this.components.get(BovinesDataComponents.EDIBLE_TYPE);
                if (Optional.ofNullable(this.components.get(DataComponents.ITEM_MODEL)).map(rl -> existing != null && existing.holder().value().itemModel().isPresent() && rl == existing.holder().value().itemModel().get()).orElse(false))
                    patch.remove(DataComponents.ITEM_MODEL);
                if (Optional.ofNullable(this.components.get(DataComponents.MAX_STACK_SIZE)).map(size -> existing != null && existing.holder().value().maxStackSize() == size).orElse(false))
                    patch.remove(DataComponents.MAX_STACK_SIZE);
            }
        }
        this.components.applyPatch(patch.build());
    }
}
