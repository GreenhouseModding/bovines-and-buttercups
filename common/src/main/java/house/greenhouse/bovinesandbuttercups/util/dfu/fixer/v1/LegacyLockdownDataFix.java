package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v1;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.util.datafix.fixes.References;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class LegacyLockdownDataFix extends DataFix {
    public LegacyLockdownDataFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return TypeRewriteRule.seq(
                fixTypeEverywhereTyped("Fix lockdown attachment for entities",
                        this.getInputSchema().getType(References.ENTITY),
                        this.getOutputSchema().getType(References.ENTITY),
                        this::fix),
                fixTypeEverywhereTyped("Fix lockdown attachment for players",
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
                return convertAttachment(dynamic, attachments, lockdownAttachment.result().get());
            }
            // < 1.20.4 Fabric
            OptionalDynamic<?> cardinalComponents = dynamic.get("cardinal_components");
            if (cardinalComponents.result().isPresent()) {
                OptionalDynamic<?> mooshroomTypeComponent = cardinalComponents.result().get().get("bovinesandbuttercups:lockdown");
                if (mooshroomTypeComponent.result().isPresent()) {
                    return convertComponability(dynamic, attachments, mooshroomTypeComponent.result().get())
                            .set("cardinal_components", cardinalComponents.result().get().remove("bovinesandbuttercups:lockdown"));
                }
            }
            // < 1.20.4 (Neo)Forge
            OptionalDynamic<?> capabilities = dynamic.get("ForgeCaps");
            if (capabilities.result().isPresent()) {
                OptionalDynamic<?> mooshroomTypeCapability = capabilities.result().get().get("bovinesandbuttercups:lockdown");
                if (mooshroomTypeCapability.result().isPresent()) {
                    return convertComponability(dynamic, attachments, mooshroomTypeCapability.result().get())
                            .set("ForgeCaps", capabilities.result().get().remove("bovinesandbuttercups:lockdown"));
                }
            }
            return dynamic;
        });
    }

    private static Dynamic<?> convertAttachment(Dynamic<?> dynamic, Dynamic<?> attachments, Dynamic<?> old) {
        Dynamic<?> lockdownAttachment = old.get("locked_effects").orElseEmptyList();
        return dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments
                .set("bovinesandbuttercups:lockdown", lockdownAttachment));
    }

    private static Dynamic<?> convertComponability(Dynamic<?> dynamic, Dynamic<?> attachments, Dynamic<?> old) {
        Map<Dynamic<?>, Dynamic<?>> lockdownAttachment = old.get("LockedEffects").asList(dynamic1 -> {
            if (dynamic1.get("Id").result().isPresent() && dynamic1.get("Duration").result().isPresent())
                return Pair.of(dynamic1.get("Id").result().get(), dynamic1.get("Duration").result().get());
            return null;
        }).stream().filter(Objects::nonNull).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        return dynamic.set(BovinesAndButtercups.getHelper().getAttachmentKey(), attachments
                .set("bovinesandbuttercups:lockdown", attachments.createMap(lockdownAttachment)));
    }
}
