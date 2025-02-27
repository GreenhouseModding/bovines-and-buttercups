package house.greenhouse.bovinesandbuttercups.registry;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.BovinesCriteriaTriggers;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.block.entity.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.BovinesTextureModifierFactories;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.Nectar;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesArmorMaterials;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.particle.BovinesParticleTypes;
import house.greenhouse.bovinesandbuttercups.content.predicate.BovinesEntitySubPredicateTypes;
import house.greenhouse.bovinesandbuttercups.content.predicate.BovinesLootItemConditionTypes;
import house.greenhouse.bovinesandbuttercups.content.recipe.BovinesRecipeSerializers;
import house.greenhouse.bovinesandbuttercups.content.recipe.ingredient.BovinesIngredients;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.content.worldgen.BovinesBiomeModifierSerializers;
import house.greenhouse.bovinesandbuttercups.content.worldgen.BovinesStructureTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.*;

import java.util.function.Consumer;

@EventBusSubscriber(modid = BovinesAndButtercups.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BovinesRegistryEvents {
    @SubscribeEvent
    public static void registerContent(RegisterEvent event) {
        register(event, NeoForgeRegistries.Keys.ATTACHMENT_TYPES, BovinesAttachments::registerAll);
        register(event, NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, BovinesBiomeModifierSerializers::registerAll);
        register(event, Registries.BLOCK_ENTITY_TYPE, BovinesBlockEntityTypes::registerAll);
        register(event, Registries.BLOCK, BovinesBlocks::registerAll);
        register(event, BovinesRegistryKeys.COW_TYPE, BovinesCowTypes::registerAll);
        register(event, Registries.TRIGGER_TYPE, BovinesCriteriaTriggers::registerAll);
        register(event, Registries.DATA_COMPONENT_TYPE, BovinesDataComponents::registerAll);
        register(event, Registries.ENTITY_SUB_PREDICATE_TYPE, BovinesEntitySubPredicateTypes::registerAll);
        register(event, Registries.ENTITY_TYPE, BovinesEntityTypes::registerAll);
        register(event, NeoForgeRegistries.Keys.INGREDIENT_TYPES, BovinesIngredients::registerAll);
        register(event, Registries.ARMOR_MATERIAL, BovinesArmorMaterials::registerAll);
        register(event, Registries.ARMOR_MATERIAL, BovinesSoundEvents::registerAll);
        register(event, Registries.ARMOR_MATERIAL, BovinesItems::registerAll);
        register(event, Registries.MOB_EFFECT, BovinesEffects::registerAll);
        register(event, Registries.LOOT_CONDITION_TYPE, BovinesLootItemConditionTypes::registerAll);
        register(event, Registries.PARTICLE_TYPE, BovinesParticleTypes::registerAll);
        register(event, Registries.RECIPE_SERIALIZER, BovinesRecipeSerializers::registerAll);
        register(event, Registries.STRUCTURE_TYPE, BovinesStructureTypes::registerAll);
        register(event, BovinesRegistryKeys.TEXTURE_MODIFIER, BovinesTextureModifierFactories::registerAll);
        register(event, BovinesRegistryKeys.MODEL_TYPE, BovinesCowModelTypes::registerAll);;
    }

    private static <T> void register(RegisterEvent event, ResourceKey<T> registerAt, Runnable runnable) {
        if (event.getRegistryKey() == registerAt)
            runnable.run();
    }

    @SubscribeEvent
    public static void createNewRegistries(NewRegistryEvent event) {
        event.register(BovinesRegistries.COW_TYPE);
        event.register(BovinesRegistries.MODEL_TYPE);
        event.register(BovinesRegistries.TEXTURE_MODIFIER);
    }

    @SubscribeEvent
    public static void createNewDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(BovinesRegistryKeys.COW_VARIANT, CowVariant.DIRECT_CODEC, CowVariant.DIRECT_CODEC);
        event.dataPackRegistry(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE, CustomFlowerType.DIRECT_CODEC, CustomFlowerType.DIRECT_CODEC);
        event.dataPackRegistry(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE, CustomMushroomType.DIRECT_CODEC, CustomMushroomType.DIRECT_CODEC);
        event.dataPackRegistry(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, EdibleBlockType.DIRECT_CODEC, EdibleBlockType.DIRECT_CODEC);
        event.dataPackRegistry(BovinesRegistryKeys.NECTAR, Nectar.DIRECT_CODEC, Nectar.DIRECT_CODEC);
        event.dataPackRegistry(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, FlowerCrownMaterial.DIRECT_CODEC, FlowerCrownMaterial.DIRECT_CODEC);
    }
}
