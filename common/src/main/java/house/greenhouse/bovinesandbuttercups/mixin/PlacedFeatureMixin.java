package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.access.ChunkGeneratorAccess;
import house.greenhouse.bovinesandbuttercups.content.worldgen.RanchStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlacedFeature.class)
public abstract class PlacedFeatureMixin {
    @Shadow @Final private Holder<ConfiguredFeature<?, ?>> feature;

    @Inject(method = "placeWithContext", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V"), cancellable = true)
    private void bovinesandbuttercups$cancelPlacementIfInRanch(PlacementContext context, RandomSource randomSource, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!(context.getLevel() instanceof WorldGenRegion region) || ((ChunkGeneratorAccess)context.generator()).bovinesandbuttercups$getStep() != GenerationStep.Decoration.VEGETAL_DECORATION || ((ChunkGeneratorAccess)context.generator()).bovinesandbuttercups$getStructureManager() == null)
            return;
        for (var structureSet : ((ServerChunkCache)region.getChunkSource()).getGeneratorState().possibleStructureSets()) {
            for (var structure : structureSet.value().structures()) {
                if (structure.structure().value() instanceof RanchStructure ranchStructure && (ranchStructure.getAllowedFeatures().isPresent() && !ranchStructure.getAllowedFeatures().get().contains(feature)) && ((ChunkGeneratorAccess)context.generator()).bovinesandbuttercups$getStructureManager().checkStructurePresence(new ChunkPos(pos), ranchStructure, structureSet.value().placement(), false) != StructureCheckResult.START_NOT_PRESENT)
                    cir.setReturnValue(false);
            }
        }
    }
}
