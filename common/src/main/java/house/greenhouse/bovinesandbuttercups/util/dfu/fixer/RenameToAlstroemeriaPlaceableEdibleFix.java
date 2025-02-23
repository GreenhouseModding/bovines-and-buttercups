package house.greenhouse.bovinesandbuttercups.util.dfu.fixer;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

public class RenameToAlstroemeriaPlaceableEdibleFix extends NamedEntityFix {
    public RenameToAlstroemeriaPlaceableEdibleFix(Schema outputSchema) {
        super(outputSchema, false, "Rename Bird of Paradise Cupcake block entity", References.BLOCK_ENTITY, "bovinesandbuttercups:placeable_edible");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.set(DSL.remainderFinder(), updateDynamic(typed.get(DSL.remainderFinder())));
    }

    public static Dynamic<?> updateDynamic(Dynamic<?> dynamic) {
        if (dynamic.get("data").result().isPresent()) {
            if (dynamic.get("data").result().get().asString().isSuccess() && dynamic.get("data").result().get().asString().getOrThrow().equals("bovinesandbuttercups:bird_of_paradise_cupcake")) {
                return dynamic.set("data", dynamic.createString("bovinesandbuttercups:alstroemeria_cupcake"));
            } else {
                var edibleType = dynamic.get("data");
                if (edibleType.result().isPresent()) {
                    Dynamic<?> edibleTypeDynamic = edibleType.get().getOrThrow();
                    OptionalDynamic<?> typeDynamic = edibleTypeDynamic.get("type");
                    if (typeDynamic.result().isPresent() &&
                            typeDynamic.result().get().asString().isSuccess() &&
                            typeDynamic.result().get().asString().getOrThrow().equals("bovinesandbuttercups:bird_of_paradise_cupcake"))
                        return dynamic.set("data", edibleTypeDynamic.set("type", edibleTypeDynamic.createString("bovinesandbuttercups:alstroemeria_cupcake")));
                }
            }
        }
        return dynamic;
    }
}
