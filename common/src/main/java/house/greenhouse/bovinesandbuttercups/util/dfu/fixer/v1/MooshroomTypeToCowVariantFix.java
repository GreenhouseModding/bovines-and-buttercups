package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v1;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

import java.util.Map;

// TODO: Figure out how to handle enum extensions for this.
public class MooshroomTypeToCowVariantFix extends NamedEntityFix {
    public MooshroomTypeToCowVariantFix(Schema outputSchema) {
        super(outputSchema, false, "Fix minecraft mooshroom variant tag to bovines variant", References.ENTITY, "minecraft:mooshroom");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> {
            Dynamic<?> attachments = dynamic.get(BovinesAndButtercups.getHelper().getAttachmentKey()).orElseEmptyMap();
            OptionalDynamic<?> mooshroomTypeAttachment = attachments.get("bovinesandbuttercups:mooshroom_type");
            if (mooshroomTypeAttachment.result().isEmpty())
                return convert(dynamic, attachments);
            return dynamic;
        });
    }

    private static Dynamic<?> convert(Dynamic<?> dynamic, Dynamic<?> attachments) {
        Dynamic<?> variantAttachment = dynamic.createMap(Map.of())
                .setFieldIfPresent("current", dynamic.get("Type").result().map(dynamic1 -> dynamic1.createString("bovinesandbuttercups:" + dynamic1.asString("red") + "_mushroom")));
        return dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments.set("bovinesandbuttercups:cow_variant", variantAttachment));
    }
}
