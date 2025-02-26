package house.greenhouse.bovinesandbuttercups;

import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.variant.model.BovinesCowModelTypes;
import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.BovinesCriteriaTriggers;
import house.greenhouse.bovinesandbuttercups.content.attachment.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.block.entity.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.block.entity.MoobloomEatDispenseBehavior;
import house.greenhouse.bovinesandbuttercups.content.command.BovinesCommands;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.BovinesTextureModifierFactories;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.particle.BovinesParticleTypes;
import house.greenhouse.bovinesandbuttercups.content.predicate.BovinesEntitySubPredicateTypes;
import house.greenhouse.bovinesandbuttercups.content.predicate.BovinesLootItemConditionTypes;
import house.greenhouse.bovinesandbuttercups.content.recipe.BovinesRecipeSerializers;
import house.greenhouse.bovinesandbuttercups.content.recipe.ingredient.BovinesIngredients;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.content.worldgen.BovinesStructureTypes;
import house.greenhouse.bovinesandbuttercups.network.clientbound.*;
import house.greenhouse.bovinesandbuttercups.platform.BovinesPlatformHelperFabric;
import house.greenhouse.bovinesandbuttercups.registry.BovinesFabricDynamicRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.util.CowSpawnUtil;
import house.greenhouse.bovinesandbuttercups.util.CreativeTabHelper;
import house.greenhouse.bovinesandbuttercups.util.MooshroomSpawnUtil;
import house.greenhouse.bovinesandbuttercups.util.SnowLayerUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class BovinesAndButtercupsFabric implements ModInitializer {
    private static RegistryAccess biomeRegistries;

    @Override
    public void onInitialize() {
        BovinesAndButtercups.init(new BovinesPlatformHelperFabric());
        registerContents();
        registerNetwork();
        registerCreativeTabEntries();
        registerCompostables();
        registerBiomeModifications();
        registerResourcePacks();
        MoobloomEatDispenseBehavior.registerBehavior(MoobloomEatDispenseBehavior.INSTANCE);

        EntityTrackingEvents.START_TRACKING.register((entity, player) -> {
            if (entity instanceof LivingEntity living) {
                if (entity.hasAttached(BovinesAttachments.LOCKDOWN))
                    BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncLockdownEffectsClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getLockdownAttachment(living), true));
                if (entity.hasAttached(BovinesAttachments.COW_VARIANT)) {
                    CowVariantAttachment attachment = living.getAttached(BovinesAttachments.COW_VARIANT);
                    for (CowModelLayer layer : attachment.cowVariant().value().configuration().settings().layers())
                        for (TextureModifierFactory<?> modifier : layer.textureModifiers())
                            modifier.init(living);
                    BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncCowVariantClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getCowVariantAttachment(living), true));
                }
                if (entity.hasAttached(BovinesAttachments.MOOSHROOM_EXTRAS) && entity instanceof MushroomCow mooshroom)
                    BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncMooshroomExtrasClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getMooshroomExtrasAttachment(mooshroom), true));
                if (entity.hasAttached(BovinesAttachments.COW_EXTRAS) && entity instanceof Cow cow)
                    BovinesAndButtercups.getHelper().sendClientboundPacket(player, new SyncCowExtrasClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getCowExtrasAttachment(cow), true));
            }
        });
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            CowVariantAttachment attachment = entity.getAttached(BovinesAttachments.COW_VARIANT);
            if (entity.getType() == EntityType.MOOSHROOM) {
                if (attachment == null) {
                    if (MooshroomSpawnUtil.getTotalSpawnWeight(level, entity.blockPosition()) > 0)
                        CowVariantAttachment.setCowVariant((MushroomCow) entity, MooshroomSpawnUtil.getMooshroomSpawnTypeDependingOnBiome(level, entity.blockPosition(), level.getRandom()));
                    else
                        CowVariantAttachment.setCowVariant((MushroomCow) entity, MooshroomSpawnUtil.getMostCommonMooshroomSpawnType(level, ((MushroomCow)entity).getVariant()));
                    CowVariantAttachment.sync((MushroomCow)entity);
                }
            } else if (entity.getType() == EntityType.COW) {
                if (attachment == null) {
                    if (CowSpawnUtil.getTotalSpawnWeight(level, entity.blockPosition()) > 0)
                        CowVariantAttachment.setCowVariant((Cow) entity, CowSpawnUtil.getCowSpawnVariantDependingOnBiome(level, entity.blockPosition(), level.getRandom()));
                    else
                        CowVariantAttachment.setCowVariant((Cow) entity, CowSpawnUtil.getMostCommonCowSpawnVariant(level));
                    CowVariantAttachment.sync((Cow)entity);
                }
            }
        });
        UseEntityCallback.EVENT.register((player, world, hand, target, hitResult) -> {
            if (player.isSpectator())
                return InteractionResult.PASS;
            ItemStack handItem = player.getItemInHand(hand);
            if (target.getType() == EntityType.MOOSHROOM) {
                if (handItem.is(ConventionalItemTags.SHEAR_TOOLS) && target.hasAttached(BovinesAttachments.MOOSHROOM_EXTRAS) && !target.getAttached(BovinesAttachments.MOOSHROOM_EXTRAS).allowShearing())
                    return InteractionResult.FAIL;
                return SnowLayerUtil.removeSnowIfShovel(target, player, hand, handItem);
            }
            return InteractionResult.PASS;
        });

        BovinesFabricDynamicRegistries.init();

        FabricDefaultAttributeRegistry.register(BovinesEntityTypes.MOOBLOOM, Moobloom.createAttributes());
        LandPathNodeTypesRegistry.register(BovinesBlocks.RICH_HONEY_BLOCK, PathType.STICKY_HONEY, null);

//        BovinesAccessoriesEvents.init();
//        BovinesTrinketsEvents.init();
    }

    private static void registerNetwork() {
        PayloadTypeRegistry.playS2C().register(SyncConditionedTextureModifier.TYPE, SyncConditionedTextureModifier.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncCowVariantClientboundPacket.TYPE, SyncCowVariantClientboundPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncLockdownEffectsClientboundPacket.TYPE, SyncLockdownEffectsClientboundPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncMoobloomSnowLayerClientboundPacket.TYPE, SyncMoobloomSnowLayerClientboundPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncMooshroomExtrasClientboundPacket.TYPE, SyncMooshroomExtrasClientboundPacket.STREAM_CODEC);
    }

    private static void registerContents() {
        registerRegistries();
        BovinesSoundEvents.registerAll();
        BovinesBlockEntityTypes.registerAll();
        BovinesBlocks.registerAll();
        BovinesEntityTypes.registerAll();
        BovinesCowTypes.registerAll();
        BovinesCriteriaTriggers.registerAll();
        BovinesDataComponents.registerAll();
        BovinesEffects.registerAll();
        BovinesEntitySubPredicateTypes.registerAll();
        BovinesLootItemConditionTypes.registerAll();
        BovinesItems.registerAll();
        BovinesParticleTypes.registerAll();
        BovinesRecipeSerializers.registerAll();
        BovinesStructureTypes.registerAll();
        BovinesTextureModifierFactories.registerAll();
        BovinesCowModelTypes.registerAll();

        BovinesAttachments.init();
        BovinesIngredients.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BovinesCommands.register(dispatcher, registryAccess);
        });
    }

    private static void registerRegistries() {
        Registry.register((Registry) BuiltInRegistries.REGISTRY, BovinesRegistryKeys.COW_TYPE, BovinesRegistries.COW_TYPE);
        Registry.register((Registry) BuiltInRegistries.REGISTRY, BovinesRegistryKeys.MODEL_TYPE, BovinesRegistries.MODEL_TYPE);
        Registry.register((Registry) BuiltInRegistries.REGISTRY, BovinesRegistryKeys.TEXTURE_MODIFIER, BovinesRegistries.TEXTURE_MODIFIER);
    }

    private static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.ALSTROEMERIA, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.BUTTERCUP, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.CHARGELILY, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.FREESIA, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.HYACINTH, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.LIMELIGHT, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.PINK_DAISY, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.SNOWDROP, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.TROPICAL_BLUE, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.CUSTOM_FLOWER, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.CUSTOM_MUSHROOM, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.CUSTOM_MUSHROOM_BLOCK, 0.85F);
    }

    private static void registerCreativeTabEntries() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.SPORE_BLOSSOM, CreativeTabHelper.getFlowersForCreativeTab(entries.getContext().holders()));
            entries.addAfter(BovinesItems.SOMBERCUP, CreativeTabHelper.getCustomFlowersForCreativeTab(entries.getContext().holders()));
            entries.addAfter(Items.RED_MUSHROOM, CreativeTabHelper.getCustomMushroomsForCreativeTab(entries.getContext().holders()));
            entries.addAfter(Items.RED_MUSHROOM_BLOCK, CreativeTabHelper.getCustomMushroomBlocksForCreativeTab(entries.getContext().holders()));
            entries.addAfter(Items.HONEY_BLOCK, BovinesItems.RICH_HONEY_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addAfter(Items.MILK_BUCKET, CreativeTabHelper.getNectarBowlsForCreativeTab(entries.getContext().holders()));
            entries.addAfter(Items.HONEY_BOTTLE, BovinesItems.RICH_HONEY_BOTTLE);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.accept(BovinesItems.MOOBLOOM_SPAWN_EGG);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries ->
                entries.addAfter(Items.SADDLE, CreativeTabHelper.getFlowerCrownsForCreativeTab(entries.getContext().holders())));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries ->
                entries.addAfter(Items.HONEY_BLOCK, BovinesItems.RICH_HONEY_BLOCK));
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) ->
                CreativeTabHelper.addEdibleBlocksToCreativeTabs(entries.getContext().holders(), entries.getDisplayStacks(), entries.getSearchTabStacks(), BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group).orElseThrow(), entries::accept, entries::prepend, (existingStack, newStack, visibility) -> entries.addBefore(existingStack, List.of(newStack), visibility), (existingStack, newStack, visibility) -> entries.addAfter(existingStack, List.of(newStack), visibility)));
    }

    public static void setBiomeRegistries(@Nullable RegistryAccess registries) {
        biomeRegistries = registries;
    }

    public static void registerBiomeModifications() {
        createBiomeModifications(BovinesAndButtercups.asResource("moobloom"),
                biome -> biomeRegistries.lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).stream().anyMatch(cowVariant -> cowVariant.type() == BovinesCowTypes.MOOBLOOM_TYPE && cowVariant.configuration().settings() != null && cowVariant.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.data().contains(biome.getBiomeRegistryEntry())) && cowVariant.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.weight().asInt() > 0)),
                BovinesEntityTypes.MOOBLOOM, 15, 4, 4);
        createBiomeModifications(BovinesAndButtercups.asResource("mooshroom"),
                biome -> biome.getBiomeKey() != Biomes.MUSHROOM_FIELDS && biomeRegistries.lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).stream().anyMatch(cowVariant -> cowVariant.type() == BovinesCowTypes.MOOSHROOM_TYPE && cowVariant.configuration().settings() != null && cowVariant.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.data().contains(biome.getBiomeRegistryEntry())) && cowVariant.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.weight().asInt() > 0)),
                EntityType.MOOSHROOM, 15, 4, 4);
        BiomeModifications.create(BovinesAndButtercups.asResource("remove_cows")).add(ModificationPhase.REMOVALS, biome -> biome.hasTag(BovinesTags.BiomeTags.PREVENT_COW_SPAWNS), context -> context.getSpawnSettings().removeSpawnsOfEntityType(EntityType.COW));
    }

    private static void createBiomeModifications(ResourceLocation location, Predicate<BiomeSelectionContext> predicate, EntityType<?> entityType, int weight, int min, int max) {
        BiomeModifications.create(location).add(ModificationPhase.POST_PROCESSING, predicate, context -> context.getSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(entityType, weight, min, max)));
    }

    public static void registerResourcePacks() {
        FabricLoader.getInstance().getModContainer(BovinesAndButtercups.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(BovinesAndButtercups.asResource("mojang"), modContainer, Component.translatable("resourcePack.bovinesandbuttercups.mojang.name"), ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(BovinesAndButtercups.asResource("no_buds"), modContainer, Component.translatable("resourcePack.bovinesandbuttercups.noBuds.name"), ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(BovinesAndButtercups.asResource("no_grass"), modContainer, Component.translatable("resourcePack.bovinesandbuttercups.noGrass.name"), ResourcePackActivationType.NORMAL);
        });
    }
}
