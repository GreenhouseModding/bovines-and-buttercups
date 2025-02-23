package house.greenhouse.bovinesandbuttercups.util.dfu;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import house.greenhouse.bovinesandbuttercups.mixin.DataFixTypesAccessor;
import house.greenhouse.bovinesandbuttercups.util.dfu.fixer.*;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.*;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public record BovinesDataFixer(DataFixer fixer) {
    // Bump to the nearest rounded down 100 on the later version if two versions are being maintained.
    public static final int CURRENT_VERSION = 101;
    private static final BiFunction<Integer, Schema, Schema> SAME = Schema::new;
    private static final BiFunction<Integer, Schema, Schema> SAME_NAMESPACED = NamespacedSchema::new;
    private static BovinesDataFixer instance;

    public static final Map<String, String> RENAMED_BIRD_OF_PARADISE_BLOCKS = ImmutableMap.<String, String>builder()
            .put("bovinesandbuttercups:bird_of_paradise", "bovinesandbuttercups:alstroemeria")
            .put("bovinesandbuttercups:potted_bird_of_paradise", "bovinesandbuttercups:potted_alstroemeria")
            .build();
    public static final Map<String, String> RENAMED_BIRD_OF_PARADISE_ITEMS = ImmutableMap.<String, String>builder()
            .put("bovinesandbuttercups:bird_of_paradise", "bovinesandbuttercups:alstroemeria")
            .put("bovinesandbuttercups:bird_of_paradise_nectar_bowl", "bovinesandbuttercups:alstroemeria_nectar_bowl")
            .build();
    public static final Map<String, String> RENAMED_EDIBLE_TYPE_RECIPES = ImmutableMap.<String, String>builder()
            .put("bovinesandbuttercups:bird_of_paradise_cupcake", "bovinesandbuttercups:alstroemeria_cupcake")
            .build();

    public static BovinesDataFixer get() {
        if (instance == null)
            instance = new BovinesDataFixer(registerFixers());
        return instance;
    }

    @Deprecated
    public static void register() {}

    private static DataFixer registerFixers() {
        DataFixerBuilder builder = new DataFixerBuilder(CURRENT_VERSION);
        builder.addSchema(0, (integer, schema) -> DataFixers.getDataFixer()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion())));
        Schema schema100 = builder.addSchema(100, SAME_NAMESPACED);
        builder.addFixer(new NectarDecomponentizeFix(schema100));;
        Schema schema100_1 = builder.addSchema(100, 1, SAME_NAMESPACED);
        builder.addFixer(new NectarDecomponentizeEquipmentFix(schema100_1));
        Schema schema101 = builder.addSchema(101, SAME_NAMESPACED);
        builder.addFixer(BlockRenameFix.create(schema101, "Rename Bird of Paradise blocks", createRenamer(RENAMED_BIRD_OF_PARADISE_BLOCKS)));
        Schema schema101_1 = builder.addSchema(101, 1, SAME_NAMESPACED);
        builder.addFixer(ItemRenameFix.create(schema101_1, "Rename Bird of Paradise items", createRenamer(RENAMED_BIRD_OF_PARADISE_ITEMS)));
        Schema schema101_2 = builder.addSchema(101, 2, SAME_NAMESPACED);
        builder.addFixer(new RenameToAlstroemeriaCupcakeComponentFix(schema101_2));
        Schema schema101_3 = builder.addSchema(101, 3, SAME_NAMESPACED);
        builder.addFixer(new RenameToAlstroemeriaFlowerCrownComponentFix(schema101_3));
        Schema schema101_4 = builder.addSchema(101, 4, SAME_NAMESPACED);
        builder.addFixer(new VanillaEntityEquipmentFix(schema101_4, "Fix Bird of Paradise Flower Crown equipment", "bovinesandbuttercups:flower_crown", RenameToAlstroemeriaFlowerCrownComponentFix::updateDynamic));
        Schema schema101_5 = builder.addSchema(101, 5, SAME_NAMESPACED);
        builder.addFixer(new VanillaEntityEquipmentFix(schema101_5, "Fix Bird of Paradise Cupcake equipment", "bovinesandbuttercups:edible_type", RenameToAlstroemeriaCupcakeComponentFix::updateDynamic));
        Schema schema101_6 = builder.addSchema(101, 6, SAME_NAMESPACED);
        builder.addFixer(new NamespacedTypeRenameFix(schema101_6, "Rename Bird of Paradise Cupcake recipe", References.RECIPE, createRenamer(RENAMED_EDIBLE_TYPE_RECIPES)));
        Schema schema101_8 = builder.addSchema(101, 8, SAME_NAMESPACED);
        builder.addFixer(new RenameToAlstroemeriaRanchChunkFix(schema101_8));
        return builder.build().fixer();
    }

    @Deprecated(forRemoval = true, since = "2.2.0")
    public <T> Dynamic<T> updateWithFixers(DataFixTypes types, Dynamic<T> dynamic, int originalMinecraftVersion) {
        return updateWithFixers(types, dynamic);
    }

    public <T> Dynamic<T> updateWithFixers(DataFixTypes types, Dynamic<T> dynamic) {
        return fixer.update(((DataFixTypesAccessor)(Object)types).bovinesandbuttercups$getType(), dynamic, getModDataVersion(dynamic), CURRENT_VERSION);
    }

    @Deprecated(forRemoval = true, since = "2.2.0")
    public static <T> int getModDataVersion(Dynamic<T> dynamic, int originalMinecraftVersion) {
        return getModDataVersion(dynamic);
    }

    public static <T> int getModDataVersion(Dynamic<T> dynamic) {
        return dynamic.get("bovinesandbuttercups:data_version").asInt(0);
    }

    public static int getModDataVersion(CompoundTag dynamic) {
        return dynamic.getInt("bovinesandbuttercups:data_version");
    }

    public static CompoundTag setModDataVersion(CompoundTag tag) {
        tag.putInt("bovinesandbuttercups:data_version", CURRENT_VERSION);
        return tag;
    }

    private static UnaryOperator<String> createRenamer(Map<String, String> renameMap) {
        return name -> renameMap.getOrDefault(NamespacedSchema.ensureNamespaced(name), name);
    }
}
