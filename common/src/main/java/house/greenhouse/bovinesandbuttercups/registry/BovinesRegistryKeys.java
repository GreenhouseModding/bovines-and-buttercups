package house.greenhouse.bovinesandbuttercups.registry;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BovinesRegistryKeys {
    public static final ResourceKey<Registry<CowTypeType<?>>> COW_TYPE_TYPE = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("cow_type_type"));
    public static final ResourceKey<Registry<MapCodec<? extends TextureModifierFactory<?>>>> TEXTURE_MODIFIER = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("texture_modifier"));

    public static final ResourceKey<Registry<CowType<?>>> COW_TYPE = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("cow_type"));
    public static final ResourceKey<Registry<CustomFlowerType>> CUSTOM_FLOWER_TYPE = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("flower_type"));
    public static final ResourceKey<Registry<CustomMushroomType>> CUSTOM_MUSHROOM_TYPE = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("mushroom_type"));
    public static final ResourceKey<Registry<EdibleBlockType>> EDIBLE_BLOCK_TYPE = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("edible_block"));
    public static final ResourceKey<Registry<FlowerCrownMaterial>> FLOWER_CROWN_MATERIAL = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("flower_crown_material"));
}
