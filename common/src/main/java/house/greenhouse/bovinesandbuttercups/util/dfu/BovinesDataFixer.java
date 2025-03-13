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
import house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v1.*;
import house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v3.*;
import house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v2.TypeAttachmentToVariantAttachmentFix;
import house.greenhouse.bovinesandbuttercups.util.dfu.schema.BovinesSchemaV1;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.BlockRenameFix;
import net.minecraft.util.datafix.fixes.ItemRenameFix;
import net.minecraft.util.datafix.fixes.NamespacedTypeRenameFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public record BovinesDataFixer(DataFixer fixer) {
    // Bump this up by 100 for each new Minecraft version. If you're exceeding 99 schemas in the same version, you're doing something wrong.
    public static final int CURRENT_VERSION = 3;
    private static final BiFunction<Integer, Schema, Schema> SAME = Schema::new;
    private static final BiFunction<Integer, Schema, Schema> SAME_NAMESPACED = NamespacedSchema::new;
    private static BovinesDataFixer instance;

    public static final Map<String, String> RENAMED_BIRD_OF_PARADISE_BLOCKS = ImmutableMap.<String, String>builder()
            .put("bovinesandbuttercups:bird_of_paradise", "bovinesandbuttercups:alstroemeria")
            .put("bovinesandbuttercups:potted_bird_of_paradise", "bovinesandbuttercups:potted_alstroemeria")
            .build();
    public static final Map<String, String> RENAMED_BIRD_OF_PARADISE_ITEMS = ImmutableMap.<String, String>builder()
            .put("bovinesandbuttercups:bird_of_paradise", "bovinesandbuttercups:alstroemeria")
            .build();
    public static final Map<String, String> RENAMED_BIRD_OF_PARADISE_RECIPES = ImmutableMap.<String, String>builder()
            .put("bovinesandbuttercups:orange_dye_from_bird_of_paradise", "bovinesandbuttercups:orange_dye_from_alstroemeria")
            .put("bovinesandbuttercups:bird_of_paradise_cupcake", "bovinesandbuttercups:alstroemeria_cupcake")
            .build();
    // Don't run DFU on everything. See: https://github.com/GreenhouseModding/bovines-and-buttercups/issues/11, which is caused by a Fabric API bug.
    public static final Set<DataFixTypes> AFFECTED_TYPES = Set.of(
            DataFixTypes.ADVANCEMENTS,
            DataFixTypes.CHUNK,
            DataFixTypes.ENTITY_CHUNK,
            DataFixTypes.HOTBAR,
            DataFixTypes.PLAYER,
            DataFixTypes.STRUCTURE
    );

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

        Schema schema1 = builder.addSchema(1, BovinesSchemaV1::new);
        builder.addFixer(new CowVariantFix(schema1));
        builder.addFixer(new MooshroomTypeToCowVariantFix(schema1));

        // Bovines 1.x.x -> Bovines 2.0.0
        Schema schema1_1 = builder.addSchema(1, 1, SAME_NAMESPACED);
        builder.addFixer(new LegacyMoobloomTagToVariantAttachmentFix(schema1_1));
        builder.addFixer(new LegacyMooshroomTypeToAttachmentsFix(schema1_1));
        Schema schema1_2 = builder.addSchema(1, 2, SAME_NAMESPACED);
        builder.addFixer(new LegacyLockdownDataFix(schema1_2));
        Schema schema1_3 = builder.addSchema(1, 3, SAME_NAMESPACED);
        builder.addFixer(new LegacyNectarFix(schema1_3));

        // Bovines 2.0.0 -> Bovines 2.1.0
        Schema schema2 = builder.addSchema(2, SAME_NAMESPACED);
        builder.addFixer(new TypeAttachmentToVariantAttachmentFix(schema2));

        // Bovines 2.1.0 -> Bovines 2.2.0
        Schema schema3 = builder.addSchema(3, SAME_NAMESPACED);
        builder.addFixer(BlockRenameFix.create(schema3, "Rename Bird of Paradise blocks", createRenamer(RENAMED_BIRD_OF_PARADISE_BLOCKS)));
        builder.addFixer(ItemRenameFix.create(schema3, "Rename Bird of Paradise items", createRenamer(RENAMED_BIRD_OF_PARADISE_ITEMS)));
        builder.addFixer(new RenameToAlstroemeriaCowVariantAttachment(schema3));
        builder.addFixer(new RenameToAlstroemeriaNectarComponentFix(schema3));
        builder.addFixer(new RenameToAlstroemeriaPlaceableEdibleFix(schema3));
        builder.addFixer(new RenameToAlstroemeriaCupcakeComponentFix(schema3));
        builder.addFixer(new RenameToAlstroemeriaFlowerCrownComponentFix(schema3));
        builder.addFixer(new RenameToAlstroemeriaRanchChunkFix(schema3));
        builder.addFixer(new VanillaEntityEquipmentFix(schema3, "Fix Bird of Paradise Nectar equipment", "bovinesandbuttercups:nectar", RenameToAlstroemeriaNectarComponentFix::updateDynamic));
        builder.addFixer(new VanillaEntityEquipmentFix(schema3, "Fix Bird of Paradise Flower Crown equipment", "bovinesandbuttercups:flower_crown", RenameToAlstroemeriaFlowerCrownComponentFix::updateDynamic));
        builder.addFixer(new VanillaEntityEquipmentFix(schema3, "Fix Bird of Paradise Cupcake equipment", "bovinesandbuttercups:edible_type", RenameToAlstroemeriaCupcakeComponentFix::updateDynamic));
        builder.addFixer(new NamespacedTypeRenameFix(schema3, "Rename Bird of Paradise Cupcake recipe", References.RECIPE, createRenamer(RENAMED_BIRD_OF_PARADISE_RECIPES)));

        return builder.build().fixer();
    }

    @Deprecated(forRemoval = true, since = "2.2.0")
    public <T> Dynamic<T> updateWithFixers(DataFixTypes types, Dynamic<T> dynamic, int originalMinecraftVersion) {
        return updateWithFixers(types, dynamic);
    }

    public <T> Dynamic<T> updateWithFixers(DataFixTypes types, Dynamic<T> dynamic) {
        if (AFFECTED_TYPES.contains(types))
            return fixer.update(((DataFixTypesAccessor)(Object)types).bovinesandbuttercups$getType(), dynamic, getModDataVersion(dynamic), CURRENT_VERSION);
        return dynamic;
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
