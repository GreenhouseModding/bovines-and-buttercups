package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v2;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.OptionalDynamic;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.util.datafix.fixes.References;

public class TypeAttachmentToVariantAttachmentFix extends DataFix {

    public TypeAttachmentToVariantAttachmentFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return fixTypeEverywhereTyped("Rename Cow Type attachment",
                getInputSchema().getType(References.ENTITY),
                typed -> typed.update(DSL.remainderFinder(), dynamic -> {
                    OptionalDynamic<?> attachments = dynamic.get(BovinesAndButtercups.getHelper().getAttachmentKey());
                    if (attachments.result().isPresent()) {
                        return dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments.result().get()
                                .setFieldIfPresent("bovinesandbuttercups:cow_variant", attachments.result().get().get("bovinesandbuttercups:cow_type").result()))
                                .remove("bovinesandbuttercups:cow_type");
                    }
                    return dynamic;
                }));
    }
}
