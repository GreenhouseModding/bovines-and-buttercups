package house.greenhouse.bovinesandbuttercups.content.worldgen;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class BovinesStructureTypes {
    public static final StructureType<RanchStructure> RANCH = register(BovinesAndButtercups.asResource("ranch"), () -> RanchStructure.CODEC);

    public static void registerAll() {}

    private static <T extends Structure> StructureType<T> register(ResourceLocation id, StructureType<T> structureType) {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, id, structureType);
    }
}
