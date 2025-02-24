package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v1;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;

public class CowVariantFix extends NamedEntityFix {
    public CowVariantFix(Schema outputSchema) {
        super(outputSchema, false, "Fix cow to bovines cow variant", References.ENTITY, "minecraft:cow");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> {
            Dynamic<?> attachments = dynamic.get(BovinesAndButtercups.getHelper().getAttachmentKey()).orElseEmptyMap();
            return dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments.set("bovinesandbuttercups:cow_variant", dynamic.createString("bovinesandbuttercups:default_cow")));
        });
    }
}
