package house.greenhouse.bovinesandbuttercups.util.dfu.fixer;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.References;

import java.util.Optional;

public class RenameToAlstroemeriaFlowerCrownComponentFix extends DataFix {
    public RenameToAlstroemeriaFlowerCrownComponentFix(Schema outputSchema) {
        super(outputSchema, true);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return fixTypeEverywhereTyped("Rename bird of paradise flower crown component", getInputSchema().getType(References.DATA_COMPONENTS), typed -> {
            Dynamic<?> dynamic = typed.get(DSL.remainderFinder());
            Optional<? extends Dynamic<?>> flowerCrown = dynamic.get("bovinesandbuttercups:flower_crown").result();
            if (flowerCrown.isPresent()) {
                return typed.set(DSL.remainderFinder(), dynamic.set("bovinesandbuttercups:flower_crown", updateDynamic(flowerCrown.get())));
            }
            return typed;
        });
    }

    public static Dynamic<?> updateDynamic(Dynamic<?> dynamic) {
        dynamic = setDynamicValue(dynamic, "top_left");
        dynamic = setDynamicValue(dynamic, "top");
        dynamic = setDynamicValue(dynamic, "top_right");
        dynamic = setDynamicValue(dynamic, "center_left");
        dynamic = setDynamicValue(dynamic, "center_right");
        dynamic = setDynamicValue(dynamic, "bottom_left");
        dynamic = setDynamicValue(dynamic, "bottom");
        dynamic = setDynamicValue(dynamic, "bottom_right");
        return dynamic;
    }

    private static Dynamic<?> setDynamicValue(Dynamic<?> dynamic, String fieldName) {
        if (dynamic.get(fieldName).result().isPresent() && dynamic.get(fieldName).result().get().asString().getOrThrow().equals("bovinesandbuttercups:bird_of_paradise"))
            return dynamic.set(fieldName, dynamic.createString("bovinesandbuttercups:alstroemeria"));
        return dynamic;
    }
}
