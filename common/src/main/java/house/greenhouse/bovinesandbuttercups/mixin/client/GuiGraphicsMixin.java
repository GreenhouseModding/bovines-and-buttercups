package house.greenhouse.bovinesandbuttercups.mixin.client;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @ModifyVariable(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"), argsOnly = true)
    private List<Component> bovinesandbuttercups$addLockdownDescription(List<Component> list) {
        if (!list.isEmpty() && list.get(0).contains(BovinesEffects.LOCKDOWN.value().getDisplayName())) {
            List<Component> newList = new ArrayList<>(list);
            newList.remove(1);
            BovinesAndButtercups.getHelper().getLockdownAttachment(Minecraft.getInstance().player).effects().forEach(((effect, duration) -> {
                if (!effect.isBound())
                    return;
                MutableComponent component = effect.value().getDisplayName().plainCopy().append(" ");
                if (duration == -1)
                    component.append(Component.translatable("effect.duration.infinite"));
                else
                    component.append(StringUtil.formatTickDuration(duration, Minecraft.getInstance().level.tickRateManager().tickrate()));
                component.setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));
                newList.add(component);
            }));
            return newList;
        }
        return list;
    }
}
