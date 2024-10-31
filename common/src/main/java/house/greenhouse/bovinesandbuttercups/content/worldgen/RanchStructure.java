package house.greenhouse.bovinesandbuttercups.content.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.mixin.JigsawStructureAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class RanchStructure extends JigsawStructure {
    public static final int MAX_TOTAL_STRUCTURE_RANGE = 128;
    public static final MapCodec<RanchStructure> CODEC = RecordCodecBuilder.<RanchStructure>mapCodec(builder -> builder.group(
            settingsCodec(builder),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(RanchStructure::getStartPool),
            ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(RanchStructure::getStartJigsawName),
            Codec.intRange(0, 20).fieldOf("size").forGetter(RanchStructure::getMaxDepth),
            HeightProvider.CODEC.fieldOf("start_height").forGetter(RanchStructure::getStartHeight),
            Codec.BOOL.fieldOf("use_expansion_hack").forGetter(RanchStructure::shouldUseExpansionHack),
            Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(RanchStructure::getProjectStartToHeightmap),
            Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(RanchStructure::getMaxDistanceFromCenter),
            Codec.list(PoolAliasBinding.CODEC).optionalFieldOf("pool_aliases", List.of()).forGetter(RanchStructure::getPoolAliases),
            DimensionPadding.CODEC.optionalFieldOf("dimension_padding", DEFAULT_DIMENSION_PADDING).forGetter(RanchStructure::getDimensionPadding),
            LiquidSettings.CODEC.optionalFieldOf("liquid_settings", DEFAULT_LIQUID_SETTINGS).forGetter(RanchStructure::getLiquidSettings),
            RegistryCodecs.homogeneousList(Registries.CONFIGURED_FEATURE).optionalFieldOf("allowed_features").forGetter(RanchStructure::getAllowedFeatures),
            Codec.BOOL.optionalFieldOf("generate_in_fluids", true).forGetter(RanchStructure::isAbleToGenerateInFluids)
    ).apply(builder, RanchStructure::new)).flatXmap(verifyRange(), verifyRange());

    private final Optional<HolderSet<ConfiguredFeature<?, ?>>> allowedFeatures;
    private final boolean generateInFluids;
    private int currentlyGeneratingHeight;

    private static Function<RanchStructure, DataResult<RanchStructure>> verifyRange() {
        return (structure) -> {
            byte distanceModifier = switch (structure.terrainAdaptation()) {
                case NONE -> 0;
                case BURY, BEARD_THIN, BEARD_BOX, ENCAPSULATE -> 12;
            };

            return structure.getMaxDistanceFromCenter() + (int) distanceModifier > MAX_TOTAL_STRUCTURE_RANGE ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 128") : DataResult.success(structure);
        };
    }

    public RanchStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startHeight, boolean usesExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter, List<PoolAliasBinding> poolAliasBindings, DimensionPadding dimensionPadding, LiquidSettings liquidSettings, Optional<HolderSet<ConfiguredFeature<?, ?>>> allowedFeatures, boolean generateInFluids) {
        super(settings, startPool, startJigsawName, maxDepth, startHeight, usesExpansionHack, projectStartToHeightmap, maxDistanceFromCenter, poolAliasBindings, dimensionPadding, liquidSettings);
        this.generateInFluids = generateInFluids;
        this.allowedFeatures = allowedFeatures;
    }

    public Holder<StructureTemplatePool> getStartPool() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getStartPool();
    }

    public Optional<ResourceLocation> getStartJigsawName() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getStartJigsawName();
    }

    public int getMaxDepth() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getMaxDepth();
    }

    public HeightProvider getStartHeight() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getStartHeight();
    }

    public boolean shouldUseExpansionHack() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$shouldUseExpansionHack();
    }

    public Optional<Heightmap.Types> getProjectStartToHeightmap() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getProjectStartToHeightmap();
    }

    public int getMaxDistanceFromCenter() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getMaxDistanceFromCenter();
    }

    public List<PoolAliasBinding> getPoolAliases() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getPoolAliases();
    }

    public DimensionPadding getDimensionPadding() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getDimensionPadding();
    }

    public LiquidSettings getLiquidSettings() {
        return ((JigsawStructureAccessor)this).bovinesandbuttercups$getLiquidSettings();
    }

    public Optional<HolderSet<ConfiguredFeature<?, ?>>> getAllowedFeatures() {
        return allowedFeatures;
    }

    public boolean isAbleToGenerateInFluids() {
        return generateInFluids;
    }

    public int getCurrentlyGeneratingHeight() {
        return currentlyGeneratingHeight;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int height = this.getStartHeight().sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos pos = new BlockPos(chunkPos.getMinBlockX(), height, chunkPos.getMinBlockZ());
        currentlyGeneratingHeight = height;
        return JigsawPlacement.addPieces(context, this.getStartPool(), this.getStartJigsawName(), this.getMaxDepth(), pos, false, this.getProjectStartToHeightmap(), this.getMaxDistanceFromCenter(), PoolAliasLookup.EMPTY, this.getDimensionPadding(), this.getLiquidSettings());
    }

    public StructureType<?> type() {
        return BovinesStructureTypes.RANCH;
    }
}
