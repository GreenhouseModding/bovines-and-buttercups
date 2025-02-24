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
import java.util.Optional;

public class LegacyMooshroomTypeToAttachmentsFix extends NamedEntityFix {
    public LegacyMooshroomTypeToAttachmentsFix(Schema outputSchema) {
        super(outputSchema, false, "Fix legacy mooshroom variant attachment/component/capability", References.ENTITY, "minecraft:mooshroom");
    }

    @Override
    protected Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> {
            // 1.20.4
            Dynamic<?> attachments = dynamic.get(BovinesAndButtercups.getHelper().getAttachmentKey()).orElseEmptyMap();
            OptionalDynamic<?> mooshroomTypeAttachment = attachments.get("bovinesandbuttercups:mooshroom_type");
            if (mooshroomTypeAttachment.result().isPresent()) {
                Dynamic<?> newDynamic = convert(dynamic, attachments, mooshroomTypeAttachment.result().get());
                return newDynamic
                        .set(BovinesAndButtercups.getHelper().getAttachmentKey(), newDynamic.get(BovinesAndButtercups.getHelper().getAttachmentKey()).orElseEmptyMap().remove("bovinesandbuttercups:mooshroom_type"));
            }
            // < 1.20.4 Fabric
            OptionalDynamic<?> cardinalComponents = dynamic.get("cardinal_components");
            if (cardinalComponents.result().isPresent()) {
                OptionalDynamic<?> mooshroomTypeComponent = cardinalComponents.result().get().get("bovinesandbuttercups:mooshroom_type");
                if (mooshroomTypeComponent.result().isPresent()) {
                    return convert(dynamic, attachments, mooshroomTypeComponent.result().get())
                            .set("cardinal_components", cardinalComponents.result().get().remove("bovinesandbuttercups:mooshroom_type"));
                }
            }
            // < 1.20.4 (Neo)Forge
            OptionalDynamic<?> capabilities = dynamic.get("ForgeCaps");
            if (capabilities.result().isPresent()) {
                OptionalDynamic<?> mooshroomTypeCapability = capabilities.result().get().get("bovinesandbuttercups:mooshroom_type");
                if (mooshroomTypeCapability.result().isPresent()) {
                    return convert(dynamic, attachments, mooshroomTypeCapability.result().get())
                            .set("ForgeCaps", capabilities.result().get().remove("bovinesandbuttercups:mooshroom_type"));
                }
            }
            return dynamic;
        });
    }

    private static Dynamic<?> convert(Dynamic<?> dynamic, Dynamic<?> attachments, Dynamic<?> old) {
        Dynamic<?> variantAttachment = dynamic.createMap(Map.of())
                .setFieldIfPresent("current", old.get("Type").result())
                .setFieldIfPresent("previous", old.get("PreviousType").result());
        Optional<Dynamic<?>> extrasAttachment = old.get("AllowShearing").result().filter(dynamic1 -> !dynamic1.asBoolean(true))
                .map(dynamic1 -> dynamic.createMap(Map.of()).set("allow_shearing", dynamic1));
        return dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments
                .set("bovinesandbuttercups:cow_variant", variantAttachment)
                .setFieldIfPresent("bovinesandbuttercups:mooshroom_extras", extrasAttachment));
    }
}
