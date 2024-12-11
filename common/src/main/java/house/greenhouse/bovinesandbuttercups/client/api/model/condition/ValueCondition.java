package house.greenhouse.bovinesandbuttercups.client.api.model.condition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelSetUtil;
import house.greenhouse.bovinesandbuttercups.content.block.PlaceableEdibleBlock;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import house.greenhouse.bovinesandbuttercups.util.IntRange;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public record ValueCondition(Optional<IntRange> bites, Map<Either<TagKey<Item>, List<ResourceKey<Item>>>, AttachmentValues> attachments) implements PlaceableEdibleCondition {
    public boolean test(PlaceableEdibleBlockEntity blockEntity) {
        var registry = blockEntity.getLevel().registryAccess().lookupOrThrow(Registries.ITEM);
        return (bites.isEmpty() || bites.get().test(blockEntity.getBlockState().getValue(PlaceableEdibleBlock.BITES))) && (attachments.isEmpty() || attachments.entrySet().stream().allMatch(entry -> {
            if (entry.getKey().map(itemTagKey -> registry.get(itemTagKey).isEmpty(), itemResourceKeys -> itemResourceKeys.stream().noneMatch(key -> registry.get(key).isEmpty())) || !blockEntity.getAttachments().containsKey(entry.getKey().map(itemTagKey -> {
                return registry.get(itemTagKey).map(holders -> (HolderSet<Item>)holders).orElseGet(HolderSet::empty);
            }, itemResourceKeys -> HolderSet.direct(itemResourceKeys.stream().map(key -> {
                return registry.get(key).orElse(null);
            }).filter(Objects::nonNull).toList()))))
                return false;
            PlaceableEdibleBlockEntity.AttachmentState state = blockEntity.getAttachments().get(entry.getKey().map(itemTagKey -> registry.getOrThrow(itemTagKey), itemResourceKeys -> HolderSet.direct(itemResourceKeys.stream().map(registry::getOrThrow).toList())));
            for (int i = 0 ; i < state.items().size(); ++i) {
                if (entry.getValue().index().isEmpty() || entry.getValue().index().get().test(i)) {
                    int finalI = i;
                    return entry.getValue().items().stream().anyMatch(stack -> ItemStack.isSameItemSameComponents(stack, state.items().get(finalI))) && (entry.getValue().active().isEmpty() || entry.getValue().active().get() == state.active());
                }
            }
            return false;
        }));
    }

    private static final Map<ValueCondition, List<List<ItemStack>>> LIST_IDS = new ConcurrentHashMap<>();

    @Override
    public String toModelVariantString() {
        List<String> strings = Lists.newArrayList();
        if (bites.isPresent()) {
            if (bites.get().min().isPresent())
                strings.add("bites.min." + bites.get().min().get());
            if (bites.get().max().isPresent())
                strings.add("bites.max." + bites.get().max().get());
        }
        if (!attachments.isEmpty()) {
            strings.add("attachments." + String.join("-", attachments.entrySet().stream().map(entry -> {
                List<String> str = Lists.newArrayList();
                str.add(entry.getKey().map(tagKey -> "tag." + tagKey.location().toString().replace(":", ".separator."), key -> "items." + String.join("-", key.stream().map(key1 -> key1.location().toString().replace(":", ".separator.")).toList())));
                str.add("stackid." + entry.getValue().getListId(this));
                if (entry.getValue().index.isPresent()) {
                    if (entry.getValue().index.get().min().isPresent())
                        str.add("index.min." + entry.getValue().index.get().min().get());
                    if (entry.getValue().index.get().max().isPresent())
                        str.add("index.max." + entry.getValue().index.get().max().get());
                }
                entry.getValue().active.ifPresent(bool -> str.add("active." + bool));
                return String.join("-", str);
            }).toList()));
        }
        return String.join("-", strings);
    }

    public record AttachmentValues(List<ItemStack> items, Optional<IntRange> index, Optional<Boolean> active) {
        public static final Codec<AttachmentValues> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                BovinesModelSetUtil.ITEM_OR_STRICT_STACK.listOf().fieldOf("items").forGetter(AttachmentValues::items),
                IntRange.codec(0, 15).optionalFieldOf("index").forGetter(AttachmentValues::index),
                Codec.BOOL.optionalFieldOf("active").forGetter(AttachmentValues::active)
        ).apply(inst, AttachmentValues::new));

        public int getListId(ValueCondition condition) {
            if (!LIST_IDS.containsKey(condition))
                LIST_IDS.compute(condition, (con, list) -> {
                    if (list == null)
                        list = new ArrayList<>();
                    list.add(items);
                    return list;
                });
            return LIST_IDS.get(condition).indexOf(items);
        }
    }
}
