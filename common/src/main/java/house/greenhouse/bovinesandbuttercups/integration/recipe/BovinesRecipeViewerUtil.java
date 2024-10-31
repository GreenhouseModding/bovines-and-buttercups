package house.greenhouse.bovinesandbuttercups.integration.recipe;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.BovinesFlowerCrownMaterials;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class BovinesRecipeViewerUtil {
    private static final ResourceKey<FlowerCrownMaterial> MONOCOLOR = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("monocolor"));
    private static final ResourceKey<FlowerCrownMaterial> RANDOM_TWO = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("random_two"));
    private static final ResourceKey<FlowerCrownMaterial> RANDOM_FOUR = ResourceKey.create(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesAndButtercups.asResource("random_four"));
    private static final List<List<ResourceKey<FlowerCrownMaterial>>> COMBINATIONS = List.of(
            List.of(MONOCOLOR),
            List.of(RANDOM_TWO),
            List.of(RANDOM_FOUR),
            List.of(
                    BovinesFlowerCrownMaterials.FREESIA,
                    BovinesFlowerCrownMaterials.BIRD_OF_PARADISE,
                    BovinesFlowerCrownMaterials.BUTTERCUP,
                    BovinesFlowerCrownMaterials.LIMELIGHT,
                    BovinesFlowerCrownMaterials.CHARGELILY,
                    BovinesFlowerCrownMaterials.TROPICAL_BLUE,
                    BovinesFlowerCrownMaterials.HYACINTH,
                    BovinesFlowerCrownMaterials.PINK_DAISY
            ),
            List.of(
                    BovinesFlowerCrownMaterials.CHARGELILY,
                    BovinesFlowerCrownMaterials.PINK_DAISY,
                    BovinesFlowerCrownMaterials.SNOWDROP,
                    BovinesFlowerCrownMaterials.PINK_DAISY,
                    BovinesFlowerCrownMaterials.CHARGELILY,
                    BovinesFlowerCrownMaterials.PINK_DAISY,
                    BovinesFlowerCrownMaterials.SNOWDROP,
                    BovinesFlowerCrownMaterials.PINK_DAISY
            )
    );

    public static ItemStack generateRandomFlowerCrown(Random random, Item item) {
        var registry = Minecraft.getInstance().level.registryAccess().registryOrThrow(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL);
        ItemStack stack = new ItemStack(item);
        if (registry.holders().findAny().isEmpty())
            return stack;

        List<ResourceKey<FlowerCrownMaterial>> materials = COMBINATIONS.get(random.nextInt(COMBINATIONS.size()));

        Holder<FlowerCrownMaterial> centerLeft = registry.holders().findFirst().orElseThrow();
        Holder<FlowerCrownMaterial> topLeft = centerLeft;
        Holder<FlowerCrownMaterial> top = topLeft;
        Holder<FlowerCrownMaterial> topRight = topLeft;
        Holder<FlowerCrownMaterial> centerRight = topLeft;
        Holder<FlowerCrownMaterial> bottomLeft = topLeft;
        Holder<FlowerCrownMaterial> bottom = topLeft;
        Holder<FlowerCrownMaterial> bottomRight = topLeft;

        if (materials.size() == 1) {
            if (materials.getFirst().equals(MONOCOLOR)) {
                centerLeft = registry.getHolder(random.nextInt(registry.size())).orElseThrow();
                topLeft = centerLeft;
                top = centerLeft;
                topRight = centerLeft;
                centerRight = centerLeft;
                bottomRight = centerLeft;
                bottom = centerLeft;
                bottomLeft = centerLeft;
            } else if (materials.getFirst().equals(RANDOM_TWO)) {
                centerLeft = registry.getHolder(random.nextInt(registry.size())).orElseThrow();
                Holder<FlowerCrownMaterial> finalCenterLeft = centerLeft;
                List<Holder.Reference<FlowerCrownMaterial>> topLeftMaterials = registry.holders().filter(reference -> !reference.is(finalCenterLeft)).toList();
                topLeft = topLeftMaterials.get(random.nextInt(topLeftMaterials.size()));
                top = centerLeft;
                topRight = topLeft;
                centerRight = centerLeft;
                bottomRight = topLeft;
                bottom = centerLeft;
                bottomLeft = topLeft;
            } else if (materials.getFirst().equals(RANDOM_FOUR)) {
                centerLeft = registry.getHolder(random.nextInt(registry.size())).orElseThrow();
                Holder<FlowerCrownMaterial> finalCenterLeft = centerLeft;
                List<Holder.Reference<FlowerCrownMaterial>> topLeftMaterials = registry.holders().filter(reference -> !reference.is(finalCenterLeft)).toList();
                topLeft = topLeftMaterials.get(random.nextInt(topLeftMaterials.size()));
                Holder<FlowerCrownMaterial> finalTopLeft = topLeft;
                List<Holder.Reference<FlowerCrownMaterial>> topMaterials = registry.holders().filter(reference -> !reference.is(finalCenterLeft) && !reference.is(finalTopLeft)).toList();
                top = topMaterials.get(random.nextInt(topMaterials.size()));
                Holder<FlowerCrownMaterial> finalTop = top;
                List<Holder.Reference<FlowerCrownMaterial>> topRightMaterials = registry.holders().filter(reference -> !reference.is(finalCenterLeft) && !reference.is(finalTopLeft) && !reference.is(finalTop)).toList();
                topRight = topRightMaterials.get(random.nextInt(topRightMaterials.size()));
                centerRight = centerLeft;
                bottomRight = topLeft;
                bottom = top;
                bottomLeft = topRight;
            }
        } else if (materials.size() == 8) {
            centerLeft = registry.getHolderOrThrow(materials.get(0));
            topLeft = registry.getHolderOrThrow(materials.get(1));
            top = registry.getHolderOrThrow(materials.get(2));
            topRight = registry.getHolderOrThrow(materials.get(3));
            centerRight = registry.getHolderOrThrow(materials.get(4));
            bottomRight = registry.getHolderOrThrow(materials.get(5));
            bottom = registry.getHolderOrThrow(materials.get(6));
            bottomLeft = registry.getHolderOrThrow(materials.get(7));
        } else {
            return stack;
        }

        stack.set(BovinesDataComponents.FLOWER_CROWN, new FlowerCrown(
                topLeft,
                top,
                topRight,
                centerLeft,
                centerRight,
                bottomLeft,
                bottom,
                bottomRight)
        );
        return stack;
    }
}
