package house.greenhouse.bovinesandbuttercups.access;

import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.GenerationStep;

public interface ChunkGeneratorAccess {
    GenerationStep.Decoration bovinesandbuttercups$getStep();
    StructureManager bovinesandbuttercups$getStructureManager();
}
