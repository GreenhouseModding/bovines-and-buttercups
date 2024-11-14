package house.greenhouse.bovinesandbuttercups.mixin.fabric.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.effect.LockdownEffect;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public EffectRenderingInventoryScreenMixin(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @ModifyArg(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1))
    private ResourceLocation bovinesandbuttercups$renderLockedEffect(ResourceLocation original, @Local(ordinal = 2) int i, @Local MobEffectInstance effect) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovinesEffects.LOCKDOWN))
            return original;

        LockdownAttachment attachment = minecraft.player.getAttached(BovinesAttachments.LOCKDOWN);
        if (!(effect.getEffect().is(BovinesEffects.LOCKDOWN)) && attachment != null && attachment.effects().keySet().stream().anyMatch(instance -> instance.is(effect.getEffect())))
            return BovinesAndButtercups.asResource("container/inventory/effect_background_lockdown");
        return original;
    }

    @Inject(method = "renderIcons", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(IIIIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$drawOverridenEffectSprite(GuiGraphics guiGraphics, int x, int y, Iterable<MobEffectInstance> iterable, boolean large, CallbackInfo ci, MobEffectTextureManager mobEffectTextureManager, int k, Iterator var8, MobEffectInstance mobEffectInstance, Holder<MobEffect> holder, TextureAtlasSprite textureAtlasSprite) {
        if (!holder.isBound() || !(holder.is(BovinesEffects.LOCKDOWN))) return;

        List<Holder<MobEffect>> statusEffectList = BovinesAndButtercups.getHelper().getLockdownAttachment(minecraft.player).effects().keySet().stream().toList();

        if (statusEffectList.isEmpty()) return;
        int lockdownEffectIndex = minecraft.player.tickCount / (160 / statusEffectList.size()) % statusEffectList.size();

        Holder<MobEffect> mobEffect1 = statusEffectList.get(lockdownEffectIndex);

        TextureAtlasSprite additionalSprite = mobEffectTextureManager.get(mobEffect1);
        RenderSystem.setShaderTexture(0, additionalSprite.atlasLocation());
        guiGraphics.blit(x + (large ? 6 : 7), k + 7, 0, 18, 18, additionalSprite);
    }

}
