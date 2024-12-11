package house.greenhouse.bovinesandbuttercups.content.component;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.util.TooltipUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ItemEdible(Holder<EdibleBlockType> holder, List<MobEffectEntry> effects) implements TooltipProvider {
    public static final Codec<ItemEdible> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            EdibleBlockType.CODEC.fieldOf("type").forGetter(ItemEdible::holder),
            MobEffectEntry.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(ItemEdible::effects)
    ).apply(inst, ItemEdible::new));
    public static final Codec<ItemEdible> CODEC = Codec.either(EdibleBlockType.CODEC, DIRECT_CODEC).xmap(either -> either.map(holder -> new ItemEdible(holder, List.of()), Function.identity()), edible -> {
        if (edible.effects.isEmpty())
            return Either.left(edible.holder);
        return Either.right(edible);
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemEdible> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE),
            ItemEdible::holder,
            MobEffectEntry.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ItemEdible::effects,
            ItemEdible::new
    );

    public static void apply(ItemStack stack, ItemEdible edible) {
        if (edible.holder.isBound()) {
            var dataComponentTypes = stack.getComponentsPatch().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet());
            if (!dataComponentTypes.contains(DataComponents.MAX_STACK_SIZE))
                stack.set(DataComponents.MAX_STACK_SIZE, edible.holder.value().maxStackSize());
            if (!dataComponentTypes.contains(DataComponents.ITEM_MODEL) && edible.holder.value().itemModel().isPresent())
                stack.set(DataComponents.ITEM_MODEL, edible.holder.value().itemModel().get());
            stack.set(BovinesDataComponents.EDIBLE_TYPE, edible);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof ItemEdible otherEdible))
            return false;
        return otherEdible.effects.equals(effects) && otherEdible.holder.equals(holder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holder, effects);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        for (MobEffectEntry entry : effects.stream().filter(mobEffectEntry -> mobEffectEntry.showInTooltip() != MobEffectEntry.ShowTooltip.NEVER).toList()) {
            if (!tooltipFlag.isCreative() && entry.showInTooltip() == MobEffectEntry.ShowTooltip.CREATIVE_MENU_ONLY)
                return;
            TooltipUtil.millisecondAllowedPotionTooltip(List.of(entry.effect), tooltipAdder, 1.0F, context.tickRate());
        }
    }

    public record MobEffectEntry(MobEffectInstance effect, int maxDuration, ShowTooltip showInTooltip) {
        private static final Codec<MobEffectEntry> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
                MobEffectInstance.CODEC.fieldOf("effect").forGetter(MobEffectEntry::effect),
                Codec.INT.optionalFieldOf("max_duration", Integer.MAX_VALUE).forGetter(MobEffectEntry::maxDuration),
                ShowTooltip.CODEC.optionalFieldOf("show_in_tooltip", ShowTooltip.ALWAYS).forGetter(MobEffectEntry::showInTooltip)
        ).apply(inst, MobEffectEntry::new));
        public static final Codec<MobEffectEntry> CODEC = Codec.either(DIRECT_CODEC, MobEffectInstance.CODEC).xmap(either -> either.map(mobEffectEntry -> mobEffectEntry, effectInstance -> new MobEffectEntry(effectInstance, Integer.MAX_VALUE, ShowTooltip.ALWAYS)), Either::left);

        public static final StreamCodec<RegistryFriendlyByteBuf, MobEffectEntry> STREAM_CODEC = StreamCodec.composite(
                MobEffectInstance.STREAM_CODEC,
                MobEffectEntry::effect,
                ByteBufCodecs.INT,
                MobEffectEntry::maxDuration,
                ShowTooltip.STREAM_CODEC,
                MobEffectEntry::showInTooltip,
                MobEffectEntry::new
        );

        public enum ShowTooltip implements StringRepresentable {
            NEVER("never"),
            CREATIVE_MENU_ONLY("creative_menu_only"),
            ALWAYS("always");

            public static final Codec<ShowTooltip> CODEC = StringRepresentable.fromEnum(ShowTooltip::values);
            public static final StreamCodec<ByteBuf, ShowTooltip> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

            final String name;

            ShowTooltip(String name) {
                this.name = name;
            }

            @Override
            public String getSerializedName() {
                return name;
            }
        }
    }
}
