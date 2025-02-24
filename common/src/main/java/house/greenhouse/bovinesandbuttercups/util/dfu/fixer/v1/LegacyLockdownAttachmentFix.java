package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v1;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.util.datafix.fixes.References;

public class LegacyLockdownAttachmentFix extends DataFix {
    public LegacyLockdownAttachmentFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return TypeRewriteRule.seq(
                fixTypeEverywhereTyped("Fix Lockdown attachment for entities",
                        this.getInputSchema().getType(References.ENTITY),
                        this.getOutputSchema().getType(References.ENTITY),
                        this::fix),
                fixTypeEverywhereTyped("Fix Lockdown attachment for players",
                        this.getInputSchema().getType(References.PLAYER),
                        this.getOutputSchema().getType(References.PLAYER),
                        this::fix)
        );
    }

    private Typed<?> fix(Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> {
            // 1.20.4
            Dynamic<?> attachments = dynamic.get(BovinesAndButtercups.getHelper().getAttachmentKey()).orElseEmptyMap();
            OptionalDynamic<?> lockdownAttachment = attachments.get("bovinesandbuttercups:lockdown");
            if (lockdownAttachment.result().isPresent()) {
                return convert(dynamic, attachments, lockdownAttachment.result().get());
            }
            // < 1.20.4 Fabric
            OptionalDynamic<?> cardinalComponents = dynamic.get("cardinal_components");
            if (cardinalComponents.result().isPresent()) {
                OptionalDynamic<?> mooshroomTypeComponent = cardinalComponents.result().get().get("bovinesandbuttercups:lockdown");
                if (mooshroomTypeComponent.result().isPresent()) {
                    return convert(dynamic, attachments, mooshroomTypeComponent.result().get())
                            .set("cardinal_components", cardinalComponents.result().get().remove("bovinesandbuttercups:lockdown"));
                }
            }
            // < 1.20.4 (Neo)Forge
            OptionalDynamic<?> capabilities = dynamic.get("ForgeCaps");
            if (capabilities.result().isPresent()) {
                OptionalDynamic<?> mooshroomTypeCapability = capabilities.result().get().get("bovinesandbuttercups:lockdown");
                if (mooshroomTypeCapability.result().isPresent()) {
                    return convert(dynamic, attachments, mooshroomTypeCapability.result().get())
                            .set("ForgeCaps", capabilities.result().get().remove("bovinesandbuttercups:lockdown"));
                }
            }
            return dynamic;
        });
    }

    private static Dynamic<?> convert(Dynamic<?> dynamic, Dynamic<?> attachments, Dynamic<?> old) {
        Dynamic<?> lockdownAttachment = old.get("locked_effects").orElseEmptyList();
        return dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments
                .set("bovinesandbuttercups:lockdown", lockdownAttachment));
    }
}
