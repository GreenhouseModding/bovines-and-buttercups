package house.greenhouse.bovinesandbuttercups.client;

import com.mojang.blaze3d.systems.RenderSystem;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.List;
import java.util.Map;

public class LockdownClientEffectExtensions implements IClientMobEffectExtensions {
    public static final LockdownClientEffectExtensions INSTANCE = new LockdownClientEffectExtensions();

    protected LockdownClientEffectExtensions() {}

    @Override
    public boolean renderInventoryIcon(MobEffectInstance instance, AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int blitOffset) {
        List<Map.Entry<Holder<MobEffect>, Integer>> list = Minecraft.getInstance().player.getData(BovinesAttachments.LOCKDOWN).effects().entrySet().stream().toList();
        if (!list.isEmpty()) {
            int lockdownEffectIndex = Minecraft.getInstance().player.tickCount / (160 / list.size()) % list.size();

            Holder<MobEffect> mobEffect1 = list.get(lockdownEffectIndex).getKey();

            TextureAtlasSprite additionalSprite = Minecraft.getInstance().getMobEffectTextures().get(mobEffect1);
            RenderSystem.setShaderTexture(0, additionalSprite.atlasLocation());
            guiGraphics.blitSprite(RenderType::guiTextured, additionalSprite, x, y + 7, 18, 18);
        }

        return false;
    }

    @Override
    public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
        List<Map.Entry<Holder<MobEffect>, Integer>> list = Minecraft.getInstance().player.getData(BovinesAttachments.LOCKDOWN).effects().entrySet().stream().toList();
        if (!list.isEmpty()) {
            int lockdownEffectIndex = Minecraft.getInstance().player.tickCount / (160 / list.size()) % list.size();

            Holder<MobEffect> statusEffect1 = list.get(lockdownEffectIndex).getKey();

            List<Map.Entry<Holder<MobEffect>, Integer>> runningOutEffectList = list.stream().filter(statusEffectIntegerEntry -> statusEffectIntegerEntry.getValue() <= 200).toList();

            float a = alpha;
            if (list.size() > 1 && !runningOutEffectList.isEmpty()) {
                int runningOutEffectIndex = Minecraft.getInstance().player.tickCount / (160 / runningOutEffectList.size()) % runningOutEffectList.size();

                if (!instance.isAmbient()) {
                    int duration = runningOutEffectList.get(runningOutEffectIndex).getValue();
                    int m = 10 - duration / 20;
                    a = Mth.clamp((float) duration / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos((float) duration * (float) Math.PI / 5.0f) * Mth.clamp((float) m / 10.0f * 0.25f, 0.0f, 0.25f);
                }

                statusEffect1 = runningOutEffectList.get(runningOutEffectIndex).getKey();
            }

            TextureAtlasSprite additionalSprite = Minecraft.getInstance().getMobEffectTextures().get(statusEffect1);

            int argb = ARGB.white(a);
            guiGraphics.blitSprite(RenderType::guiTextured, additionalSprite, x + 3, y + 3, 18, 18, argb);
        }
        return false;
    }
}
