package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v3;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import net.minecraft.util.datafix.fixes.References;

public class RenameToAlstroemeriaCupcakeComponentFix extends DataFix {
    public RenameToAlstroemeriaCupcakeComponentFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return fixTypeEverywhereTyped("Rename Bird of Paradise Edible Block Type component",
                getInputSchema().getType(References.DATA_COMPONENTS),
                getOutputSchema().getType(References.DATA_COMPONENTS),
                typed -> typed.set(DSL.remainderFinder(), updateDynamic(typed.get(DSL.remainderFinder()))));
    }

    public static Dynamic<?> updateDynamic(Dynamic<?> dynamic) {
        if (dynamic.get("bovinesandbuttercups:edible_type").result().isPresent()) {
            if (dynamic.get("bovinesandbuttercups:edible_type").result().get().asString().isSuccess() && dynamic.get("bovinesandbuttercups:edible_type").result().get().asString().getOrThrow().equals("bovinesandbuttercups:bird_of_paradise_cupcake")) {
                return dynamic.set("bovinesandbuttercups:edible_type", dynamic.createString("bovinesandbuttercups:alstroemeria_cupcake"));
            } else {
                var edibleType = dynamic.get("bovinesandbuttercups:edible_type");
                if (edibleType.result().isPresent()) {
                    Dynamic<?> edibleTypeDynamic = edibleType.get().getOrThrow();
                    OptionalDynamic<?> typeDynamic = edibleTypeDynamic.get("type");
                    if (typeDynamic.result().isPresent() &&
                            typeDynamic.result().get().asString().isSuccess() &&
                            typeDynamic.result().get().asString().getOrThrow().equals("bovinesandbuttercups:bird_of_paradise_cupcake"))
                        return dynamic.set("bovinesandbuttercups:edible_type", edibleTypeDynamic.set("type", edibleTypeDynamic.createString("bovinesandbuttercups:alstroemeria_cupcake")));
                }
            }
        }
        return dynamic;
    }
}
