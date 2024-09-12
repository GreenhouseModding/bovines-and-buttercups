package house.greenhouse.bovinesandbuttercups.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BovinesConventionalTags {
    public static class ConventionalItemTags {
        public static final TagKey<Item> HONEY_FOODS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/honey"));
    }
}
