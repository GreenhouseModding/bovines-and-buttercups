package house.greenhouse.bovinesandbuttercups.content.item;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.registry.HolderRegistrationCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public class BovinesArmorMaterials {
    public static Holder<ArmorMaterial> FLOWER_CROWN;

    public static void registerAll(HolderRegistrationCallback<ArmorMaterial> callback) {
        FLOWER_CROWN = callback.register(BuiltInRegistries.ARMOR_MATERIAL, BovinesAndButtercups.asResource("flower_crown"), new ArmorMaterial(
                Map.of(),
                0,
                BovinesSoundEvents.EQUIP_FLOWER_CROWN,
                () -> Ingredient.EMPTY,
                List.of(),
                0.0F,
                0.0F
        ));
    }
}
