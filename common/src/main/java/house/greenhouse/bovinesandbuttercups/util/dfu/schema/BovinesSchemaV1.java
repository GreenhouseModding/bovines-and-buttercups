package house.greenhouse.bovinesandbuttercups.util.dfu.schema;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import house.greenhouse.bovinesandbuttercups.util.dfu.BovinesDataFixer;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.Map;
import java.util.function.Supplier;

public class BovinesSchemaV1 extends NamespacedSchema {
    public BovinesSchemaV1(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
        schema.register(map, "bovinesandbuttercups:moobloom", () -> BovinesDataFixer.equipment(schema));
        return map;
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
        schema.registerSimple(map, "bovinesandbuttercups:placeable_edible");
        return map;
    }

}
