package house.greenhouse.bovinesandbuttercups.mixin.neoforge.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyArg(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V"))
    private ResourceLocation bovinesandbuttercups$renderLockedEffect(ResourceLocation original, @Local(ordinal = 2) int i, @Local MobEffectInstance effect) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovinesEffects.LOCKDOWN))
            return original;

        LockdownAttachment attachment = minecraft.player.getExistingData(BovinesAttachments.LOCKDOWN).orElse(null);
        if (!(effect.getEffect().is(BovinesEffects.LOCKDOWN)) && attachment != null && attachment.effects().keySet().stream().anyMatch(instance -> instance.is(effect.getEffect())))
            return BovinesAndButtercups.asResource("container/inventory/effect_background_lockdown");
        return original;
    }

}
