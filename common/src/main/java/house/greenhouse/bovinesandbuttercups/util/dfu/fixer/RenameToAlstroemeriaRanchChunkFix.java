package house.greenhouse.bovinesandbuttercups.util.dfu.fixer;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.fixes.References;

public class RenameToAlstroemeriaRanchChunkFix extends DataFix {
    public RenameToAlstroemeriaRanchChunkFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.writeFixAndRead("Rename Bird of Paradise Ranch structure in chunks", getInputSchema().getType(References.CHUNK), getOutputSchema().getType(References.CHUNK), dynamic ->
                dynamic.setFieldIfPresent("structures", dynamic.get("structures").map(dynamic1 -> {
                    dynamic1 = dynamic1.setFieldIfPresent("starts", dynamic1.get("starts").map(dynamic2 -> {
                        if (dynamic2.get("bovinesandbuttercups:ranch/bird_of_paradise").result().isPresent()) {
                            dynamic2 = dynamic2.set("bovinesandbuttercups:ranch/alstroemeria", dynamic2.get("bovinesandbuttercups:ranch/bird_of_paradise").map(dynamic3 ->
                                    dynamic3.set("id", dynamic3.createString("bovinesandbuttercups:ranch/alstroemeria"))
                            ).result().orElseThrow()).remove("bovinesandbuttercups:ranch/bird_of_paradise");
                        }
                        return dynamic2;
                    }).result());
                    return dynamic1.setFieldIfPresent("References", dynamic1.get("References").map(dynamic2 -> {
                        if (dynamic2.get("bovinesandbuttercups:ranch/bird_of_paradise").result().isPresent()) {
                            dynamic2 = dynamic2.set("bovinesandbuttercups:ranch/alstroemeria", dynamic2.get("bovinesandbuttercups:ranch/bird_of_paradise").result().get())
                                    .remove("bovinesandbuttercups:ranch/bird_of_paradise");
                        }
                        return dynamic2;
                    }).result());
                }).result()));
    }
}