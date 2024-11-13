package house.greenhouse.bovinesandbuttercups.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.access.MobEffectInstanceLockdownDataAccess;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class TooltipUtil {
    public static void millisecondAllowedPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> tooltipAdder, float durationFactor, float ticksPerSecond) {
        List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
        boolean flag = true;

        for (MobEffectInstance effectInstance : effects) {
            flag = false;
            MutableComponent mutablecomponent = Component.translatable(effectInstance.getDescriptionId());
            Holder<MobEffect> holder = effectInstance.getEffect();
            holder.value().createModifiers(effectInstance.getAmplifier(), (p_331556_, p_330860_) -> list.add(new Pair<>(p_331556_, p_330860_)));

            if (effectInstance.is(BovinesEffects.LOCKDOWN)) {
                List<LockdownData> dataList =  ((MobEffectInstanceLockdownDataAccess)effectInstance).bovinesandbuttercups$getLockdownData();
                for (LockdownData data : dataList) {
                    MobEffectInstance dataInstance = new MobEffectInstance(data.linkedEffect(), data.duration().orElse(effectInstance.getDuration()));
                    tooltipAdder.accept(Component.translatable("potion.bovinesandbuttercups.lockdown", Component.translatable(dataInstance.getDescriptionId()), formatDuration(dataInstance, durationFactor, ticksPerSecond)).withStyle(effectInstance.getEffect().value().getCategory().getTooltipFormatting()));
                }
                continue;
            }

            if (effectInstance.getAmplifier() > 0) {
                mutablecomponent = Component.translatable(
                        "potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + effectInstance.getAmplifier())
                );
            }

            mutablecomponent = Component.translatable(
                    "potion.withDuration", mutablecomponent, formatDuration(effectInstance, durationFactor, ticksPerSecond)
            );

            tooltipAdder.accept(mutablecomponent.withStyle(holder.value().getCategory().getTooltipFormatting()));
        }

        if (flag) {
            tooltipAdder.accept(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
        }

        if (!list.isEmpty()) {
            tooltipAdder.accept(CommonComponents.EMPTY);
            tooltipAdder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

            for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                AttributeModifier attributemodifier = pair.getSecond();
                double d1 = attributemodifier.amount();
                double d0;
                if (attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    d0 = attributemodifier.amount();
                } else {
                    d0 = attributemodifier.amount() * 100.0;
                }

                if (d1 > 0.0) {
                    tooltipAdder.accept(
                            Component.translatable(
                                            "attribute.modifier.plus." + attributemodifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d0),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.BLUE)
                    );
                } else if (d1 < 0.0) {
                    d0 *= -1.0;
                    tooltipAdder.accept(
                            Component.translatable(
                                            "attribute.modifier.take." + attributemodifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d0),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.RED)
                    );
                }
            }
        }
    }

    public static Component formatDuration(MobEffectInstance effect, float durationFactor, float ticksPerSecond) {
        if (effect.isInfiniteDuration()) {
            return Component.translatable("effect.duration.infinite");
        } else {
            int i = Mth.floor((float)effect.getDuration() * durationFactor);
            return Component.literal(i < 20 ? formatMilliseconds(i, ticksPerSecond) : StringUtil.formatTickDuration(i, ticksPerSecond));
        }
    }

    public static String formatMilliseconds(int ticks, float ticksPerSecond) {
        int m = (ticks % 20) * 50;
        int i = Mth.floor((float)ticks / ticksPerSecond);
        int j = i / 60;
        i %= 60;
        return String.format(Locale.ROOT, "%02d:%02d:%02d", j, i, m);
    }
}
