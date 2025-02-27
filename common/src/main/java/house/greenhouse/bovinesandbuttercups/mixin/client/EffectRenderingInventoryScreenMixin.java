package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.content.effect.LockdownEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    @Shadow protected abstract Component getEffectName(MobEffectInstance mobEffectInstance);

    public EffectRenderingInventoryScreenMixin(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Inject(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1, shift = At.Shift.AFTER))
    private void bovinesandbuttercups$overlayLockdownBorder(GuiGraphics guiGraphics, int x, int height, Iterable<MobEffectInstance> iterable, boolean wide, CallbackInfo ci, @Local(ordinal = 2) int i, @Local MobEffectInstance mobEffectInstance) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        List<MobEffectInstance> lockdownEffectInstance = this.minecraft.player.getActiveEffects().stream().filter(instance -> instance.getEffect().is(BovinesEffects.LOCKDOWN)).toList();

        if (lockdownEffectInstance.isEmpty()) return;

        if (!(mobEffectInstance.getEffect().is(BovinesEffects.LOCKDOWN)) && BovinesAndButtercups.getHelper().getLockdownAttachment(this.minecraft.player).effects().entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect())) {
            guiGraphics.blitSprite(BovinesAndButtercups.asResource("container/inventory/effect_background_lockdown"), x, i, 32, 32);
        }
    }

    @Inject(method = "renderLabels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I", ordinal = 1))
    private void bovinesandbuttercups$drawEffectDescriptionWhenHoveredOver(GuiGraphics guiGraphics, int i, int j, Iterable<MobEffectInstance> iterable, CallbackInfo ci) {
        if (this.minecraft == null) return;
        int mouseX = (int)(this.minecraft.mouseHandler.xpos() * (double)this.minecraft.getWindow().getGuiScaledWidth() / (double)this.minecraft.getWindow().getScreenWidth());
        int mouseY = (int)(this.minecraft.mouseHandler.ypos() * (double)this.minecraft.getWindow().getGuiScaledHeight() / (double)this.minecraft.getWindow().getScreenHeight());
        if (mouseX >= i && mouseX <= i + 119) {
            int l = this.topPos;
            MobEffectInstance mobEffectInstance = null;
            for (MobEffectInstance mobEffectInstance2 : iterable) {
                if (mouseY >= l && mouseY <= l + j && mobEffectInstance2.getEffect().is(BovinesEffects.LOCKDOWN)) {
                    mobEffectInstance = mobEffectInstance2;
                }
                l += j;
            }
            if (mobEffectInstance != null) {
                List<Component> list = List.of(this.getEffectName(mobEffectInstance), MobEffectUtil.formatDuration(mobEffectInstance, 1.0F, Minecraft.getInstance().level.tickRateManager().tickrate()));
                guiGraphics.renderTooltip(this.font, list, Optional.empty(), mouseX, mouseY);
            }
        }
    }

}
