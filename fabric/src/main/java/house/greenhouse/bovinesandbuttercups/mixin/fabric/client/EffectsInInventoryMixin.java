package house.greenhouse.bovinesandbuttercups.mixin.fabric.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {
    @Shadow @Final private Minecraft minecraft;

    @ModifyArg(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1))
    private ResourceLocation bovinesandbuttercups$renderLockedEffect(ResourceLocation original, @Local(ordinal = 2) int i, @Local MobEffectInstance effect) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovinesEffects.LOCKDOWN))
            return original;

        LockdownAttachment attachment = minecraft.player.getAttached(BovinesAttachments.LOCKDOWN);
        if (!(effect.getEffect().is(BovinesEffects.LOCKDOWN)) && attachment != null && attachment.effects().keySet().stream().anyMatch(instance -> instance.is(effect.getEffect())))
            return BovinesAndButtercups.asResource("container/inventory/effect_background_lockdown");
        return original;
    }

    @Inject(method = "renderIcons", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getEffect()Lnet/minecraft/core/Holder;"))
    private void bovinesandbuttercups$drawOverridenEffectSprite(GuiGraphics guiGraphics, int x, int y, Iterable<MobEffectInstance> iterable, boolean large, CallbackInfo ci, @Local MobEffectTextureManager mobEffectTextureManager, @Local(ordinal = 2) int i, @Local MobEffectInstance instance) {
        if (!instance.getEffect().isBound() || !(instance.is(BovinesEffects.LOCKDOWN))) return;

        List<Holder<MobEffect>> statusEffectList = BovinesAndButtercups.getHelper().getLockdownAttachment(minecraft.player).effects().keySet().stream().toList();

        if (statusEffectList.isEmpty()) return;
        int lockdownEffectIndex = minecraft.player.tickCount / (160 / statusEffectList.size()) % statusEffectList.size();

        Holder<MobEffect> mobEffect1 = statusEffectList.get(lockdownEffectIndex);

        TextureAtlasSprite additionalSprite = mobEffectTextureManager.get(mobEffect1);
        guiGraphics.blitSprite(RenderType::guiTextured, additionalSprite, x + (large ? 6 : 7), i + 7, 18, 18);
    }

}
