package house.greenhouse.bovinesandbuttercups.util.dfu.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SequencedMap;
import java.util.function.Supplier;

public class BovinesV2440 extends NamespacedSchema {
    public BovinesV2440(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    public static SequencedMap<String, Supplier<TypeTemplate>> components() {
        SequencedMap<String, Supplier<TypeTemplate>> sequencedMap = new LinkedHashMap<>();
        sequencedMap.put("bovinesandbuttercups:nectar", () -> DSL.constType(NamespacedSchema.namespacedString()));
        return sequencedMap;
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> map, Map<String, Supplier<TypeTemplate>> map2) {
        super.registerTypes(schema, map, map2);
        schema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(components()));
    }
}
