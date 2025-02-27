package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public class BovinesArmorMaterials {
    public static final Holder<ArmorMaterial> FLOWER_CROWN = register(BovinesAndButtercups.asResource("flower_crown"), new ArmorMaterial(
            Map.of(),
            0,
            BovinesSoundEvents.EQUIP_FLOWER_CROWN,
            () -> Ingredient.EMPTY,
            List.of(),
            0.0F,
            0.0F
    ));

    public static void registerAll() {}

    public static Holder<ArmorMaterial> register(ResourceLocation id, ArmorMaterial material) {
        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, id, material);
    }
}
