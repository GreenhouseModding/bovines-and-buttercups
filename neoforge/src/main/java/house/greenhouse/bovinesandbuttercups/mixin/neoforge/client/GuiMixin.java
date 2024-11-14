package house.greenhouse.bovinesandbuttercups.mixin.neoforge.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.effect.LockdownEffect;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow @Final
    private Minecraft minecraft;

    @ModifyArg(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"))
    private ResourceLocation bovinesandbuttercups$overlayLockdownBorder(ResourceLocation original, @Local(ordinal = 2) int width, @Local(ordinal = 3) int height, @Local Holder<MobEffect> effect) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovinesEffects.LOCKDOWN))
            return original;

        LockdownAttachment attachment = minecraft.player.getExistingData(BovinesAttachments.LOCKDOWN).orElse(null);
        if (!(effect.is(BovinesEffects.LOCKDOWN)) && attachment != null && attachment.effects().keySet().stream().anyMatch(instance -> instance.is(effect)))
            return BovinesAndButtercups.asResource("hud/effect_background_lockdown");
        return original;
    }
}