package house.greenhouse.bovinesandbuttercups.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {
    @Shadow protected abstract Component getEffectName(MobEffectInstance mobEffectInstance);

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private AbstractContainerScreen<?> screen;

    @Inject(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1, shift = At.Shift.AFTER))
    private void bovinesandbuttercups$overlayLockdownBorder(GuiGraphics guiGraphics, int x, int height, Iterable<MobEffectInstance> iterable, boolean wide, CallbackInfo ci, @Local(ordinal = 2) int i, @Local MobEffectInstance mobEffectInstance) {
        if (minecraft == null || minecraft.player == null) return;

        List<MobEffectInstance> lockdownEffectInstance = minecraft.player.getActiveEffects().stream().filter(instance -> instance.getEffect().is(BovinesEffects.LOCKDOWN)).toList();

        if (lockdownEffectInstance.isEmpty()) return;

        if (!(mobEffectInstance.getEffect().is(BovinesEffects.LOCKDOWN)) && BovinesAndButtercups.getHelper().getLockdownAttachment(minecraft.player).effects().entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect())) {
            guiGraphics.blitSprite(RenderType::guiTextured, BovinesAndButtercups.asResource("container/inventory/effect_background_lockdown"), x, i, 32, 32);
        }
    }

    @Inject(method = "renderLabels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I", ordinal = 1))
    private void bovinesandbuttercups$drawEffectDescriptionWhenHoveredOver(GuiGraphics guiGraphics, int i, int j, Iterable<MobEffectInstance> iterable, CallbackInfo ci) {
        if (minecraft == null) return;
        int mouseX = (int)(minecraft.mouseHandler.xpos() * (double)minecraft.getWindow().getGuiScaledWidth() / (double)minecraft.getWindow().getScreenWidth());
        int mouseY = (int)(minecraft.mouseHandler.ypos() * (double)minecraft.getWindow().getGuiScaledHeight() / (double)minecraft.getWindow().getScreenHeight());
        if (mouseX >= i && mouseX <= i + 119) {
            int l = ((AbstractContainerScreenAccessor)this.screen).bovinesandbuttercups$getTopPos();
            MobEffectInstance mobEffectInstance = null;
            for (MobEffectInstance mobEffectInstance2 : iterable) {
                if (mouseY >= l && mouseY <= l + j && mobEffectInstance2.getEffect().is(BovinesEffects.LOCKDOWN)) {
                    mobEffectInstance = mobEffectInstance2;
                }
                l += j;
            }
            if (mobEffectInstance != null) {
                List<Component> list = List.of(getEffectName(mobEffectInstance), MobEffectUtil.formatDuration(mobEffectInstance, 1.0F, Minecraft.getInstance().level.tickRateManager().tickrate()));
                guiGraphics.renderTooltip(minecraft.font, list, Optional.empty(), mouseX, mouseY);
            }
        }
    }

}
