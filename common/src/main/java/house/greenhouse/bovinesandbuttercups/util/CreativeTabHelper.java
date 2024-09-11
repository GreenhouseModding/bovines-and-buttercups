package house.greenhouse.bovinesandbuttercups.util;

import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomMushroom;
import house.greenhouse.bovinesandbuttercups.content.component.ItemNectar;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.Nectar;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.registry.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.registry.BovinesFlowerCrownMaterials;
import house.greenhouse.bovinesandbuttercups.registry.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesNectars;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreativeTabHelper {
    public static List<ItemStack> getCustomFlowersForCreativeTab(HolderLookup.Provider lookup) {
        return lookup.lookupOrThrow(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE).listElements().filter(flowerType -> flowerType.isBound() && !flowerType.value().equals(CustomFlowerType.MISSING)).map(flowerType -> {
            ItemStack stack = new ItemStack(BovinesItems.CUSTOM_FLOWER);
            stack.set(BovinesDataComponents.CUSTOM_FLOWER, new ItemCustomFlower(flowerType));
            return stack;
        }).toList();
    }

    public static List<ItemStack> getCustomMushroomsForCreativeTab(HolderLookup.Provider lookup) {
        return lookup.lookupOrThrow(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE).listElements().filter(mushroomType -> mushroomType.isBound() && !mushroomType.value().equals(CustomMushroomType.MISSING)).map(mushroomType -> {
            ItemStack stack = new ItemStack(BovinesItems.CUSTOM_MUSHROOM);
            stack.set(BovinesDataComponents.CUSTOM_MUSHROOM, new ItemCustomMushroom(mushroomType));
            return stack;
        }).toList();
    }

    public static List<ItemStack> getCustomMushroomBlocksForCreativeTab(HolderLookup.Provider lookup) {
        return lookup.lookupOrThrow(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE).listElements().filter(mushroomType -> mushroomType.isBound() && !mushroomType.value().equals(CustomMushroomType.MISSING)).map(mushroomType -> {
            ItemStack stack = new ItemStack(BovinesItems.CUSTOM_MUSHROOM_BLOCK);
            stack.set(BovinesDataComponents.CUSTOM_MUSHROOM, new ItemCustomMushroom(mushroomType));
            return stack;
        }).toList();
    }

    public static List<ItemStack> getNectarBowlsForCreativeTab(HolderLookup.Provider lookup) {
        HolderSet<Nectar> creativeModeTabOrder = lookup.lookupOrThrow(BovinesRegistryKeys.NECTAR).getOrThrow(BovinesTags.NectarTags.CREATIVE_MENU_ORDER);
        return lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).listElements().filter(cowType -> cowType.isBound() && cowType.value().configuration() instanceof MoobloomConfiguration && ((MoobloomConfiguration) cowType.value().configuration()).nectar().isPresent()).map(cowType -> {
            ItemStack stack = new ItemStack(BovinesItems.NECTAR_BOWL);
            stack.set(BovinesDataComponents.NECTAR, new ItemNectar(((MoobloomConfiguration) cowType.value().configuration()).nectar().get()));
            return stack;
        }).sorted(Comparator.comparingInt(value -> {
            int i = creativeModeTabOrder.stream().toList().indexOf(value.get(BovinesDataComponents.NECTAR).nectar());
            if (i == -1)
                return Integer.MAX_VALUE;
            return i;
        })).toList();
    }

    public static List<ItemStack> getFlowerCrownsForCreativeTab(HolderLookup.Provider lookup) {
        HolderLookup.RegistryLookup<FlowerCrownMaterial> registry = lookup.lookupOrThrow(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL);
        HolderSet<FlowerCrownMaterial> creativeModeTabOrder = registry.getOrThrow(BovinesTags.FlowerCrownMaterialTags.CREATIVE_MENU_ORDER);
        List<ItemStack> stacks = registry.listElements().filter(Holder.Reference::isBound).sorted(Comparator.comparingInt(value -> {
            int i = creativeModeTabOrder.stream().toList().indexOf(value);
            if (i == -1)
                return Integer.MAX_VALUE;
            return i;
        })).map(petal -> {
            ItemStack stack = new ItemStack(BovinesItems.FLOWER_CROWN);
            stack.set(BovinesDataComponents.FLOWER_CROWN, new FlowerCrown(petal, petal, petal, petal, petal, petal, petal, petal));
            return stack;
        }).collect(Collectors.toCollection(ArrayList::new));

        stacks.add(FlowerCrownItem.createRainbowCrown(lookup));

        return stacks;
    }
}
