package house.greenhouse.bovinesandbuttercups.content.component;

import com.mojang.serialization.Codec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.Nectar;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.NectarEffects;
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

public record ItemNectar(Holder<Nectar> nectar) implements TooltipProvider {
    public static final ItemNectar EMPTY = new ItemNectar(Holder.direct(new Nectar(BovinesAndButtercups.asResource("bovinesandbuttercups/item/buttercup_nectar_bowl"), NectarEffects.EMPTY)));
    public static final Codec<ItemNectar> CODEC = Nectar.CODEC.xmap(ItemNectar::new, ItemNectar::nectar);

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemNectar> STREAM_CODEC = Nectar.STREAM_CODEC.map(ItemNectar::new, ItemNectar::nectar);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> component, TooltipFlag flag) {
        for (NectarEffects.Entry entry : nectar.value().effects().effects()) {
            MobEffectInstance instance = new MobEffectInstance(entry.effect(), entry.duration());
            component.accept(Component.translatable("bovinesandbuttercups.nectarBowl.effect", Component.translatable(instance.getDescriptionId()), MobEffectUtil.formatDuration(instance, 1.0F, context.tickRate())).withStyle(ChatFormatting.BLUE));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof ItemNectar nectar))
            return false;
        return nectar.nectar.equals(nectar);
    }

    @Override
    public int hashCode() {
        return nectar.hashCode();
    }
}
