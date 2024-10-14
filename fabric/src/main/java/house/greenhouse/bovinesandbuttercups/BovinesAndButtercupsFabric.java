package house.greenhouse.bovinesandbuttercups;

import house.greenhouse.bovinesandbuttercups.access.MooshroomInitializedTypeAccess;
import house.greenhouse.bovinesandbuttercups.api.attachment.MooshroomExtrasAttachment;
import house.greenhouse.bovinesandbuttercups.api.cowtype.CowModelLayer;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.integration.accessories.BovinesAccessoriesEvents;
import house.greenhouse.bovinesandbuttercups.integration.trinkets.BovinesTrinketsEvents;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncMoobloomSnowLayerClientboundPacket;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncMooshroomExtrasClientboundPacket;
import house.greenhouse.bovinesandbuttercups.registry.BovinesEntitySubPredicateTypes;
import house.greenhouse.bovinesandbuttercups.util.MooshroomSpawnUtil;
import house.greenhouse.bovinesandbuttercups.util.SnowLayerUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.attachment.LockdownAttachment;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncConditionedTextureModifier;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncCowTypeClientboundPacket;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncLockdownEffectsClientboundPacket;
import house.greenhouse.bovinesandbuttercups.registry.BovinesArmorMaterials;
import house.greenhouse.bovinesandbuttercups.registry.BovinesAttachments;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesCriteriaTriggers;
import house.greenhouse.bovinesandbuttercups.registry.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.registry.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.registry.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesFabricDynamicRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesLootItemConditionTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesParticleTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRecipeSerializers;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.registry.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.registry.BovinesStructureTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesTextureModificationFactories;
import house.greenhouse.bovinesandbuttercups.util.CreativeTabHelper;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class BovinesAndButtercupsFabric implements ModInitializer {
    private static RegistryAccess biomeRegistries;

    @Override
    public void onInitialize() {
        registerContents();
        registerNetwork();
        registerCreativeTabEntries();
        registerCompostables();
        registerBiomeModifications();

        EntityTrackingEvents.START_TRACKING.register((entity, player) -> {
            if (entity instanceof LivingEntity living) {
                if (entity.hasAttached(BovinesAttachments.LOCKDOWN))
                    LockdownAttachment.sync(living);
                if (entity.hasAttached(BovinesAttachments.COW_TYPE)) {
                    CowTypeAttachment attachment = living.getAttached(BovinesAttachments.COW_TYPE);
                    for (CowModelLayer layer : attachment.cowType().value().configuration().layers()) {
                        for (TextureModifierFactory<?> modifier : layer.textureModifiers())
                            modifier.init(living);
                    }
                    CowTypeAttachment.syncToPlayer(living, player);
                }
                if (entity.hasAttached(BovinesAttachments.MOOSHROOM_EXTRAS))
                    MooshroomExtrasAttachment.syncToPlayer(living, player);
            }
        });
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (!(entity instanceof LivingEntity living))
                return;
            if (entity.hasAttached(BovinesAttachments.LOCKDOWN))
                LockdownAttachment.sync(living);
            CowTypeAttachment attachment = entity.getAttached(BovinesAttachments.COW_TYPE);
            if (entity.getType() == EntityType.MOOSHROOM) {
                if (attachment == null) {
                    if (((MooshroomInitializedTypeAccess)entity).bovinesandbuttercups$initialType() != null) {
                        CowTypeAttachment.setCowType((MushroomCow) entity, MooshroomSpawnUtil.getMooshroomTypeFromMushroomType(level, ((MooshroomInitializedTypeAccess)entity).bovinesandbuttercups$initialType()));
                    } else if (MooshroomSpawnUtil.getTotalSpawnWeight(level, entity.blockPosition()) > 0) {
                        CowTypeAttachment.setCowType((MushroomCow) entity, MooshroomSpawnUtil.getMooshroomSpawnTypeDependingOnBiome(level, entity.blockPosition(), level.getRandom()));
                    } else {
                        CowTypeAttachment.setCowType((MushroomCow) entity, MooshroomSpawnUtil.getMostCommonMooshroomSpawnType(level, ((MushroomCow)entity).getVariant()));
                    }
                }
                ((MooshroomInitializedTypeAccess)entity).bovinesandbuttercups$clearInitialType();
            }
            if (entity.hasAttached(BovinesAttachments.COW_TYPE))
                CowTypeAttachment.sync(living);
            if (entity.hasAttached(BovinesAttachments.MOOSHROOM_EXTRAS))
                MooshroomExtrasAttachment.sync(living);
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

        BovinesAccessoriesEvents.init();
        BovinesTrinketsEvents.init();
    }

    private static void registerNetwork() {
        PayloadTypeRegistry.playS2C().register(SyncConditionedTextureModifier.TYPE, SyncConditionedTextureModifier.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncCowTypeClientboundPacket.TYPE, SyncCowTypeClientboundPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncLockdownEffectsClientboundPacket.TYPE, SyncLockdownEffectsClientboundPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncMoobloomSnowLayerClientboundPacket.TYPE, SyncMoobloomSnowLayerClientboundPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncMooshroomExtrasClientboundPacket.TYPE, SyncMooshroomExtrasClientboundPacket.STREAM_CODEC);
    }

    private static void registerContents() {
        BovinesSoundEvents.registerHolders(Registry::registerForHolder);
        BovinesArmorMaterials.registerAll(Registry::registerForHolder);
        BovinesBlockEntityTypes.registerAll(Registry::register);
        BovinesBlocks.registerAll(Registry::register);
        BovinesCowTypeTypes.registerAll(Registry::register);
        BovinesCriteriaTriggers.registerAll(Registry::register);
        BovinesDataComponents.registerAll(Registry::register);
        BovinesEffects.registerAll(Registry::registerForHolder);
        BovinesEntitySubPredicateTypes.registerAll(Registry::register);
        BovinesEntityTypes.registerAll(Registry::register);
        BovinesLootItemConditionTypes.registerAll(Registry::register);
        BovinesItems.registerAll(Registry::register);
        BovinesParticleTypes.registerAll(Registry::register);
        BovinesRecipeSerializers.registerAll(Registry::register);
        BovinesSoundEvents.registerAll(Registry::register);
        BovinesStructureTypes.registerAll(Registry::register);
        BovinesTextureModificationFactories.registerAll(Registry::register);
    }

    private static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(BovinesItems.BIRD_OF_PARADISE, 0.65F);
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
            entries.addAfter(Items.SPORE_BLOSSOM, Stream.of(
                    BovinesItems.FREESIA,
                    BovinesItems.BIRD_OF_PARADISE,
                    BovinesItems.BUTTERCUP,
                    BovinesItems.LIMELIGHT,
                    BovinesItems.LINGHOLM,
                    BovinesItems.CHARGELILY,
                    BovinesItems.TROPICAL_BLUE,
                    BovinesItems.HYACINTH,
                    BovinesItems.PINK_DAISY,
                    BovinesItems.SNOWDROP
            ).map(ItemStack::new).toList());
            entries.addAfter(BovinesItems.SNOWDROP, CreativeTabHelper.getCustomFlowersForCreativeTab(entries.getContext().holders()));
            entries.addAfter(Items.RED_MUSHROOM, CreativeTabHelper.getCustomMushroomsForCreativeTab(entries.getContext().holders()));
            entries.addAfter(Items.RED_MUSHROOM_BLOCK, CreativeTabHelper.getCustomMushroomBlocksForCreativeTab(entries.getContext().holders()));
            entries.addAfter(Items.HONEY_BLOCK, BovinesItems.RICH_HONEY_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addAfter(Items.MILK_BUCKET, CreativeTabHelper.getNectarBowlsForCreativeTab(entries.getContext().holders()));
            entries.addAfter(Items.HONEY_BOTTLE, BovinesItems.RICH_HONEY_BOTTLE);
            entries.addAfter(Items.CAKE, Stream.of(
                    BovinesItems.FREESIA_CUPCAKE,
                    BovinesItems.BIRD_OF_PARADISE_CUPCAKE,
                    BovinesItems.BUTTERCUP_CUPCAKE,
                    BovinesItems.LIMELIGHT_CUPCAKE,
                    BovinesItems.LINGHOLM_CUPCAKE,
                    BovinesItems.CHARGELILY_CUPCAKE,
                    BovinesItems.TROPICAL_BLUE_CUPCAKE,
                    BovinesItems.HYACINTH_CUPCAKE,
                    BovinesItems.PINK_DAISY_CUPCAKE,
                    BovinesItems.SNOWDROP_CUPCAKE
            ).map(ItemStack::new).toList());
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.accept(BovinesItems.MOOBLOOM_SPAWN_EGG);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries ->
                entries.addAfter(Items.SADDLE, CreativeTabHelper.getFlowerCrownsForCreativeTab(entries.getContext().holders())));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries ->
                entries.addAfter(Items.HONEY_BLOCK, BovinesItems.RICH_HONEY_BLOCK));
    }

    public static void setBiomeRegistries(@Nullable RegistryAccess registries) {
        biomeRegistries = registries;
    }

    public static void registerBiomeModifications() {
        createBiomeModifications(BovinesAndButtercups.asResource("moobloom"),
                biome -> biomeRegistries.registryOrThrow(BovinesRegistryKeys.COW_TYPE).stream().anyMatch(cowType -> cowType.type() == BovinesCowTypeTypes.MOOBLOOM_TYPE && cowType.configuration().settings() != null && cowType.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.data().contains(biome.getBiomeRegistryEntry())) && cowType.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.weight().asInt() > 0)),
                BovinesEntityTypes.MOOBLOOM, 15, 4, 4);
        createBiomeModifications(BovinesAndButtercups.asResource("mooshroom"),
                biome -> biome.getBiomeKey() != Biomes.MUSHROOM_FIELDS && biomeRegistries.registryOrThrow(BovinesRegistryKeys.COW_TYPE).stream().anyMatch(cowType -> cowType.type() == BovinesCowTypeTypes.MOOSHROOM_TYPE && cowType.configuration().settings() != null && cowType.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.data().contains(biome.getBiomeRegistryEntry())) && cowType.configuration().settings().biomes().unwrap().stream().anyMatch(wrapper -> wrapper.weight().asInt() > 0)),
                EntityType.MOOSHROOM, 15, 4, 4);
        BiomeModifications.create(BovinesAndButtercups.asResource("remove_cows")).add(ModificationPhase.REMOVALS, biome -> biome.hasTag(BovinesTags.BiomeTags.PREVENT_COW_SPAWNS), context -> context.getSpawnSettings().removeSpawnsOfEntityType(EntityType.COW));
    }

    private static void createBiomeModifications(ResourceLocation location, Predicate<BiomeSelectionContext> predicate, EntityType<?> entityType, int weight, int min, int max) {
        BiomeModifications.create(location).add(ModificationPhase.POST_PROCESSING, predicate, context -> context.getSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(entityType, weight, min, max)));
    }
}
