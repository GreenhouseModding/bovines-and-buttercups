package house.greenhouse.bovinesandbuttercups.registry;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.BovinesCowModelTypes;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = BovinesAndButtercups.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BovinesRegistryEvents {
    @SubscribeEvent
    public static void registerContent(RegisterEvent event) {
        register(event, BovinesAttachments::registerAll);
        register(event, BovinesBiomeModifierSerializers::registerAll);
        register(event, BovinesBlockEntityTypes::registerAll);
        register(event, BovinesBlocks::registerAll);
        register(event, BovinesCowTypes::registerAll);
        register(event, BovinesCriteriaTriggers::registerAll);
        register(event, BovinesDataComponents::registerAll);
        register(event, BovinesEntitySubPredicateTypes::registerAll);
        register(event, BovinesEntityTypes::registerAll);
        register(event, BovinesIngredients::registerAll);
        register(event, BovinesLootItemConditionTypes::registerAll);
        register(event, BovinesParticleTypes::registerAll);
        register(event, BovinesRecipeSerializers::registerAll);
        register(event, BovinesSoundEvents::registerAll);
        register(event, BovinesStructureTypes::registerAll);
        register(event, BovinesTextureModifierFactories::registerAll);
        register(event, BovinesCowModelTypes::registerAll);

        if (event.getRegistryKey() == Registries.ARMOR_MATERIAL) {
            registerHolders(BovinesSoundEvents::registerHolders);
            registerHolders(BovinesArmorMaterials::registerAll);
            BovinesItems.registerAll(Registry::register);
        }

        if (event.getRegistryKey() == Registries.MOB_EFFECT)
            registerHolders(BovinesEffects::registerAll);
    }

    private static <T> void register(RegisterEvent event, Consumer<RegistrationCallback<T>> consumer) {
        consumer.accept((registry, id, value) ->
                event.register(registry.key(), id, () -> value));
    }

    private static <T> void registerHolders(Consumer<HolderRegistrationCallback<T>> consumer) {
        consumer.accept((registry, id, value) -> {
            Registry.register(registry, id, value);
            return DeferredHolder.create(registry.key(), id);
        });
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
