package house.greenhouse.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomMushroom;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdible;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CreativeTabHelper {
    public static List<ItemStack> getCustomFlowersForCreativeTab(HolderLookup.Provider lookup) {
        return lookup.lookupOrThrow(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE).listElements().filter(flowerType -> flowerType.isBound() && !flowerType.value().equals(CustomFlowerType.MISSING)).map(flowerType -> {
            ItemStack stack = new ItemStack(BovinesItems.CUSTOM_FLOWER);
            stack.set(BovinesDataComponents.CUSTOM_FLOWER, new ItemCustomFlower(flowerType));
            stack.set(DataComponents.ITEM_MODEL, flowerType.value().itemModel());
            return stack;
        }).toList();
    }

    public static List<ItemStack> getCustomMushroomsForCreativeTab(HolderLookup.Provider lookup) {
        return lookup.lookupOrThrow(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE).listElements().filter(mushroomType -> mushroomType.isBound() && !mushroomType.value().equals(CustomMushroomType.MISSING)).map(mushroomType -> {
            ItemStack stack = new ItemStack(BovinesItems.CUSTOM_MUSHROOM);
            stack.set(BovinesDataComponents.CUSTOM_MUSHROOM, new ItemCustomMushroom(mushroomType));
            stack.set(DataComponents.ITEM_MODEL, mushroomType.value().itemModel());
            return stack;
        }).toList();
    }

    public static List<ItemStack> getCustomMushroomBlocksForCreativeTab(HolderLookup.Provider lookup) {
        return lookup.lookupOrThrow(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE).listElements().filter(mushroomType -> mushroomType.isBound() && !mushroomType.value().equals(CustomMushroomType.MISSING)).map(mushroomType -> {
            ItemStack stack = new ItemStack(BovinesItems.CUSTOM_MUSHROOM_BLOCK);
            stack.set(BovinesDataComponents.CUSTOM_MUSHROOM, new ItemCustomMushroom(mushroomType));
            stack.set(DataComponents.ITEM_MODEL, mushroomType.value().hugeBlockItemModel().orElse(null));
            return stack;
        }).toList();
    }

    public static List<ItemStack> getNectarBowlsForCreativeTab(HolderLookup.Provider lookup) {
        return List.of(
                BovinesItems.FREESIA_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.BIRD_OF_PARADISE_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.BUTTERCUP_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.LIMELIGHT_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.LINGHOLM_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.CHARGELILY_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.TROPICAL_BLUE_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.HYACINTH_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.PINK_DAISY_NECTAR_BOWL.getDefaultInstance(),
                BovinesItems.SNOWDROP_NECTAR_BOWL.getDefaultInstance()
        );
    }

    public static List<ItemStack> getFlowerCrownsForCreativeTab(HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<FlowerCrownMaterial> registry = lookup.lookupOrThrow(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL);
        HolderSet<FlowerCrownMaterial> creativeModeTabOrder = registry.getOrThrow(BovinesTags.FlowerCrownMaterialTags.CREATIVE_MENU_ORDER);
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(FlowerCrownItem.createRainbowCrown(lookup));
        stacks.addAll(registry.listElements().filter(Holder.Reference::isBound).sorted(Comparator.comparingInt(value -> {
            int i = creativeModeTabOrder.stream().toList().indexOf(value);
            if (i == -1)
                return Integer.MAX_VALUE;
            return i;
        })).map(petal -> {
            ItemStack stack = new ItemStack(BovinesItems.FLOWER_CROWN);
            stack.set(BovinesDataComponents.FLOWER_CROWN, new FlowerCrown(petal, petal, petal, petal, petal, petal, petal, petal));
            return stack;
        }).toList());

        return stacks;
    }

    public static void addEdibleBlocksToCreativeTabs(HolderLookup.Provider lookup, List<ItemStack> displayStacks, List<ItemStack> searchStacks, ResourceKey<CreativeModeTab> tab, AddFunction addFunction, AddFunction prependFunction, AppendFunction addBeforeFunction, AppendFunction addAfterFunction) {
        HolderLookup.RegistryLookup<EdibleBlockType> registry = lookup.lookupOrThrow(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE);
        Optional<HolderSet.Named<EdibleBlockType>> creativeModeTabOrder = registry.get(BovinesTags.EdibleBlockTypeTags.CREATIVE_MENU_ORDER);
        if (creativeModeTabOrder.isEmpty())
            return;

        Map<ItemStack, ItemStack> prependedItem = new HashMap<>();
        Map<ItemStack, ItemStack> prependedSearchItem = new HashMap<>();
        Map<ItemStack, ItemStack> appendedItem = new HashMap<>();
        Map<ItemStack, ItemStack> appendedSearchItem = new HashMap<>();

        for (var type : registry.listElements().filter(ref -> ref.isBound() && ref.value().creativeModeTabs().stream().anyMatch(creativeModeTabEntry -> {
            if (creativeModeTabEntry.tab() == tab) {
                var actualTab = lookup.lookup(Registries.CREATIVE_MODE_TAB).orElseThrow().get(tab);
                return actualTab.isPresent() && (creativeModeTabEntry.placement().stack().isEmpty() || displayStacks.stream().anyMatch(stack1 -> creativeModeTabEntry.placement().stack().get().map(stack2 -> ItemStack.isSameItemSameComponents(stack2, stack1), item -> stack1.getItem() == item)));
            }
            return false;
        })).sorted(Comparator.comparingInt(value -> {
            int i = creativeModeTabOrder.get().stream().toList().indexOf(value);
            if (i == -1)
                return Integer.MAX_VALUE;
            return i;
        })).toList()) {
            var creativeModeTab = type.value().creativeModeTabs().stream().filter(creativeModeTabEntry -> creativeModeTabEntry.tab() == tab).findFirst();

            var list = creativeModeTab.stream().flatMap(creativeModeTabEntry -> {
                    var actualTab = lookup.lookup(Registries.CREATIVE_MODE_TAB).orElseThrow().get(tab);
                    if (actualTab.isPresent() && creativeModeTabEntry.placement().stack().isPresent())
                        return displayStacks.stream().filter(stack1 -> creativeModeTabEntry.placement().stack().get().map(stack2 -> ItemStack.isSameItemSameComponents(stack2, stack1), item -> stack1.getItem() == item));
                return null;
            }).filter(Objects::nonNull).toList();

            MutableObject<Unit> hasAddedOne = new MutableObject<>();
            List<Pair<ItemStack, CreativeModeTab.TabVisibility>> items = creativeModeTab.orElseThrow().componentsToAdd().stream().map(components -> {
                ItemStack stack = new ItemStack(BovinesItems.PLACEABLE_EDIBLE);
                stack.applyComponents(components.map());
                ItemEdible.apply(stack, new ItemEdible(type, components.effects()));
                return Pair.of(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }).toList();
            hasAddedOne.setValue(null);

            if (items.isEmpty()) {
                if (type.is(BovinesTags.EdibleBlockTypeTags.IS_SUSPICIOUS)) {
                    items = SuspiciousEffectHolder.getAllEffectHolders().stream().map(suspiciousEffectHolder -> {
                        ItemStack stack = new ItemStack(BovinesItems.PLACEABLE_EDIBLE);
                        List<ItemEdible.MobEffectEntry> entries = suspiciousEffectHolder.getSuspiciousEffects().effects().stream().map(entry ->
                                new ItemEdible.MobEffectEntry(new MobEffectInstance(entry.effect(), Mth.ceil((float)entry.duration() / 4)), entry.duration(), ItemEdible.MobEffectEntry.ShowTooltip.CREATIVE_MENU_ONLY)).toList();
                        ItemEdible.apply(stack, new ItemEdible(type, entries));
                        CreativeModeTab.TabVisibility visibility = hasAddedOne.getValue() != null ? CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY : CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
                        hasAddedOne.setValue(Unit.INSTANCE);
                        return Pair.of(stack, visibility);
                    }).toList();
                } else {
                    ItemStack stack = new ItemStack(BovinesItems.PLACEABLE_EDIBLE);
                    ItemEdible.apply(stack, new ItemEdible(type, List.of()));
                    items = List.of(Pair.of(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
                }
            }

            List<Pair<ItemStack, CreativeModeTab.TabVisibility>> deduplicated = new ArrayList<>();
            for (Pair<ItemStack, CreativeModeTab.TabVisibility> stack : items) {
                if (deduplicated.stream().anyMatch(stack1 -> ItemStack.isSameItemSameComponents(stack.getFirst(), stack1.getFirst())))
                    continue;

                deduplicated.add(stack);
            }

            if (!list.isEmpty()) {
                if (creativeModeTab.get().placement().ordering() == CreativeModeTabEntry.Ordering.AFTER) {
                    for (Pair<ItemStack, CreativeModeTab.TabVisibility> stackToAdd : deduplicated) {
                        Map<ItemStack, ItemStack> map = stackToAdd.getSecond() == CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY ? prependedSearchItem : prependedItem;
                        if (!map.containsKey(list.getLast()))
                            addAfterFunction.accept(list.getLast(), stackToAdd.getFirst(), stackToAdd.getSecond());
                        else
                            addAfterFunction.accept(map.get(list.getLast()), stackToAdd.getFirst(), stackToAdd.getSecond());
                        map.put(list.getLast(), stackToAdd.getFirst());
                        if (stackToAdd.getSecond() == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS && searchStacks.contains(list.getLast()))
                            prependedSearchItem.put(list.getLast(), stackToAdd.getFirst());
                    }
                } else {
                    for (Pair<ItemStack, CreativeModeTab.TabVisibility> stackToAdd : deduplicated) {
                        Map<ItemStack, ItemStack> map = stackToAdd.getSecond() == CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY ? appendedSearchItem : appendedItem;
                        if (!map.containsKey(list.getFirst()))
                            addBeforeFunction.accept(list.getFirst(), stackToAdd.getFirst(), stackToAdd.getSecond());
                        else
                            addAfterFunction.accept(map.get(list.getFirst()), stackToAdd.getFirst(), stackToAdd.getSecond());
                        map.put(list.getFirst(), stackToAdd.getFirst());
                        if (stackToAdd.getSecond() == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS && searchStacks.contains(list.getFirst()))
                            appendedSearchItem.put(list.getFirst(), stackToAdd.getFirst());
                    }
                }
            } else if (creativeModeTab.get().placement().ordering() == CreativeModeTabEntry.Ordering.BEFORE)
                for (Pair<ItemStack, CreativeModeTab.TabVisibility> stackToAdd : deduplicated) {
                    addFunction.accept(stackToAdd.getFirst(), stackToAdd.getSecond());
                }
            else
                for (Pair<ItemStack, CreativeModeTab.TabVisibility> stackToAdd : deduplicated.reversed()) {
                    addFunction.accept(stackToAdd.getFirst(), stackToAdd.getSecond());
                }
        }
    }

    @FunctionalInterface
    public interface AddFunction {
        void accept(ItemStack newStack, CreativeModeTab.TabVisibility visibility);
    }

    @FunctionalInterface
    public interface AppendFunction {
        void accept(ItemStack existingStack, ItemStack newStack, CreativeModeTab.TabVisibility visibility);
    }
}