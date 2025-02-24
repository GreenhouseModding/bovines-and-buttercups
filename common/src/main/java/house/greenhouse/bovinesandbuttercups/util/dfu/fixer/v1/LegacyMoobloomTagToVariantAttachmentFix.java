package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v1;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

import java.util.Map;

public class LegacyMoobloomTagToVariantAttachmentFix extends NamedEntityFix {

    public LegacyMoobloomTagToVariantAttachmentFix(Schema outputSchema) {
        super(outputSchema, false, "Fix legacy Moobloom tag", References.ENTITY, "bovinesandbuttercups:moobloom");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> {
            Dynamic<?> attachments = dynamic.get(BovinesAndButtercups.getHelper().getAttachmentKey()).orElseEmptyMap();
            if (dynamic.get("Type").result().isPresent()) {
                if (dynamic.get("PreviousType").result().isPresent()) {
                    Dynamic<?> variantAttachment = dynamic.createMap(Map.of())
                            .set("current", dynamic.get("Type").result().get())
                            .set("previous", dynamic.get("PreviousType").result().get());
                    dynamic = dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments
                            .set("bovinesandbuttercups:cow_variant", variantAttachment))
                            .remove("Type").remove("PreviousType");
                } else {
                    dynamic = dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments
                                    .set("bovinesandbuttercups:cow_variant", dynamic.get("Type").result().get()))
                            .remove("Type");
                }
            }
            return dynamic.setFieldIfPresent("allow_shearing", dynamic.get("AllowShearing").result())
                    .setFieldIfPresent("pollinated_reset_ticks", dynamic.get("PollinatedResetTicks").result());
        });
    }
}
