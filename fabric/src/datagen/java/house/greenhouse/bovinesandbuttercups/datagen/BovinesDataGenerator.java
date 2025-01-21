package house.greenhouse.bovinesandbuttercups.datagen;

import com.mojang.serialization.Lifecycle;
import house.greenhouse.bovinesandbuttercups.api.BovinesConventionalTags;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.FlowerCrownItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.select.EdibleBlockSelectProperty;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.BreedCowWithVariantTrigger;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.ConvertMoobloomFromSculkTrigger;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.LockEffectTrigger;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.PreventEffectTrigger;
import house.greenhouse.bovinesandbuttercups.content.data.edible.BovinesEdibleBlockTypes;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.content.recipe.FlowerCrownRecipe;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowVariants;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.BovinesFlowerCrownMaterials;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootTables;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BovinesDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(DynamicRegistryProvider::new);
        pack.addProvider(AdvancementProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(BlockLootTableProvider::new);
        pack.addProvider(ChestLootTableProvider::new);
        pack.addProvider(EntityLootTableProvider::new);
        pack.addProvider(ShearingLootTableProvider::new);
        pack.addProvider(BiomeTagProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(ConfiguredFeatureTagProvider::new);
        pack.addProvider(EdibleBlockTypeTagProvider::new);
        pack.addProvider(EntityTypeTagProvider::new);
        pack.addProvider(FlowerCrownMaterialTagProvider::new);
        pack.addProvider(ItemTagProvider::new);

        pack.addProvider(ModelProvider::new);
    }

    @Override
    public String getEffectiveModId() {
        return BovinesAndButtercups.MOD_ID;
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(BovinesRegistryKeys.COW_VARIANT, BovinesCowVariants::bootstrap);
        registryBuilder.add(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesFlowerCrownMaterials::bootstrap);
        registryBuilder.add(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesEdibleBlockTypes::bootstrap);
    }

    private static class DynamicRegistryProvider extends FabricDynamicRegistryProvider {

        public DynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
        }

        @Override
        protected void configure(HolderLookup.Provider registries, Entries entries) {
            BovinesCowVariants.bootstrap(createContext(registries, entries));
            BovinesFlowerCrownMaterials.bootstrap(createContext(registries, entries));
            BovinesEdibleBlockTypes.bootstrap(createContext(registries, entries));
        }

        private static <T> BootstrapContext<T> createContext(HolderLookup.Provider registries, Entries entries) {
            return new BootstrapContext<>() {
                @Override
                public Holder.Reference<T> register(ResourceKey<T> resourceKey, T object, Lifecycle lifecycle) {
                    return (Holder.Reference<T>) entries.add(resourceKey, object);
                }

                @Override
                public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> resourceKey) {
                    return registries.lookupOrThrow(resourceKey);
                }
            };
        }

        @Override
        public @NotNull String getName() {
            return "Dynamic Registries";
        }
    }

    private static class AdvancementProvider extends FabricAdvancementProvider {

        protected AdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup);
        }

        @Override
        @SuppressWarnings("removal")
        public void generateAdvancement(HolderLookup.Provider lookup, Consumer<AdvancementHolder> consumer) {
            ItemStack lockEffectStack = new ItemStack(BovinesItems.BUTTERCUP_NECTAR_BOWL);
            ItemStack preventEffectStack = new ItemStack(BovinesItems.FREESIA_NECTAR_BOWL);
            consumer.accept(Advancement.Builder.advancement()
                    .display(new DisplayInfo(
                            lockEffectStack,
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.lock_effect.title"),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.lock_effect.description"),
                            Optional.empty(),
                            AdvancementType.TASK,
                            true,
                            true,
                            false)
                    )
                    .parent(BovinesAndButtercups.asResource("husbandry/prevent_effect"))
                    .requirements(AdvancementRequirements.allOf(List.of("lock_effect")))
                    .addCriterion("lock_effect", LockEffectTrigger.INSTANCE.createCriterion(new LockEffectTrigger.TriggerInstance(Optional.empty(), Optional.empty())))
                    .build(BovinesAndButtercups.asResource("husbandry/lock_effect")));
            consumer.accept(Advancement.Builder.advancement()
                    .display(new DisplayInfo(
                            preventEffectStack,
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.prevent_effect.title"),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.prevent_effect.description"),
                            Optional.empty(),
                            AdvancementType.TASK,
                            true,
                            true,
                            false)
                    )
                    .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                    .requirements(AdvancementRequirements.allOf(List.of("prevent_effect")))
                    .addCriterion("prevent_effect", PreventEffectTrigger.INSTANCE.createCriterion(new PreventEffectTrigger.TriggerInstance(Optional.empty(), Optional.empty())))
                    .build(BovinesAndButtercups.asResource("husbandry/prevent_effect")));
            consumer.accept(Advancement.Builder.advancement()
                    .display(new DisplayInfo(
                            new ItemStack(BovinesItems.PINK_DAISY),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.breed_new_moobloom.title"),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.breed_new_moobloom.description"),
                            Optional.empty(),
                            AdvancementType.TASK,
                            true,
                            true,
                            false)
                    )
                    .parent(ResourceLocation.withDefaultNamespace("husbandry/breed_an_animal"))
                    .requirements(AdvancementRequirements.allOf(List.of("breed_new_moobloom")))
                    .addCriterion("breed_new_moobloom", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(true))))
                    .build(BovinesAndButtercups.asResource("husbandry/breed_new_moobloom")));

            HolderLookup.RegistryLookup<CowVariant<?>> cowTypeRegistry = lookup.lookupOrThrow(BovinesRegistryKeys.COW_VARIANT);

            consumer.accept(Advancement.Builder.advancement()
                    .display(new DisplayInfo(
                            new ItemStack(BovinesItems.LIMELIGHT),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.breed_all_mooblooms.title"),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.breed_all_mooblooms.description"),
                            Optional.empty(),
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false)
                    )
                    .parent(BovinesAndButtercups.asResource("husbandry/breed_new_moobloom"))
                    .requirements(AdvancementRequirements.allOf(List.of(
                            "bovinesandbuttercups:bird_of_paradise",
                            "bovinesandbuttercups:camellia",
                            "bovinesandbuttercups:freesia",
                            "bovinesandbuttercups:hyacinth",
                            "bovinesandbuttercups:limelight",
                            "bovinesandbuttercups:lingholm",
                            "bovinesandbuttercups:nightshade",
                            "bovinesandbuttercups:snowdrop",
                            "bovinesandbuttercups:tropical_blue"
                    )))
                    .addCriterion("bovinesandbuttercups:bird_of_paradise", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.BIRD_OF_PARADISE)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:camellia", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.CAMELLIA)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:freesia", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.FREESIA)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:hyacinth", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.HYACINTH)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:limelight", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.LIMELIGHT)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:lingholm", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.LINGHOLM)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:nightshade", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.NIGHTSHADE)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:snowdrop", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.SNOWDROP)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:tropical_blue", BreedCowWithVariantTrigger.INSTANCE.createCriterion(new BreedCowWithVariantTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowVariants.MoobloomKeys.TROPICAL_BLUE)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .rewards(AdvancementRewards.Builder.experience(100))
                    .build(BovinesAndButtercups.asResource("husbandry/breed_all_mooblooms")));
            consumer.accept(Advancement.Builder.advancement()
                    .display(new DisplayInfo(
                            new ItemStack(BovinesItems.SOMBERCUP),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.convert_moobloom_from_sculk.title"),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.convert_moobloom_from_sculk.description"),
                            Optional.empty(),
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false)
                    )
                    .parent(BovinesAndButtercups.asResource("husbandry/breed_new_moobloom"))
                    .requirements(AdvancementRequirements.allOf(List.of(
                            "has_converted"
                    )))
                    .addCriterion("has_converted", ConvertMoobloomFromSculkTrigger.INSTANCE.createCriterion(new ConvertMoobloomFromSculkTrigger.TriggerInstance(Optional.empty())))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .build(BovinesAndButtercups.asResource("husbandry/convert_moobloom_from_sculk")));
            consumer.accept(Advancement.Builder.advancement()
                    .display(new DisplayInfo(
                            FlowerCrownItem.createRainbowCrown(lookup),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.obtain_flower_crown.title"),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.obtain_flower_crown.description"),
                            Optional.empty(),
                            AdvancementType.TASK,
                            true,
                            true,
                            false)
                    )
                    .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                    .requirements(AdvancementRequirements.allOf(List.of("get_flower_crown")))
                    .addCriterion("get_flower_crown", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(lookup.lookupOrThrow(Registries.ITEM), BovinesItems.FLOWER_CROWN)))
                    .build(BovinesAndButtercups.asResource("husbandry/obtain_flower_crown")));
        }
    }

    private static class RecipeProvider extends FabricRecipeProvider {
        public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
        }

        @Override
        protected net.minecraft.data.recipes.RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput output) {
            return new net.minecraft.data.recipes.RecipeProvider(registryLookup, output) {
                @Override
                public void buildRecipes() {
                    oneToOneConversionRecipe(Items.ORANGE_DYE, BovinesBlocks.BIRD_OF_PARADISE, "orange_dye");
                    oneToOneConversionRecipe(Items.YELLOW_DYE, BovinesBlocks.BUTTERCUP, "yellow_dye");
                    oneToOneConversionRecipe(Items.MAGENTA_DYE, BovinesBlocks.CAMELLIA, "magenta_dye");
                    oneToOneConversionRecipe(Items.LIGHT_BLUE_DYE, BovinesBlocks.CHARGELILY, "light_blue_dye");
                    oneToOneConversionRecipe(Items.RED_DYE, BovinesBlocks.FREESIA, "red_dye");
                    oneToOneConversionRecipe(Items.PURPLE_DYE, BovinesBlocks.HYACINTH, "purple_dye");
                    oneToOneConversionRecipe(Items.LIME_DYE, BovinesBlocks.LIMELIGHT, "lime_dye");
                    oneToOneConversionRecipe(Items.CYAN_DYE, BovinesBlocks.LINGHOLM, "cyan_dye");
                    oneToOneConversionRecipe(Items.LIGHT_GRAY_DYE, BovinesBlocks.NIGHTSHADE, "light_gray_dye");
                    oneToOneConversionRecipe(Items.PINK_DYE, BovinesBlocks.PINK_DAISY, "pink_dye");
                    oneToOneConversionRecipe(Items.WHITE_DYE, BovinesBlocks.SNOWDROP, "white_dye");
                    oneToOneConversionRecipe(Items.BLACK_DYE, BovinesBlocks.SOMBERCUP, "black_dye");
                    oneToOneConversionRecipe(Items.BLUE_DYE, BovinesBlocks.TROPICAL_BLUE, "blue_dye");

                    ShapelessRecipeBuilder.shapeless(registryLookup.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.SUGAR, 3)
                            .requires(BovinesItems.RICH_HONEY_BOTTLE)
                            .group("sugar")
                            .unlockedBy("has_rich_honey_bottle", has(BovinesItems.RICH_HONEY_BOTTLE))
                            .save(output, getConversionRecipeName(Items.SUGAR, BovinesItems.RICH_HONEY_BOTTLE));
                    ShapelessRecipeBuilder.shapeless(registryLookup.lookupOrThrow(Registries.ITEM), RecipeCategory.FOOD, BovinesItems.RICH_HONEY_BOTTLE, 4)
                            .requires(BovinesItems.RICH_HONEY_BLOCK)
                            .requires(Items.GLASS_BOTTLE, 4)
                            .unlockedBy("has_rich_honey_block", has(BovinesBlocks.RICH_HONEY_BLOCK))
                            .save(output);
                    twoByTwoPacker(RecipeCategory.REDSTONE, BovinesBlocks.RICH_HONEY_BLOCK, BovinesItems.RICH_HONEY_BOTTLE);

                    SpecialRecipeBuilder.special(FlowerCrownRecipe::new).save(output, ResourceKey.create(Registries.RECIPE, BovinesAndButtercups.asResource("flower_crown")));
                }
            };
        }


        @Override
        public String getName() {
            return "";
        }
    }

    private static class BlockLootTableProvider extends FabricBlockLootTableProvider {

        protected BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> lookup) {
            super(dataOutput, lookup);
        }

        @Override
        public void generate() {
            dropSelf(BovinesBlocks.BIRD_OF_PARADISE);
            dropSelf(BovinesBlocks.BUTTERCUP);
            dropSelf(BovinesBlocks.CAMELLIA);
            dropSelf(BovinesBlocks.CHARGELILY);
            dropSelf(BovinesBlocks.FREESIA);
            dropSelf(BovinesBlocks.HYACINTH);
            dropSelf(BovinesBlocks.LIMELIGHT);
            dropSelf(BovinesBlocks.LINGHOLM);
            dropSelf(BovinesBlocks.NIGHTSHADE);
            dropSelf(BovinesBlocks.PINK_DAISY);
            dropSelf(BovinesBlocks.SNOWDROP);
            dropSelf(BovinesBlocks.SOMBERCUP);
            dropSelf(BovinesBlocks.TROPICAL_BLUE);

            dropPottedContents(BovinesBlocks.POTTED_BIRD_OF_PARADISE);
            dropPottedContents(BovinesBlocks.POTTED_BUTTERCUP);
            dropPottedContents(BovinesBlocks.POTTED_CAMELLIA);
            dropPottedContents(BovinesBlocks.POTTED_CHARGELILY);
            dropPottedContents(BovinesBlocks.POTTED_FREESIA);
            dropPottedContents(BovinesBlocks.POTTED_HYACINTH);
            dropPottedContents(BovinesBlocks.POTTED_LIMELIGHT);
            dropPottedContents(BovinesBlocks.POTTED_LINGHOLM);
            dropPottedContents(BovinesBlocks.POTTED_PINK_DAISY);
            dropPottedContents(BovinesBlocks.POTTED_SNOWDROP);
            dropPottedContents(BovinesBlocks.POTTED_TROPICAL_BLUE);

            dropSelf(BovinesBlocks.RICH_HONEY_BLOCK);

            add(BovinesBlocks.CUSTOM_FLOWER,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(BovinesItems.CUSTOM_FLOWER))
                                    .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(BovinesDataComponents.CUSTOM_FLOWER))
                                    .when(ExplosionCondition.survivesExplosion())
                    )
            );
            add(BovinesBlocks.POTTED_CUSTOM_FLOWER,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(Items.FLOWER_POT))
                                    .when(ExplosionCondition.survivesExplosion())
                            ).withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(BovinesItems.CUSTOM_FLOWER))
                                    .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(BovinesDataComponents.CUSTOM_FLOWER))
                                    .when(ExplosionCondition.survivesExplosion())
                    )
            );

            add(BovinesBlocks.CUSTOM_MUSHROOM,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(BovinesItems.CUSTOM_MUSHROOM))
                                    .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(BovinesDataComponents.CUSTOM_MUSHROOM))
                                    .when(ExplosionCondition.survivesExplosion())
                    )
            );
            add(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(BovinesItems.CUSTOM_MUSHROOM_BLOCK))
                                    .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(BovinesDataComponents.CUSTOM_MUSHROOM))
                                    .when(ExplosionCondition.survivesExplosion())
                    )
            );
            add(BovinesBlocks.POTTED_CUSTOM_MUSHROOM,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(Items.FLOWER_POT))
                                    .when(ExplosionCondition.survivesExplosion())
                            ).withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(BovinesItems.CUSTOM_MUSHROOM))
                                    .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(BovinesDataComponents.CUSTOM_MUSHROOM))
                                    .when(ExplosionCondition.survivesExplosion())
                            )
            );
            add(BovinesBlocks.POTTED_CUSTOM_MUSHROOM,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(Items.FLOWER_POT))
                                    .when(ExplosionCondition.survivesExplosion())
                            )
            );
        }

    }

    private static class ChestLootTableProvider extends SimpleFabricLootTableProvider {

        public ChestLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup, LootContextParamSets.CHEST);
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
            biConsumer.accept(BovinesLootTables.NIGHTSHADE_RANCH, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(NestedLootTable.lootTableReference(BovinesLootTables.RANCH)))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.STONE_AXE)
                                    .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))))));
            biConsumer.accept(BovinesLootTables.RANCH, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(2.0F))
                            .add(LootItem.lootTableItem(Blocks.SHORT_GRASS)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F)))
                                    .setWeight(2))
                            .add(LootItem.lootTableItem(Items.POTATO)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 12.0F)))
                                    .setWeight(2))
                            .add(LootItem.lootTableItem(Items.CARROT)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F)))
                                    .setWeight(2))
                            .add(LootItem.lootTableItem(Items.HAY_BLOCK)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                    .setWeight(1))
                            .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                    .setWeight(1)))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(2.0F))
                            .setBonusRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.WOODEN_SHOVEL)
                                    .setWeight(3))
                            .add(LootItem.lootTableItem(Items.WOODEN_HOE)
                                    .setWeight(3))
                            .add(LootItem.lootTableItem(Items.SHEARS)
                                    .setWeight(2))
                            .add(LootItem.lootTableItem(Items.STONE_HOE)
                                    .setWeight(2))
                            .add(LootItem.lootTableItem(Items.COMPOSTER)
                                    .setWeight(1))
                            .add(LootItem.lootTableItem(Items.SMOKER)
                                    .setWeight(1)))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.SADDLE)))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.BOWL)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))));
        }
    }

    private static class EntityLootTableProvider extends SimpleFabricLootTableProvider {
        private final CompletableFuture<HolderLookup.Provider> registries;

        public EntityLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup, LootContextParamSets.ENTITY);
            registries = lookup;
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
            HolderLookup.Provider lookup = registries.join();

            biConsumer.accept(BovinesLootTables.MOOBLOOM, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.LEATHER)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookup, UniformGenerator.between(0.0F, 1.0F)))
                            )
                    ).withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.BEEF)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                    .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)))))
                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookup, UniformGenerator.between(0.0F, 1.0F))))
                    )
            );

        }
    }

    private static class ShearingLootTableProvider extends SimpleFabricLootTableProvider {

        public ShearingLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup, LootContextParamSets.SHEARING);
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            output.accept(BovinesLootTables.SHEAR_BIRD_OF_PARADISE_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.BIRD_OF_PARADISE))));
            output.accept(BovinesLootTables.SHEAR_BUTTERCUP_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.BUTTERCUP))));
            output.accept(BovinesLootTables.SHEAR_CAMELLIA_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.CAMELLIA))));
            output.accept(BovinesLootTables.SHEAR_CHARGELILY_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.CHARGELILY))));
            output.accept(BovinesLootTables.SHEAR_FREESIA_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.FREESIA))));
            output.accept(BovinesLootTables.SHEAR_HYACINTH_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.HYACINTH))));
            output.accept(BovinesLootTables.SHEAR_LIMELIGHT_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.LIMELIGHT))));
            output.accept(BovinesLootTables.SHEAR_LINGHOLM_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.LINGHOLM))));
            output.accept(BovinesLootTables.SHEAR_NIGHTSHADE_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.NIGHTSHADE))));
            output.accept(BovinesLootTables.SHEAR_PINK_DAISY_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.PINK_DAISY))));
            output.accept(BovinesLootTables.SHEAR_SNOWDROP_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.SNOWDROP))));
            output.accept(BovinesLootTables.SHEAR_SOMBERCUP_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.SOMBERCUP))));
            output.accept(BovinesLootTables.SHEAR_TROPICAL_BLUE_MOOBLOOM,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(5.0F)).add(LootItem.lootTableItem(BovinesItems.TROPICAL_BLUE))));
        }
    }

    private static class BiomeTagProvider extends FabricTagProvider<Biome> {
        public BiomeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, Registries.BIOME, lookup);
        }

        // TODO: Create Camellia Ranch.
        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_BIRD_OF_PARADISE))
                    .forceAddTag(ConventionalBiomeTags.IS_SAVANNA);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_BUTTERCUP))
                    .forceAddTag(ConventionalBiomeTags.IS_FLOWER_FOREST);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_CAMELLIA))
                    .add(Biomes.CHERRY_GROVE);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_CHARGELILY))
                    .add(Biomes.STONY_PEAKS);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_FREESIA))
                    .add(Biomes.MANGROVE_SWAMP);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_HYACINTH))
                    .add(Biomes.DARK_FOREST);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_LIMELIGHT))
                    .add(Biomes.LUSH_CAVES);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_LINGHOLM))
                    .add(Biomes.TAIGA);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_NIGHTSHADE))
                    .add(Biomes.PALE_GARDEN);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_PINK_DAISY))
                    .forceAddTag(ConventionalBiomeTags.IS_FLOWER_FOREST);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_SNOWDROP))
                    .add(Biomes.SNOWY_TAIGA);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_TROPICAL_BLUE))
                    .forceAddTag(ConventionalBiomeTags.IS_JUNGLE);

            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST))
                    .forceAddTag(ConventionalBiomeTags.IS_FLOWER_FOREST);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_MOOSHROOM_MUSHROOM))
                    .add(Biomes.MUSHROOM_FIELDS);
            tag(BovinesTags.BiomeTags.PREVENT_COW_SPAWNS)
                    .addTag(BovinesTags.BiomeTags.HAS_MOOBLOOM_FLOWER_FOREST);
        }
    }

    private static class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
        public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            ((FabricTagBuilder)tag(BlockTags.BEE_ATTRACTIVE))
                    .add(
                            reverseLookup(BovinesBlocks.BIRD_OF_PARADISE),
                            reverseLookup(BovinesBlocks.BUTTERCUP),
                            reverseLookup(BovinesBlocks.CAMELLIA),
                            reverseLookup(BovinesBlocks.CHARGELILY),
                            reverseLookup(BovinesBlocks.CUSTOM_FLOWER),
                            reverseLookup(BovinesBlocks.FREESIA),
                            reverseLookup(BovinesBlocks.HYACINTH),
                            reverseLookup(BovinesBlocks.LIMELIGHT),
                            reverseLookup(BovinesBlocks.LINGHOLM),
                            reverseLookup(BovinesBlocks.NIGHTSHADE),
                            reverseLookup(BovinesBlocks.PINK_DAISY),
                            reverseLookup(BovinesBlocks.SNOWDROP),
                            reverseLookup(BovinesBlocks.SOMBERCUP),
                            reverseLookup(BovinesBlocks.TROPICAL_BLUE)
                    );
            ((FabricTagBuilder)tag(BlockTags.SMALL_FLOWERS))
                    .forceAddTag(BovinesTags.BlockTags.MOOBLOOM_FLOWERS);

            tag(BovinesTags.BlockTags.DOES_NOT_STICK_RICH_HONEY_BLOCK)
                    .add(
                            reverseLookup(Blocks.SLIME_BLOCK),
                            reverseLookup(Blocks.HONEY_BLOCK)
                    );
            tag(BovinesTags.BlockTags.MOOBLOOM_FLOWERS)
                    .add(
                            reverseLookup(BovinesBlocks.BIRD_OF_PARADISE),
                            reverseLookup(BovinesBlocks.BUTTERCUP),
                            reverseLookup(BovinesBlocks.CAMELLIA),
                            reverseLookup(BovinesBlocks.CHARGELILY),
                            reverseLookup(BovinesBlocks.CUSTOM_FLOWER),
                            reverseLookup(BovinesBlocks.FREESIA),
                            reverseLookup(BovinesBlocks.HYACINTH),
                            reverseLookup(BovinesBlocks.LIMELIGHT),
                            reverseLookup(BovinesBlocks.LINGHOLM),
                            reverseLookup(BovinesBlocks.NIGHTSHADE),
                            reverseLookup(BovinesBlocks.PINK_DAISY),
                            reverseLookup(BovinesBlocks.SNOWDROP),
                            reverseLookup(BovinesBlocks.SOMBERCUP),
                            reverseLookup(BovinesBlocks.TROPICAL_BLUE)
                    );
            tag(BovinesTags.BlockTags.SNOWDROP_PLACEABLE)
                    .add(
                            reverseLookup(Blocks.SNOW_BLOCK)
                    );
            tag(BovinesTags.BlockTags.SOMBERCUP_PLACEABLE)
                    .add(
                            reverseLookup(Blocks.SCULK)
                    );
        }
    }

    private static class ConfiguredFeatureTagProvider extends FabricTagProvider<ConfiguredFeature<?, ?>> {
        public ConfiguredFeatureTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, Registries.CONFIGURED_FEATURE, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            tag(BovinesTags.ConfiguredFeatureTags.RANCH_ALLOWED)
                    .add(CaveFeatures.GLOW_LICHEN)
                    .add(VegetationFeatures.SINGLE_PIECE_OF_GRASS)
                    .add(VegetationFeatures.PATCH_GRASS)
                    .add(VegetationFeatures.PATCH_GRASS_JUNGLE)
                    .add(VegetationFeatures.PATCH_TAIGA_GRASS)
                    .add(VegetationFeatures.PATCH_TALL_GRASS);
        }
    }

    private static class EdibleBlockTypeTagProvider extends FabricTagProvider<EdibleBlockType> {
        public EdibleBlockTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            tag(BovinesTags.EdibleBlockTypeTags.CREATIVE_MENU_ORDER)
                    .add(BovinesEdibleBlockTypes.FREESIA_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.BIRD_OF_PARADISE_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.BUTTERCUP_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.LIMELIGHT_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.LINGHOLM_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.CHARGELILY_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.TROPICAL_BLUE_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.HYACINTH_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.CAMELLIA_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.PINK_DAISY_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.SNOWDROP_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.NIGHTSHADE_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.SOMBERCUP_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.BROWN_MUSHROOM_PUFF_PASTRY)
                    .add(BovinesEdibleBlockTypes.RED_MUSHROOM_PUFF_PASTRY)
                    .add(BovinesEdibleBlockTypes.SUSPICIOUS_BROWN_MUSHROOM_PUFF_PASTRY)
                    .add(BovinesEdibleBlockTypes.SUSPICIOUS_RED_MUSHROOM_PUFF_PASTRY);

            tag(BovinesTags.EdibleBlockTypeTags.IS_SUSPICIOUS)
                    .add(BovinesEdibleBlockTypes.SUSPICIOUS_BROWN_MUSHROOM_PUFF_PASTRY)
                    .add(BovinesEdibleBlockTypes.SUSPICIOUS_RED_MUSHROOM_PUFF_PASTRY);
        }
    }

    private static class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
        public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            tag(BovinesTags.EntityTypeTags.WILL_EQUIP_FLOWER_CROWN)
                    .add(reverseLookup(EntityType.PIGLIN))
                    .add(reverseLookup(EntityType.VILLAGER));
        }
    }

    private static class FlowerCrownMaterialTagProvider extends FabricTagProvider<FlowerCrownMaterial> {
        public FlowerCrownMaterialTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            tag(BovinesTags.FlowerCrownMaterialTags.CREATIVE_MENU_ORDER)
                    .add(BovinesFlowerCrownMaterials.FREESIA)
                    .add(BovinesFlowerCrownMaterials.BIRD_OF_PARADISE)
                    .add(BovinesFlowerCrownMaterials.BUTTERCUP)
                    .add(BovinesFlowerCrownMaterials.LIMELIGHT)
                    .add(BovinesFlowerCrownMaterials.LINGHOLM)
                    .add(BovinesFlowerCrownMaterials.CHARGELILY)
                    .add(BovinesFlowerCrownMaterials.TROPICAL_BLUE)
                    .add(BovinesFlowerCrownMaterials.HYACINTH)
                    .add(BovinesFlowerCrownMaterials.CAMELLIA)
                    .add(BovinesFlowerCrownMaterials.PINK_DAISY)
                    .add(BovinesFlowerCrownMaterials.SNOWDROP)
                    .add(BovinesFlowerCrownMaterials.NIGHTSHADE)
                    .add(BovinesFlowerCrownMaterials.SOMBERCUP);
        }
    }

    private static class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
        public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            ((FabricTagBuilder)tag(ItemTags.SMALL_FLOWERS))
                    .forceAddTag(BovinesTags.ItemTags.MOOBLOOM_FLOWERS);
            tag(BovinesTags.ItemTags.MOOBLOOM_FLOWERS)
                    .add(
                            reverseLookup(BovinesItems.BIRD_OF_PARADISE),
                            reverseLookup(BovinesItems.BUTTERCUP),
                            reverseLookup(BovinesItems.CAMELLIA),
                            reverseLookup(BovinesItems.CHARGELILY),
                            reverseLookup(BovinesItems.CUSTOM_FLOWER),
                            reverseLookup(BovinesItems.FREESIA),
                            reverseLookup(BovinesItems.HYACINTH),
                            reverseLookup(BovinesItems.LIMELIGHT),
                            reverseLookup(BovinesItems.LINGHOLM),
                            reverseLookup(BovinesItems.NIGHTSHADE),
                            reverseLookup(BovinesItems.PINK_DAISY),
                            reverseLookup(BovinesItems.SNOWDROP),
                            reverseLookup(BovinesItems.SOMBERCUP),
                            reverseLookup(BovinesItems.TROPICAL_BLUE)
                    );
            tag(ConventionalItemTags.FOODS)
                    .add(reverseLookup(BovinesItems.RICH_HONEY_BOTTLE));
            tag(BovinesConventionalTags.ConventionalItemTags.HONEY_FOODS)
                    .add(reverseLookup(Items.HONEY_BOTTLE))
                    .add(reverseLookup(BovinesItems.RICH_HONEY_BOTTLE));
        }
    }

    private static class ModelProvider extends FabricModelProvider {
        public ModelProvider(FabricDataOutput output) {
            super(output);
        }

        private static final ModelTemplate CUPCAKE = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_cupcake")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate TWO_CUPCAKES = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_two_cupcakes")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate THREE_CUPCAKES = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_three_cupcakes")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate FOUR_CUPCAKES = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_four_cupcakes")), Optional.empty(), TextureSlot.ALL);

        private static final ModelTemplate PUFF_PASTRY = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_puff_pastry")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate TWO_PUFF_PASTRIES = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_two_puff_pastries")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate THREE_PUFF_PASTRIES = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_three_puff_pastries")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate FOUR_PUFF_PASTRIES = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_four_puff_pastries")), Optional.empty(), TextureSlot.ALL);

        private static final ModelTemplate CUPCAKE_CANDLE = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_cupcake_candle")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate TWO_CUPCAKE_CANDLES_INDEX_ONE = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_two_cupcake_candles_index_one")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate TWO_CUPCAKE_CANDLES_INDEX_TWO = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_two_cupcake_candles_index_two")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate THREE_CUPCAKE_CANDLES_INDEX_ONE = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_three_cupcake_candles_index_one")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate THREE_CUPCAKE_CANDLES_INDEX_TWO = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_three_cupcake_candles_index_two")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate THREE_CUPCAKE_CANDLES_INDEX_THREE = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_three_cupcake_candles_index_three")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate FOUR_CUPCAKE_CANDLES_INDEX_ONE = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_four_cupcake_candles_index_one")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate FOUR_CUPCAKE_CANDLES_INDEX_TWO = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_four_cupcake_candles_index_two")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate FOUR_CUPCAKE_CANDLES_INDEX_THREE = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_four_cupcake_candles_index_three")), Optional.empty(), TextureSlot.ALL);
        private static final ModelTemplate FOUR_CUPCAKE_CANDLES_INDEX_FOUR = new ModelTemplate(Optional.of(BovinesAndButtercups.asResource("block/template_four_cupcake_candles_index_four")), Optional.empty(), TextureSlot.ALL);

        @Override
        public void generateBlockStateModels(BlockModelGenerators generators) {
            var missingMapping = new TextureMapping().put(TextureSlot.ALL, EdibleBlockType.MISSING_KEY.location().withPath(s -> "block/" + s));
            PUFF_PASTRY.create(EdibleBlockType.MISSING_KEY.location().withPath(s -> "block/" + s), missingMapping, generators.modelOutput);

            createCupcakes(BovinesEdibleBlockTypes.BIRD_OF_PARADISE_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.BUTTERCUP_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.CAMELLIA_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.CHARGELILY_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.FREESIA_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.HYACINTH_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.LIMELIGHT_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.LINGHOLM_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.NIGHTSHADE_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.PINK_DAISY_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.SOMBERCUP_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.SNOWDROP_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.TROPICAL_BLUE_CUPCAKE, generators);
            createPuffPastries(BovinesEdibleBlockTypes.BROWN_MUSHROOM_PUFF_PASTRY, generators);
            createPuffPastries(BovinesEdibleBlockTypes.RED_MUSHROOM_PUFF_PASTRY, generators);
            createPuffPastries(BovinesEdibleBlockTypes.SUSPICIOUS_BROWN_MUSHROOM_PUFF_PASTRY, generators);
            createPuffPastries(BovinesEdibleBlockTypes.SUSPICIOUS_RED_MUSHROOM_PUFF_PASTRY, generators);

            createCandles(Blocks.CANDLE, generators);
            createCandles(Blocks.WHITE_CANDLE, generators);
            createCandles(Blocks.ORANGE_CANDLE, generators);
            createCandles(Blocks.MAGENTA_CANDLE, generators);
            createCandles(Blocks.LIGHT_BLUE_CANDLE, generators);
            createCandles(Blocks.YELLOW_CANDLE, generators);
            createCandles(Blocks.LIME_CANDLE, generators);
            createCandles(Blocks.PINK_CANDLE, generators);
            createCandles(Blocks.GRAY_CANDLE, generators);
            createCandles(Blocks.LIGHT_GRAY_CANDLE, generators);
            createCandles(Blocks.CYAN_CANDLE, generators);
            createCandles(Blocks.PURPLE_CANDLE, generators);
            createCandles(Blocks.BLUE_CANDLE, generators);
            createCandles(Blocks.BROWN_CANDLE, generators);
            createCandles(Blocks.GREEN_CANDLE, generators);
            createCandles(Blocks.RED_CANDLE, generators);
            createCandles(Blocks.BLACK_CANDLE, generators);
        }

        public static void createCupcakes(ResourceKey<EdibleBlockType> type, BlockModelGenerators generators) {
            var mapping = new TextureMapping().put(TextureSlot.ALL, type.location().withPath(s -> "block/" + s));

            CUPCAKE.create(type.location().withPath(s -> "block/" + s), mapping, generators.modelOutput);
            TWO_CUPCAKES.create(type.location().withPath(s -> "block/two_" + s + "s"), mapping, generators.modelOutput);
            THREE_CUPCAKES.create(type.location().withPath(s -> "block/three_" + s + "s"), mapping, generators.modelOutput);
            FOUR_CUPCAKES.create(type.location().withPath(s -> "block/four_" + s + "s"), mapping, generators.modelOutput);
        }

        public static void createPuffPastries(ResourceKey<EdibleBlockType> type, BlockModelGenerators generators) {
            var mapping = new TextureMapping().put(TextureSlot.ALL, type.location().withPath(s -> "block/" + s));

            String plural = type.location().getPath().substring(0, type.location().getPath().length() - 1) + "ies";

            PUFF_PASTRY.create(type.location().withPath(s -> "block/" + s), mapping, generators.modelOutput);
            TWO_PUFF_PASTRIES.create(type.location().withPath("block/two_" + plural), mapping, generators.modelOutput);
            THREE_PUFF_PASTRIES.create(type.location().withPath("block/three_" + plural), mapping, generators.modelOutput);
            FOUR_PUFF_PASTRIES.create(type.location().withPath("block/four_" + plural), mapping, generators.modelOutput);
        }

        public static void createCandles(Block candleBlock, BlockModelGenerators generators) {
            createCandlesInner(candleBlock, "", generators);
            createCandlesInner(candleBlock, "_lit", generators);
        }

        private static void createCandlesInner(Block candleBlock, String suffix, BlockModelGenerators generators) {
            var mapping = new TextureMapping().put(TextureSlot.ALL, TextureMapping.getBlockTexture(candleBlock, suffix));
            CUPCAKE_CANDLE.create(modelLocation(candleBlock, "cupcake_", suffix), mapping, generators.modelOutput);

            TWO_CUPCAKE_CANDLES_INDEX_ONE.create(modelLocation(candleBlock, "two_cupcake_", "s_index_one" + suffix), mapping, generators.modelOutput);
            TWO_CUPCAKE_CANDLES_INDEX_TWO.create(modelLocation(candleBlock, "two_cupcake_", "s_index_two" + suffix), mapping, generators.modelOutput);

            THREE_CUPCAKE_CANDLES_INDEX_ONE.create(modelLocation(candleBlock, "three_cupcake_", "s_index_one" + suffix), mapping, generators.modelOutput);
            THREE_CUPCAKE_CANDLES_INDEX_TWO.create(modelLocation(candleBlock, "three_cupcake_", "s_index_two" + suffix), mapping, generators.modelOutput);
            THREE_CUPCAKE_CANDLES_INDEX_THREE.create(modelLocation(candleBlock, "three_cupcake_", "s_index_three" + suffix), mapping, generators.modelOutput);

            FOUR_CUPCAKE_CANDLES_INDEX_ONE.create(modelLocation(candleBlock, "four_cupcake_", "s_index_one" + suffix), mapping, generators.modelOutput);
            FOUR_CUPCAKE_CANDLES_INDEX_TWO.create(modelLocation(candleBlock, "four_cupcake_", "s_index_two" + suffix), mapping, generators.modelOutput);
            FOUR_CUPCAKE_CANDLES_INDEX_THREE.create(modelLocation(candleBlock, "four_cupcake_", "s_index_three" + suffix), mapping, generators.modelOutput);
            FOUR_CUPCAKE_CANDLES_INDEX_FOUR.create(modelLocation(candleBlock, "four_cupcake_", "s_index_four" + suffix), mapping, generators.modelOutput);
        }

        private static ResourceLocation modelLocation(Block block, String prefix, String suffix) {
            ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
            if (blockKey.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE))
                blockKey = BovinesAndButtercups.asResource(blockKey.getPath());
            return blockKey.withPath(s -> "block/" + prefix + s + suffix);
        }

        @Override
        public void generateItemModels(ItemModelGenerators generators) {
            generators.itemModelOutput.accept(BovinesItems.PLACEABLE_EDIBLE, ItemModelUtils.select(new EdibleBlockSelectProperty(), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/missing_edible")),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.BIRD_OF_PARADISE_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/bird_of_paradise_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.BUTTERCUP_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/buttercup_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.CAMELLIA_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/camellia_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.CHARGELILY_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/chargelily_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.FREESIA_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/freesia_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.HYACINTH_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/hyacinth_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.LIMELIGHT_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/limelight_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.LINGHOLM_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/lingholm_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.NIGHTSHADE_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/nightshade_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.PINK_DAISY_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/pink_daisy_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.SNOWDROP_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/snowdrop_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.SOMBERCUP_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/sombercup_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.TROPICAL_BLUE_CUPCAKE), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/tropical_blue_cupcake"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.BROWN_MUSHROOM_PUFF_PASTRY), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/brown_mushroom_puff_pastry"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.RED_MUSHROOM_PUFF_PASTRY), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/red_mushroom_puff_pastry"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.SUSPICIOUS_BROWN_MUSHROOM_PUFF_PASTRY), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/suspicious_brown_mushroom_puff_pastry"))),
                    new SelectItemModel.SwitchCase<>(List.of(BovinesEdibleBlockTypes.SUSPICIOUS_RED_MUSHROOM_PUFF_PASTRY), ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/suspicious_red_mushroom_puff_pastry")))
            ));
            generators.itemModelOutput.accept(BovinesItems.BIRD_OF_PARADISE_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/bird_of_paradise_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.BUTTERCUP_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/buttercup_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.CAMELLIA_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/camellia_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.CHARGELILY_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/chargelily_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.FREESIA_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/freesia_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.HYACINTH_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/hyacinth_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.LIMELIGHT_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/limelight_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.LINGHOLM_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/lingholm_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.NIGHTSHADE_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/nightshade_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.PINK_DAISY_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/pink_daisy_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.SNOWDROP_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/snowdrop_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.SOMBERCUP_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/sombercup_nectar_bowl")));
            generators.itemModelOutput.accept(BovinesItems.TROPICAL_BLUE_NECTAR_BOWL, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/tropical_blue_nectar_bowl")));

            generators.itemModelOutput.accept(BovinesItems.MOOBLOOM_SPAWN_EGG, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/moobloom_spawn_egg")));
            generators.itemModelOutput.accept(BovinesItems.BIRD_OF_PARADISE, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/bird_of_paradise")));
            generators.itemModelOutput.accept(BovinesItems.BUTTERCUP, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/buttercup")));
            generators.itemModelOutput.accept(BovinesItems.CAMELLIA, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/camellia")));
            generators.itemModelOutput.accept(BovinesItems.CHARGELILY, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/chargelily")));
            generators.itemModelOutput.accept(BovinesItems.FREESIA, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/freesia")));
            generators.itemModelOutput.accept(BovinesItems.HYACINTH, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/hyacinth")));
            generators.itemModelOutput.accept(BovinesItems.LIMELIGHT, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/limelight")));
            generators.itemModelOutput.accept(BovinesItems.LINGHOLM, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/lingholm")));
            generators.itemModelOutput.accept(BovinesItems.NIGHTSHADE, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/nightshade")));
            generators.itemModelOutput.accept(BovinesItems.PINK_DAISY, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/pink_daisy")));
            generators.itemModelOutput.accept(BovinesItems.SNOWDROP, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/snowdrop")));
            generators.itemModelOutput.accept(BovinesItems.SOMBERCUP, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/sombercup")));
            generators.itemModelOutput.accept(BovinesItems.TROPICAL_BLUE, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/tropical_blue")));
            generators.itemModelOutput.accept(BovinesItems.CUSTOM_FLOWER, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/missing_flower")));
            generators.itemModelOutput.accept(BovinesItems.CUSTOM_MUSHROOM, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/missing_mushroom")));
            generators.itemModelOutput.accept(BovinesItems.CUSTOM_MUSHROOM_BLOCK, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/missing_mushroom_block")));
            generators.itemModelOutput.accept(BovinesItems.RICH_HONEY_BOTTLE, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("item/rich_honey_bottle")));
            generators.itemModelOutput.accept(BovinesItems.RICH_HONEY_BLOCK, ItemModelUtils.plainModel(BovinesAndButtercups.asResource("block/rich_honey_block")));
            generators.itemModelOutput.accept(BovinesItems.FLOWER_CROWN, ItemModelUtils.specialModel(FlowerCrownItemRenderer.BASE, FlowerCrownItemRenderer.Unbaked.INSTANCE));
        }
    }
}
