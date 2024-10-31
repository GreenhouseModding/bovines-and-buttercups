package house.greenhouse.bovinesandbuttercups.mixin.neoforge.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.effect.LockdownEffect;
import house.greenhouse.bovinesandbuttercups.registry.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public EffectRenderingInventoryScreenMixin(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @ModifyArg(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1))
    private ResourceLocation bovinesandbuttercups$renderLockedEffect(ResourceLocation original, @Local(ordinal = 2) int i, @Local MobEffectInstance effect) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovinesEffects.LOCKDOWN))
            return original;

        LockdownAttachment attachment = minecraft.player.getExistingData(BovinesAttachments.LOCKDOWN).orElse(null);
        if (!(effect.getEffect().value() instanceof LockdownEffect) && attachment != null && attachment.effects().keySet().stream().anyMatch(instance -> instance.is(effect.getEffect())))
            return BovinesAndButtercups.asResource("container/inventory/effect_background_lockdown");
        return original;
    }

}
