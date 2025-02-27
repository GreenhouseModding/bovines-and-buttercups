package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v3;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import net.minecraft.util.datafix.fixes.References;

public class RenameToAlstroemeriaNectarComponentFix extends DataFix {
    public RenameToAlstroemeriaNectarComponentFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return fixTypeEverywhereTyped("Rename Bird of Paradise Nectar component",
                getInputSchema().getType(References.DATA_COMPONENTS),
                getOutputSchema().getType(References.DATA_COMPONENTS),
                typed -> typed.set(DSL.remainderFinder(), updateDynamic(typed.get(DSL.remainderFinder()))));
    }

    public static Dynamic<?> updateDynamic(Dynamic<?> dynamic) {
        if (dynamic.get("bovinesandbuttercups:nectar").result().isPresent()) {
            if (dynamic.get("bovinesandbuttercups:nectar").result().get().asString().isSuccess() && dynamic.get("bovinesandbuttercups:nectar").result().get().asString().getOrThrow().equals("bovinesandbuttercups:bird_of_paradise")) {
                return dynamic.set("bovinesandbuttercups:nectar", dynamic.createString("bovinesandbuttercups:alstroemeria"));
            }
        }
        return dynamic;
    }
}
