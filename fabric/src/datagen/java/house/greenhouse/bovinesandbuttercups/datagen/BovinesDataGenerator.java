package house.greenhouse.bovinesandbuttercups.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Lifecycle;
import house.greenhouse.bovinesandbuttercups.api.BovinesConventionalTags;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeType;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.BreedCowWithTypeTrigger;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.LockEffectTrigger;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.PreventEffectTrigger;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdible;
import house.greenhouse.bovinesandbuttercups.content.component.ItemNectar;
import house.greenhouse.bovinesandbuttercups.content.data.edible.BovinesEdibleBlockTypes;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.Nectar;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.BovinesNectars;
import house.greenhouse.bovinesandbuttercups.content.recipe.SuspiciousEdibleRecipe;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.content.data.flowercrown.FlowerCrownMaterial;
import house.greenhouse.bovinesandbuttercups.content.recipe.FlowerCrownRecipe;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
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
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.recipes.RecipeBuilder;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        pack.addProvider(BiomeTagProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(ConfiguredFeatureTagProvider::new);
        pack.addProvider(EdibleBlockTypeTagProvider::new);
        pack.addProvider(EntityTypeTagProvider::new);
        pack.addProvider(FlowerCrownMaterialTagProvider::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(NectarTagProvider::new);

        pack.addProvider(ModelProvider::new);
    }

    @Override
    public String getEffectiveModId() {
        return BovinesAndButtercups.MOD_ID;
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(BovinesRegistryKeys.NECTAR, BovinesNectars::bootstrap);
        registryBuilder.add(BovinesRegistryKeys.COW_TYPE, BovinesCowTypes::bootstrap);
        registryBuilder.add(BovinesRegistryKeys.FLOWER_CROWN_MATERIAL, BovinesFlowerCrownMaterials::bootstrap);
        registryBuilder.add(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE, BovinesEdibleBlockTypes::bootstrap);
    }

    private static class DynamicRegistryProvider extends FabricDynamicRegistryProvider {

        public DynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
        }

        @Override
        protected void configure(HolderLookup.Provider registries, Entries entries) {
            BovinesNectars.bootstrap(createContext(registries, entries));
            BovinesCowTypes.bootstrap(createContext(registries, entries));
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
            ItemStack lockEffectStack = new ItemStack(BovinesItems.NECTAR_BOWL);
            lockEffectStack.set(BovinesDataComponents.NECTAR, new ItemNectar(lookup.asGetterLookup().lookupOrThrow(BovinesRegistryKeys.NECTAR).getOrThrow(BovinesNectars.BUTTERCUP)));
            ItemStack preventEffectStack = new ItemStack(BovinesItems.NECTAR_BOWL);
            lockEffectStack.set(BovinesDataComponents.NECTAR, new ItemNectar(lookup.asGetterLookup().lookupOrThrow(BovinesRegistryKeys.NECTAR).getOrThrow(BovinesNectars.FREESIA)));
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
                            true)
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
                            true)
                    )
                    .parent(ResourceLocation.withDefaultNamespace("husbandry/breed_an_animal"))
                    .requirements(AdvancementRequirements.allOf(List.of("breed_new_moobloom")))
                    .addCriterion("breed_new_moobloom", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(true))))
                    .build(BovinesAndButtercups.asResource("husbandry/breed_new_moobloom")));

            HolderLookup.RegistryLookup<CowType<?>> cowTypeRegistry = lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE);

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
                            "bovinesandbuttercups:buttercup",
                            "bovinesandbuttercups:chargelily",
                            "bovinesandbuttercups:freesia",
                            "bovinesandbuttercups:hyacinth",
                            "bovinesandbuttercups:limelight",
                            "bovinesandbuttercups:lingholm",
                            "bovinesandbuttercups:pink_daisy",
                            "bovinesandbuttercups:snowdrop",
                            "bovinesandbuttercups:tropical_blue"
                    )))
                    .addCriterion("bovinesandbuttercups:bird_of_paradise", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.BIRD_OF_PARADISE)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:buttercup", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.BUTTERCUP)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:chargelily", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.CHARGELILY)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:freesia", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.FREESIA)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:hyacinth", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.HYACINTH)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:limelight", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.LIMELIGHT)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:lingholm", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.LINGHOLM)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:pink_daisy", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.PINK_DAISY)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:snowdrop", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.SNOWDROP)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .addCriterion("bovinesandbuttercups:tropical_blue", BreedCowWithTypeTrigger.INSTANCE.createCriterion(new BreedCowWithTypeTrigger.TriggerInstance(Optional.empty(), (Optional<Holder<CowTypeType<?>>>)(Optional<?>)lookup.lookupOrThrow(BovinesRegistryKeys.COW_TYPE_TYPE).get(ResourceKey.create(BovinesRegistryKeys.COW_TYPE_TYPE, BovinesAndButtercups.asResource("moobloom"))), HolderSet.direct(cowTypeRegistry.getOrThrow(BovinesCowTypes.MoobloomKeys.TROPICAL_BLUE)), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())))
                    .build(BovinesAndButtercups.asResource("husbandry/breed_all_mooblooms")));
            consumer.accept(Advancement.Builder.advancement()
                    .display(new DisplayInfo(
                            FlowerCrownItem.createRainbowCrown(lookup),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.obtain_flower_crown.title"),
                            Component.translatable("advancements.husbandry.bovinesandbuttercups.husbandry.obtain_flower_crown.description"),
                            Optional.empty(),
                            AdvancementType.TASK,
                            true,
                            true,
                            true)
                    )
                    .parent(ResourceLocation.withDefaultNamespace("husbandry/root"))
                    .requirements(AdvancementRequirements.allOf(List.of("get_flower_crown")))
                    .addCriterion("get_flower_crown", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BovinesItems.FLOWER_CROWN)))
                    .build(BovinesAndButtercups.asResource("husbandry/obtain_flower_crown")));
        }
    }

    private static class RecipeProvider extends FabricRecipeProvider {
        private final CompletableFuture<HolderLookup.Provider> registries;

        public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
            this.registries = lookup;
        }

        @Override
        public void buildRecipes(RecipeOutput output) {
            HolderLookup.Provider lookup = registries.join();

            oneToOneConversionRecipe(output, Items.ORANGE_DYE, BovinesBlocks.BIRD_OF_PARADISE, "orange_dye");
            oneToOneConversionRecipe(output, Items.YELLOW_DYE, BovinesBlocks.BUTTERCUP, "yellow_dye");
            oneToOneConversionRecipe(output, Items.LIGHT_BLUE_DYE, BovinesBlocks.CHARGELILY, "light_blue_dye");
            oneToOneConversionRecipe(output, Items.RED_DYE, BovinesBlocks.FREESIA, "red_dye");
            oneToOneConversionRecipe(output, Items.PURPLE_DYE, BovinesBlocks.HYACINTH, "purple_dye");
            oneToOneConversionRecipe(output, Items.LIME_DYE, BovinesBlocks.LIMELIGHT, "lime_dye");
            oneToOneConversionRecipe(output, Items.CYAN_DYE, BovinesBlocks.LINGHOLM, "cyan_dye");
            oneToOneConversionRecipe(output, Items.PINK_DYE, BovinesBlocks.PINK_DAISY, "pink_dye");
            oneToOneConversionRecipe(output, Items.WHITE_DYE, BovinesBlocks.SNOWDROP, "white_dye");
            oneToOneConversionRecipe(output, Items.BLUE_DYE, BovinesBlocks.TROPICAL_BLUE, "blue_dye");

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SUGAR, 3)
                    .requires(BovinesItems.RICH_HONEY_BOTTLE)
                    .group("sugar")
                    .unlockedBy("has_rich_honey_bottle", has(BovinesItems.RICH_HONEY_BOTTLE))
                    .save(output, getConversionRecipeName(Items.SUGAR, BovinesItems.RICH_HONEY_BOTTLE));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, BovinesItems.RICH_HONEY_BOTTLE, 4)
                    .requires(BovinesItems.RICH_HONEY_BLOCK)
                    .requires(Items.GLASS_BOTTLE, 4)
                    .unlockedBy("has_rich_honey_block", has(BovinesBlocks.RICH_HONEY_BLOCK))
                    .save(output);
            twoByTwoPacker(output, RecipeCategory.REDSTONE, BovinesBlocks.RICH_HONEY_BLOCK, BovinesItems.RICH_HONEY_BOTTLE);

            SpecialRecipeBuilder.special(FlowerCrownRecipe::new).save(output, BovinesAndButtercups.asResource("flower_crown"));
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
            dropSelf(BovinesBlocks.CHARGELILY);
            dropSelf(BovinesBlocks.FREESIA);
            dropSelf(BovinesBlocks.HYACINTH);
            dropSelf(BovinesBlocks.LIMELIGHT);
            dropSelf(BovinesBlocks.LINGHOLM);
            dropSelf(BovinesBlocks.PINK_DAISY);
            dropSelf(BovinesBlocks.SNOWDROP);
            dropSelf(BovinesBlocks.TROPICAL_BLUE);

            dropPottedContents(BovinesBlocks.POTTED_BIRD_OF_PARADISE);
            dropPottedContents(BovinesBlocks.POTTED_BUTTERCUP);
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

    private static class BiomeTagProvider extends FabricTagProvider<Biome> {
        public BiomeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, Registries.BIOME, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_BIRD_OF_PARADISE))
                    .forceAddTag(ConventionalBiomeTags.IS_SAVANNA);
            ((FabricTagBuilder)tag(BovinesTags.BiomeTags.HAS_RANCH_STRUCTURE_BUTTERCUP))
                    .forceAddTag(ConventionalBiomeTags.IS_FLOWER_FOREST);
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
            ((FabricTagBuilder)tag(BlockTags.SMALL_FLOWERS))
                    .forceAddTag(BovinesTags.BlockTags.MOOBLOOM_FLOWERS);

            ((FabricTagBuilder)tag(BovinesTags.BlockTags.DOES_NOT_STICK_RICH_HONEY_BLOCK))
                    .add(reverseLookup(Blocks.SLIME_BLOCK))
                    .add(reverseLookup(Blocks.HONEY_BLOCK));
            tag(BovinesTags.BlockTags.MOOBLOOM_FLOWERS)
                    .add(
                            reverseLookup(BovinesBlocks.BIRD_OF_PARADISE),
                            reverseLookup(BovinesBlocks.BUTTERCUP),
                            reverseLookup(BovinesBlocks.CHARGELILY),
                            reverseLookup(BovinesBlocks.CUSTOM_FLOWER),
                            reverseLookup(BovinesBlocks.FREESIA),
                            reverseLookup(BovinesBlocks.HYACINTH),
                            reverseLookup(BovinesBlocks.LIMELIGHT),
                            reverseLookup(BovinesBlocks.LINGHOLM),
                            reverseLookup(BovinesBlocks.PINK_DAISY),
                            reverseLookup(BovinesBlocks.SNOWDROP),
                            reverseLookup(BovinesBlocks.TROPICAL_BLUE)
                    );
            tag(BovinesTags.BlockTags.SNOWDROP_PLACEABLE)
                    .add(
                            reverseLookup(Blocks.SNOW_BLOCK)
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
                    .add(BovinesEdibleBlockTypes.PINK_DAISY_CUPCAKE)
                    .add(BovinesEdibleBlockTypes.SNOWDROP_CUPCAKE)
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
                    .add(BovinesFlowerCrownMaterials.PINK_DAISY)
                    .add(BovinesFlowerCrownMaterials.SNOWDROP);
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
                            reverseLookup(BovinesItems.CHARGELILY),
                            reverseLookup(BovinesItems.CUSTOM_FLOWER),
                            reverseLookup(BovinesItems.FREESIA),
                            reverseLookup(BovinesItems.HYACINTH),
                            reverseLookup(BovinesItems.LIMELIGHT),
                            reverseLookup(BovinesItems.LINGHOLM),
                            reverseLookup(BovinesItems.PINK_DAISY),
                            reverseLookup(BovinesItems.SNOWDROP),
                            reverseLookup(BovinesItems.TROPICAL_BLUE)
                    );
            tag(ConventionalItemTags.FOODS)
                    .add(reverseLookup(BovinesItems.RICH_HONEY_BOTTLE));
            tag(BovinesConventionalTags.ConventionalItemTags.HONEY_FOODS)
                    .add(reverseLookup(Items.HONEY_BOTTLE))
                    .add(reverseLookup(BovinesItems.RICH_HONEY_BOTTLE));
        }
    }

    private static class NectarTagProvider extends FabricTagProvider<Nectar> {
        public NectarTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, BovinesRegistryKeys.NECTAR, lookup);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            tag(BovinesTags.NectarTags.CREATIVE_MENU_ORDER)
                    .add(BovinesNectars.FREESIA)
                    .add(BovinesNectars.BIRD_OF_PARADISE)
                    .add(BovinesNectars.BUTTERCUP)
                    .add(BovinesNectars.LIMELIGHT)
                    .add(BovinesNectars.LINGHOLM)
                    .add(BovinesNectars.CHARGELILY)
                    .add(BovinesNectars.TROPICAL_BLUE)
                    .add(BovinesNectars.HYACINTH)
                    .add(BovinesNectars.PINK_DAISY)
                    .add(BovinesNectars.SNOWDROP);
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
            createCupcakes(BovinesEdibleBlockTypes.CHARGELILY_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.FREESIA_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.HYACINTH_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.LIMELIGHT_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.LINGHOLM_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.PINK_DAISY_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.SNOWDROP_CUPCAKE, generators);
            createCupcakes(BovinesEdibleBlockTypes.TROPICAL_BLUE_CUPCAKE, generators);
            createPuffPastries(BovinesEdibleBlockTypes.BROWN_MUSHROOM_PUFF_PASTRY, generators);
            createPuffPastries(BovinesEdibleBlockTypes.RED_MUSHROOM_PUFF_PASTRY, generators);
            createPuffPastries(BovinesEdibleBlockTypes.BROWN_MUSHROOM_PUFF_PASTRY, generators, "suspicious_");
            createPuffPastries(BovinesEdibleBlockTypes.RED_MUSHROOM_PUFF_PASTRY, generators, "suspicious_");

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
            createPuffPastries(type, generators, "");
        }

        public static void createPuffPastries(ResourceKey<EdibleBlockType> type, BlockModelGenerators generators, String prefix) {
            var mapping = new TextureMapping().put(TextureSlot.ALL, type.location().withPath(s -> "block/" + prefix + s));

            String plural = prefix + type.location().getPath().substring(0, type.location().getPath().length() - 1) + "ies";

            PUFF_PASTRY.create(type.location().withPath(s -> "block/" + prefix + s), mapping, generators.modelOutput);
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
            // No-op
        }

        @Override
        public <T> CompletableFuture<?> saveCollection(CachedOutput output, Map<T, ? extends Supplier<JsonElement>> objectToJsonMap, Function<T, Path> resolveObjectPath) {
            return CompletableFuture.allOf(objectToJsonMap.entrySet().stream().map((entry) -> {
                Path path = resolveObjectPath.apply(entry.getKey());
                JsonElement jsonElement = (JsonElement)((Supplier)entry.getValue()).get();
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    jsonObject.addProperty("render_type", "cutout");
                }
                return DataProvider.saveStable(output, jsonElement, path);
            }).toArray(CompletableFuture[]::new));
        }
    }
}
