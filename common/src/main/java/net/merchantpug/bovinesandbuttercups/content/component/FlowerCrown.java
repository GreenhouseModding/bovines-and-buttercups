package net.merchantpug.bovinesandbuttercups.content.component;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.merchantpug.bovinesandbuttercups.content.data.flowercrown.FlowerCrownPetal;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public record FlowerCrown(Holder<FlowerCrownPetal> topLeft, Holder<FlowerCrownPetal> top, Holder<FlowerCrownPetal> topRight,
                          Holder<FlowerCrownPetal> centerLeft, Holder<FlowerCrownPetal> centerRight,
                          Holder<FlowerCrownPetal> bottomLeft, Holder<FlowerCrownPetal> bottom, Holder<FlowerCrownPetal> bottomRight) implements TooltipProvider {
    public static final Codec<FlowerCrown> LIST_CODEC = FlowerCrownPetal.CODEC.listOf(8, 8).comapFlatMap(holders -> DataResult.success(new FlowerCrown(holders.get(1), holders.get(2), holders.get(3), holders.get(0), holders.get(4), holders.get(7), holders.get(6), holders.get(5))), flowerCrown -> List.of(flowerCrown.topLeft, flowerCrown.top, flowerCrown.topRight, flowerCrown.centerLeft, flowerCrown.centerRight, flowerCrown.bottomLeft, flowerCrown.bottom, flowerCrown.bottomRight));
    public static final Codec<FlowerCrown> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FlowerCrownPetal.CODEC.fieldOf("top_left").forGetter(FlowerCrown::topLeft),
            FlowerCrownPetal.CODEC.fieldOf("top").forGetter(FlowerCrown::top),
            FlowerCrownPetal.CODEC.fieldOf("top_right").forGetter(FlowerCrown::topRight),
            FlowerCrownPetal.CODEC.fieldOf("center_left").forGetter(FlowerCrown::centerLeft),
            FlowerCrownPetal.CODEC.fieldOf("center_right").forGetter(FlowerCrown::centerRight),
            FlowerCrownPetal.CODEC.fieldOf("bottom_left").forGetter(FlowerCrown::bottomLeft),
            FlowerCrownPetal.CODEC.fieldOf("bottom").forGetter(FlowerCrown::bottom),
            FlowerCrownPetal.CODEC.fieldOf("bottom_right").forGetter(FlowerCrown::bottomRight)
    ).apply(inst, FlowerCrown::new));
    public static final Codec<FlowerCrown> CODEC = Codec.either(DIRECT_CODEC, LIST_CODEC).comapFlatMap(e -> DataResult.success(e.map(flowerCrown -> flowerCrown, flowerCrown -> flowerCrown)), Either::left);
    public static final StreamCodec<RegistryFriendlyByteBuf, FlowerCrown> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FlowerCrown decode(RegistryFriendlyByteBuf buffer) {
            return new FlowerCrown(
                    FlowerCrownPetal.STREAM_CODEC.decode(buffer),
                    FlowerCrownPetal.STREAM_CODEC.decode(buffer),
                    FlowerCrownPetal.STREAM_CODEC.decode(buffer),
                    FlowerCrownPetal.STREAM_CODEC.decode(buffer),
                    FlowerCrownPetal.STREAM_CODEC.decode(buffer),
                    FlowerCrownPetal.STREAM_CODEC.decode(buffer),
                    FlowerCrownPetal.STREAM_CODEC.decode(buffer),
                    FlowerCrownPetal.STREAM_CODEC.decode(buffer)
            );
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, FlowerCrown value) {
            FlowerCrownPetal.STREAM_CODEC.encode(buffer, value.topLeft());
            FlowerCrownPetal.STREAM_CODEC.encode(buffer, value.top());
            FlowerCrownPetal.STREAM_CODEC.encode(buffer, value.topRight());
            FlowerCrownPetal.STREAM_CODEC.encode(buffer, value.centerLeft());
            FlowerCrownPetal.STREAM_CODEC.encode(buffer, value.centerRight());
            FlowerCrownPetal.STREAM_CODEC.encode(buffer, value.bottomLeft());
            FlowerCrownPetal.STREAM_CODEC.encode(buffer, value.bottom());
            FlowerCrownPetal.STREAM_CODEC.encode(buffer, value.bottomRight());
        }
    };

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FlowerCrown flowerCrown))
            return false;
        return flowerCrown.topLeft.equals(topLeft) && flowerCrown.top.equals(top) && flowerCrown.topRight.equals(topRight)
                && flowerCrown.centerLeft.equals(centerLeft) && flowerCrown.centerRight.equals(centerRight)
                && flowerCrown.bottomLeft.equals(bottomLeft) && flowerCrown.bottom.equals(bottom) && flowerCrown.bottomRight.equals(bottomRight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topLeft, top, topRight, centerLeft, centerRight, bottomLeft, bottom, bottomRight);
    }

    public ResourceLocation getEquippedTexture(int i) {
        return Stream.of(centerLeft, topLeft, top, topRight, centerRight, bottomRight, bottom, bottomLeft).map(holder -> holder.value().equippedTextures().get(i)).toList().get(i);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        var amountMap = collectAmount();
        for (Map.Entry<Holder<FlowerCrownPetal>, Integer> entry : amountMap.entrySet()) {
            tooltipAdder.accept(Component.translatable("item.bovinesandbuttercups.flower_crown.petal_amount", entry.getKey().value().description(), entry.getValue()));
        }
    }

    private Object2IntArrayMap<Holder<FlowerCrownPetal>> collectAmount() {
        Object2IntArrayMap<Holder<FlowerCrownPetal>> map = new Object2IntArrayMap<>();
        // Goes in a clockwise motion, starting from the center left.
        map.compute(centerLeft, (holder, integer) -> integer == null ? 1 : integer + 1);
        map.compute(topLeft, (holder, integer) -> integer == null ? 1 : integer + 1);
        map.compute(top, (holder, integer) -> integer == null ? 1 : integer + 1);
        map.compute(topRight, (holder, integer) -> integer == null ? 1 : integer + 1);
        map.compute(centerRight, (holder, integer) -> integer == null ? 1 : integer + 1);
        map.compute(bottomRight, (holder, integer) -> integer == null ? 1 : integer + 1);
        map.compute(bottom, (holder, integer) -> integer == null ? 1 : integer + 1);
        map.compute(bottomLeft, (holder, integer) -> integer == null ? 1 : integer + 1);
        return map;
    }
}
