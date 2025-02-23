package house.greenhouse.bovinesandbuttercups.util.dfu.fixer;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.OptionalDynamic;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.util.datafix.fixes.References;

public class RenameToAlstroemeriaCowVariantAttachment extends DataFix {

    public RenameToAlstroemeriaCowVariantAttachment(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return fixTypeEverywhereTyped("Rename Bird of Paradise Mooblooms",
                getInputSchema().getType(References.ENTITY),
                typed -> typed.update(DSL.remainderFinder(), dynamic -> {
                    OptionalDynamic<?> attachments = dynamic.get(BovinesAndButtercups.getHelper().getAttachmentKey());
                    if (attachments.result().isPresent()) {
                        return dynamic.setFieldIfPresent(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments.map(dynamic1 ->
                            dynamic1.setFieldIfPresent("bovinesandbuttercups:cow_variant", dynamic1.get("bovinesandbuttercups:cow_variant").map(dynamic2 -> {
                                if (dynamic2.asString().result().isPresent()) {
                                    if (dynamic2.asString().result().get().equals("bovinesandbuttercups:bird_of_paradise"))
                                        return dynamic2.createString("bovinesandbuttercups:alstroemeria");
                                    return dynamic2;
                                }
                                return dynamic2.setFieldIfPresent("current", dynamic2.get("current").map(dynamic3 -> {
                                    if (dynamic3.asString().result().isPresent() && dynamic3.asString().result().get().equals("bovinesandbuttercups:bird_of_paradise"))
                                        return dynamic3.createString("bovinesandbuttercups:alstroemeria");
                                    return dynamic3;
                                }).result()).setFieldIfPresent("previous", dynamic2.get("previous").map(dynamic3 -> {
                                    if (dynamic3.asString().result().isPresent() && dynamic3.asString().result().get().equals("bovinesandbuttercups:bird_of_paradise"))
                                        return dynamic3.createString("bovinesandbuttercups:alstroemeria");
                                    return dynamic3;
                                }).result());
                            }).result())
                        ).result());
                    }
                    return dynamic;
                }));
    }
}
