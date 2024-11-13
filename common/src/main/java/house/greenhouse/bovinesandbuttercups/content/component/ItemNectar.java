package house.greenhouse.bovinesandbuttercups.content.component;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.Nectar;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.NectarEffects;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record ItemNectar(Holder<Nectar> holder) implements TooltipProvider {
    public static final ItemNectar EMPTY = new ItemNectar(Holder.direct(new Nectar(BovinesAndButtercups.asResource("bovinesandbuttercups/item/buttercup_nectar_bowl"), NectarEffects.EMPTY)));
    public static final Codec<ItemNectar> CODEC = Nectar.CODEC.xmap(ItemNectar::new, ItemNectar::holder);

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemNectar> STREAM_CODEC = Nectar.STREAM_CODEC.map(ItemNectar::new, ItemNectar::holder);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> component, TooltipFlag flag) {
        for (NectarEffects.Entry entry : holder.value().effects().effects()) {
            MobEffectInstance instance = new MobEffectInstance(entry.effect(), entry.duration());
            component.accept(Component.translatable("potion.bovinesandbuttercups.lockdown", Component.translatable(instance.getDescriptionId()), MobEffectUtil.formatDuration(instance, 1.0F, context.tickRate())).withStyle(BovinesEffects.LOCKDOWN.value().getCategory().getTooltipFormatting()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof ItemNectar nectar))
            return false;
        return nectar.holder.equals(holder);
    }

    @Override
    public int hashCode() {
        return holder.hashCode();
    }
}
