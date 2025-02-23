package house.greenhouse.bovinesandbuttercups.util.dfu;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.serialization.Dynamic;
import house.greenhouse.bovinesandbuttercups.mixin.DataFixTypesAccessor;
import house.greenhouse.bovinesandbuttercups.util.dfu.fixer.*;
import house.greenhouse.bovinesandbuttercups.util.dfu.schema.BovinesSchemaV1;
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
    // Bump this up by 100 for each new Minecraft version. If you're exceeding 99 schemas in the same version, you're doing something wrong.
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

        // Bovines 2.1.0
        Schema schema1 = builder.addSchema(2, BovinesSchemaV1::new);
        builder.addFixer(new RenameToCowVariantAttachment(schema1));

        // MC 1.21.4
        Schema schema100 = builder.addSchema(100, SAME_NAMESPACED);
        builder.addFixer(new MoobloomAttributeIdPrefixFix(schema100)); // Implemented to make sure that Mooblooms have their attributes converted just like everything else.
        Schema schema100_1 = builder.addSchema(100, 1, SAME_NAMESPACED);
        builder.addFixer(new NectarDecomponentizeFix(schema100_1));
        builder.addFixer(new NectarDecomponentizeEquipmentFix(schema100_1));

        // Bovines 2.2.0
        Schema schema101 = builder.addSchema(101, SAME_NAMESPACED);
        builder.addFixer(BlockRenameFix.create(schema101, "Rename Bird of Paradise blocks", createRenamer(RENAMED_BIRD_OF_PARADISE_BLOCKS)));
        builder.addFixer(ItemRenameFix.create(schema101, "Rename Bird of Paradise items", createRenamer(RENAMED_BIRD_OF_PARADISE_ITEMS)));
        builder.addFixer(new RenameToAlstroemeriaCowVariantAttachment(schema101));
        builder.addFixer(new RenameToAlstroemeriaPlaceableEdibleFix(schema101));
        builder.addFixer(new RenameToAlstroemeriaCupcakeComponentFix(schema101));
        builder.addFixer(new RenameToAlstroemeriaFlowerCrownComponentFix(schema101));
        builder.addFixer(new RenameToAlstroemeriaRanchChunkFix(schema101));
        builder.addFixer(new VanillaEntityEquipmentFix(schema101, "Fix Bird of Paradise Flower Crown equipment", "bovinesandbuttercups:flower_crown", RenameToAlstroemeriaFlowerCrownComponentFix::updateDynamic));
        builder.addFixer(new VanillaEntityEquipmentFix(schema101, "Fix Bird of Paradise Cupcake equipment", "bovinesandbuttercups:edible_type", RenameToAlstroemeriaCupcakeComponentFix::updateDynamic));
        builder.addFixer(new NamespacedTypeRenameFix(schema101, "Rename Bird of Paradise Cupcake recipe", References.RECIPE, createRenamer(RENAMED_EDIBLE_TYPE_RECIPES)));

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

    public static TypeTemplate equipment(Schema schema) {
        return DSL.optionalFields(
                "ArmorItems",
                DSL.list(References.ITEM_STACK.in(schema)),
                "HandItems",
                DSL.list(References.ITEM_STACK.in(schema)),
                "body_armor_item",
                References.ITEM_STACK.in(schema)
        );
    }

}
