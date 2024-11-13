package house.greenhouse.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdible;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record CreativeModeTabEntry(ResourceKey<CreativeModeTab> tab, List<ComponentsEntry> componentsToAdd, PlacementEntry placement) {
    public static final Codec<CreativeModeTabEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceKey.codec(Registries.CREATIVE_MODE_TAB).fieldOf("tab").forGetter(CreativeModeTabEntry::tab),
            ComponentsEntry.CODEC.listOf().optionalFieldOf("item_components", List.of()).forGetter(CreativeModeTabEntry::componentsToAdd),
            PlacementEntry.CODEC.optionalFieldOf("placement", PlacementEntry.LAST).forGetter(CreativeModeTabEntry::placement)
    ).apply(inst, CreativeModeTabEntry::new));

    public record PlacementEntry(Optional<Either<ItemStack, Item>> stack, Ordering ordering) {
        public static final PlacementEntry LAST = new PlacementEntry(Optional.empty(), Ordering.AFTER);

        public static final Codec<PlacementEntry> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.either(ItemStack.STRICT_SINGLE_ITEM_CODEC, BuiltInRegistries.ITEM.byNameCodec()).optionalFieldOf("stack").forGetter(PlacementEntry::stack),
                Ordering.CODEC.optionalFieldOf("ordering", Ordering.AFTER).forGetter(PlacementEntry::ordering)
        ).apply(inst, PlacementEntry::new));

        public static final Codec <PlacementEntry> CODEC = Codec.either(Codec.either(ItemStack.STRICT_SINGLE_ITEM_CODEC, BuiltInRegistries.ITEM.byNameCodec()), DIRECT_CODEC).xmap(either -> either.map(stack -> new PlacementEntry(Optional.of(stack), Ordering.AFTER), afterEntry -> afterEntry), Either::right);
    }
    public record ComponentsEntry(DataComponentMap map, List<ItemEdible.MobEffectEntry> effects) {
        public static final Codec<ComponentsEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                DataComponentMap.CODEC.optionalFieldOf("components", DataComponentMap.EMPTY).forGetter(ComponentsEntry::map),
                ItemEdible.MobEffectEntry.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(ComponentsEntry::effects)
        ).apply(inst, ComponentsEntry::new));
    }

    public enum Ordering implements StringRepresentable {
        BEFORE("before"),
        AFTER("after");

        public static final Codec<Ordering> CODEC = StringRepresentable.fromEnum(Ordering::values);

        final String name;

        Ordering(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
