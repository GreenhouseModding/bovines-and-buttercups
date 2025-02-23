package house.greenhouse.bovinesandbuttercups.util.dfu.fixer;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.ExtraDataFixUtils;
import net.minecraft.util.datafix.fixes.NamedEntityFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class MoobloomAttributeIdPrefixFix extends NamedEntityFix {
    public MoobloomAttributeIdPrefixFix(Schema outputSchema) {
        super(outputSchema, false, "Fix prefixed Moobloom attributes", References.ENTITY, "bovinesandbuttercups:moobloom");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        Typed<?> newTyped = typed.update(
                DSL.remainderFinder(),
                dynamic -> dynamic.update(
                        "attributes",
                        dynamic1 -> DataFixUtils.orElse(
                                dynamic1.asStreamOpt().result().map(stream -> stream.map(this::fixIdField)).map(dynamic1::createList), dynamic1
                        )
                )
        );
        return newTyped;
    }

    private Dynamic<?> fixIdField(Dynamic<?> data) {
        return ExtraDataFixUtils.fixStringField(data, "id", MoobloomAttributeIdPrefixFix::replaceId);
    }

    private static String replaceId(String id) {
        String s = NamespacedSchema.ensureNamespaced(id);

        String s2 = NamespacedSchema.ensureNamespaced("generic.");
        if (s.startsWith(s2)) {
            return "minecraft:" + s.substring(s2.length());
        }

        return id;
    }
}
