package house.greenhouse.bovinesandbuttercups.content.block;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BovinesBlocks {
    public static final FlowerBlock BUTTERCUP = new FlowerBlock(MobEffects.POISON, 12, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock PINK_DAISY = new FlowerBlock(MobEffects.DAMAGE_BOOST, 3, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock LIMELIGHT = new FlowerBlock(MobEffects.REGENERATION, 8, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock BIRD_OF_PARADISE = new FlowerBlock(MobEffects.SLOW_FALLING, 6, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock CHARGELILY = new FlowerBlock(MobEffects.MOVEMENT_SPEED, 4, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock HYACINTH = new FlowerBlock(MobEffects.POISON, 12, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock SNOWDROP = new FlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 5, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock TROPICAL_BLUE = new FlowerBlock(MobEffects.FIRE_RESISTANCE, 4, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock FREESIA = new FlowerBlock(MobEffects.WATER_BREATHING, 8, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock LINGHOLM = new FlowerBlock(MobEffects.MOVEMENT_SPEED, 4, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final FlowerBlock CAMELLIA = new FlowerBlock(MobEffects.SATURATION, 0.35F, BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));

    public static final FlowerPotBlock POTTED_BUTTERCUP = new FlowerPotBlock(BUTTERCUP, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_PINK_DAISY = new FlowerPotBlock(PINK_DAISY, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_LIMELIGHT = new FlowerPotBlock(LIMELIGHT, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_BIRD_OF_PARADISE = new FlowerPotBlock(BIRD_OF_PARADISE, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_CHARGELILY = new FlowerPotBlock(CHARGELILY, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_HYACINTH = new FlowerPotBlock(HYACINTH, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_SNOWDROP = new FlowerPotBlock(SNOWDROP, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_TROPICAL_BLUE = new FlowerPotBlock(TROPICAL_BLUE, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_FREESIA = new FlowerPotBlock(FREESIA, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_LINGHOLM = new FlowerPotBlock(LINGHOLM, BlockBehaviour.Properties.of().instabreak().noOcclusion());
    public static final FlowerPotBlock POTTED_CAMELLIA = new FlowerPotBlock(CAMELLIA, BlockBehaviour.Properties.of().instabreak().noOcclusion());

    public static final Block RICH_HONEY_BLOCK = new RichHoneyBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).speedFactor(0.4F).jumpFactor(0.5F).noOcclusion().sound(SoundType.HONEY_BLOCK));

    public static final CustomFlowerBlock CUSTOM_FLOWER = new CustomFlowerBlock(BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final CustomFlowerPotBlock POTTED_CUSTOM_FLOWER = new CustomFlowerPotBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion());

    public static final CustomMushroomBlock CUSTOM_MUSHROOM = new CustomMushroomBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel((value) -> 1));
    public static final CustomHugeMushroomBlock CUSTOM_MUSHROOM_BLOCK = new CustomHugeMushroomBlock(BlockBehaviour.Properties.of().strength(0.2F).sound(SoundType.WOOD));
    public static final CustomMushroomPotBlock POTTED_CUSTOM_MUSHROOM = new CustomMushroomPotBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion());

    public static final PlaceableEdibleBlock PLACEABLE_EDIBLE = new PlaceableEdibleBlock(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY).dynamicShape().lightLevel(value -> value.getValue(PlaceableEdibleBlock.LIGHT)));

    public static void registerAll(RegistrationCallback<Block> callback) {
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("buttercup"), BUTTERCUP);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("pink_daisy"), PINK_DAISY);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("limelight"), LIMELIGHT);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("bird_of_paradise"), BIRD_OF_PARADISE);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("chargelily"), CHARGELILY);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("hyacinth"), HYACINTH);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("snowdrop"), SNOWDROP);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("tropical_blue"), TROPICAL_BLUE);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("freesia"), FREESIA);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("lingholm"), LINGHOLM);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("camellia"), CAMELLIA);

        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_buttercup"), POTTED_BUTTERCUP);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_pink_daisy"), POTTED_PINK_DAISY);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_limelight"), POTTED_LIMELIGHT);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_bird_of_paradise"), POTTED_BIRD_OF_PARADISE);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_chargelily"), POTTED_CHARGELILY);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_hyacinth"), POTTED_HYACINTH);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_snowdrop"), POTTED_SNOWDROP);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_tropical_blue"), POTTED_TROPICAL_BLUE);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_freesia"), POTTED_FREESIA);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_lingholm"), POTTED_LINGHOLM);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_camellia"), POTTED_CAMELLIA);

        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("rich_honey_block"), RICH_HONEY_BLOCK);

        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("custom_flower"), CUSTOM_FLOWER);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_custom_flower"), POTTED_CUSTOM_FLOWER);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("custom_mushroom"), CUSTOM_MUSHROOM);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("custom_mushroom_block"), CUSTOM_MUSHROOM_BLOCK);
        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("potted_custom_mushroom"), POTTED_CUSTOM_MUSHROOM);

        callback.register(BuiltInRegistries.BLOCK, BovinesAndButtercups.asResource("placeable_edible"), PLACEABLE_EDIBLE);
    }
}