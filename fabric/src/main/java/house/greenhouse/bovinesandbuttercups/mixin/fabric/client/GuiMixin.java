package house.greenhouse.bovinesandbuttercups.mixin.fabric.client;

import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.effect.LockdownEffect;
import house.greenhouse.bovinesandbuttercups.registry.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
import java.util.Map;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow @Final private Minecraft minecraft;

    @ModifyArg(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"))
    private ResourceLocation bovinesandbuttercups$overlayLockdownBorder(ResourceLocation original, @Local(ordinal = 2) int width, @Local(ordinal = 3) int height, @Local Holder<MobEffect> effect) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovinesEffects.LOCKDOWN))
            return original;

        LockdownAttachment attachment = minecraft.player.getAttached(BovinesAttachments.LOCKDOWN);
        if (!(effect.value() instanceof LockdownEffect) && attachment != null && attachment.effects().keySet().stream().anyMatch(instance -> instance.is(effect)))
            return BovinesAndButtercups.asResource("hud/effect_background_lockdown");
        return original;
    }

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void bovinesandbuttercups$renderLockdownStatusEffectOverlay(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci, @Local MobEffectTextureManager mobEffectTextureManager, @Local List<Runnable> list, @Local MobEffectInstance mobEffectInstance, @Local Holder<MobEffect> holder, @Local(ordinal = 0) float g, @Local(ordinal = 4) int n, @Local(ordinal = 5) int o) {
        if (!holder.isBound() || !(holder.value() instanceof LockdownEffect)) return;

        LockdownAttachment attachment = minecraft.player.getAttached(BovinesAttachments.LOCKDOWN);

        if (attachment == null) return;

        List<Map.Entry<Holder<MobEffect>, Integer>> statusEffectList = attachment.effects().entrySet().stream().toList();

        if (statusEffectList.isEmpty()) return;
        int lockdownEffectIndex = minecraft.player.tickCount / (160 / statusEffectList.size()) % statusEffectList.size();

        Holder<MobEffect> statusEffect1 = statusEffectList.get(lockdownEffectIndex).getKey();

        List<Map.Entry<Holder<MobEffect>, Integer>> runningOutEffectList = statusEffectList.stream().filter(statusEffectIntegerEntry -> statusEffectIntegerEntry.getValue() != -1 && statusEffectIntegerEntry.getValue() <= 200).toList();

        float alpha = g;
        if (statusEffectList.size() > 1) {
            if (!runningOutEffectList.isEmpty()) {
                int runningOutEffectIndex = minecraft.player.tickCount / (160 / runningOutEffectList.size()) % runningOutEffectList.size();

                if (!mobEffectInstance.isAmbient()) {
                    int duration = runningOutEffectList.get(runningOutEffectIndex).getValue();
                    int m = 10 - duration / 20;
                    alpha = Mth.clamp((float)duration / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos((float)duration * (float)Math.PI / 5.0f) * Mth.clamp((float)m / 10.0f * 0.25f, 0.0f, 0.25f);
                }
                statusEffect1 = runningOutEffectList.get(runningOutEffectIndex).getKey();
            }
        }

        TextureAtlasSprite additionalSprite = mobEffectTextureManager.get(statusEffect1);
        float a = alpha;

        list.add(() -> {
            graphics.setColor(1.0f, 1.0f, 1.0f, a);
            graphics.blit(n + 3, o + 3, 0, 18, 18, additionalSprite);
            graphics.setColor(1.0f, 1.0f, 1.0f, 1.0F);
        });
    }
}
