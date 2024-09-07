package house.greenhouse.bovinesandbuttercups.content.recipe;

import com.google.common.collect.Lists;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownPetal;
import house.greenhouse.bovinesandbuttercups.registry.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.registry.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRecipeSerializers;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class FlowerCrownRecipe extends CustomRecipe {
    private static final NonNullList<Optional<Unit>> SHAPE = NonNullList.of(Optional.empty(),
            Optional.of(Unit.INSTANCE), Optional.of(Unit.INSTANCE), Optional.of(Unit.INSTANCE),
            Optional.of(Unit.INSTANCE), Optional.empty(), Optional.of(Unit.INSTANCE),
            Optional.of(Unit.INSTANCE), Optional.of(Unit.INSTANCE), Optional.of(Unit.INSTANCE));

    public FlowerCrownRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < input.height(); i++) {
            for (int j = 0; j < input.width(); j++) {
                Optional<Unit> hasItem = SHAPE.get(j + i * input.width());
                ItemStack stack = input.getItem(j, i);
                if (hasItem.isEmpty() && !stack.isEmpty() || hasItem.isPresent() && level.registryAccess().registry(BovinesRegistryKeys.FLOWER_CROWN_PETAL).orElseThrow().stream().noneMatch(petal -> ItemStack.isSameItemSameComponents(petal.ingredient(), stack))) {
                    return false;
                }
                if (hasItem.isEmpty())
                    continue;
                list.add(stack);
            }
        }

        return list.size() == 8;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        List<Holder<FlowerCrownPetal>> list = Lists.newArrayList();

        for (int i = 0; i < input.size(); i++) {
            if (list.size() == 8)
                break;
            ItemStack inputStack = input.getItem(i);
            if (!inputStack.isEmpty()) {
                Optional<Holder.Reference<FlowerCrownPetal>> petal = registries.lookupOrThrow(BovinesRegistryKeys.FLOWER_CROWN_PETAL).listElements().filter(flowerCrownPetal -> flowerCrownPetal.isBound() && ItemStack.isSameItemSameComponents(flowerCrownPetal.value().ingredient(), inputStack)).findFirst();
                if (petal.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                list.add(petal.get());
            }
        }

        if (list.size() == 8) {
            ItemStack crownStack = new ItemStack(BovinesItems.FLOWER_CROWN);
            crownStack.set(BovinesDataComponents.FLOWER_CROWN, assembleFlowerCrown(list));
            return crownStack;
        }
        return ItemStack.EMPTY;
    }

    private static FlowerCrown assembleFlowerCrown(List<Holder<FlowerCrownPetal>> petals) {
        return new FlowerCrown(petals.get(0), petals.get(1), petals.get(2), petals.get(3), petals.get(4), petals.get(5), petals.get(6), petals.get(7));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= SHAPE.size();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BovinesRecipeSerializers.FLOWER_CROWN;
    }
}
