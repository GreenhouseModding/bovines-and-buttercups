package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v1;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v100.NectarDecomponentizeFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.Objects;
import java.util.Optional;

public class LegacyNectarFix extends DataFix {
    public LegacyNectarFix(Schema outputSchema) {
        super(outputSchema, true);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        OpticFinder<Pair<String, String>> idFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        OpticFinder<?> componentsFinder = getInputSchema().getType(References.ITEM_STACK).findField("components");
        return fixTypeEverywhereTyped("Fix legacy nectar tag", getInputSchema().getType(References.ITEM_STACK), typed -> {
            Optional<Pair<String, String>> optional = typed.getOptional(idFinder);
            if (optional.isPresent() && Objects.equals(optional.get().getSecond(), "bovinesandbuttercups:nectar_bowl")) {
                Optional<? extends Typed<?>> componentTyped = typed.getOptionalTyped(componentsFinder);
                if (componentTyped.isPresent()) {
                    Typed<?> nonOptionalComponent = componentTyped.get();
                    Dynamic<?> components = nonOptionalComponent.getOrCreate(DSL.remainderFinder());
                    Dynamic<?> customData = components.get("minecraft:custom_data").orElseEmptyMap();
                    String nectarId = customData.get("Source").asString("bovinesandbuttercups:buttercup");

                    customData = components.get("minecraft:custom_data").orElseEmptyMap().remove("Source").remove("Effects");

                    boolean shouldRemoveCustomData = customData.asMap(Dynamic::getValue, Dynamic::getValue).isEmpty();
                    if (shouldRemoveCustomData)
                        components = components.remove("minecraft:custom_data");
                    else
                        components = components.set("minecraft:custom_data", customData);

                    nonOptionalComponent = nonOptionalComponent.set(DSL.remainderFinder(), components);
                    return typed.set(componentsFinder, nonOptionalComponent)
                            .set(idFinder, Pair.of(References.ITEM_NAME.typeName(), NectarDecomponentizeFix.COMPONENT_TO_ITEM.getOrDefault(nectarId, "bovinesandbuttercups:buttercup_nectar_bowl")));
                }
            }
            return typed;
        });
    }
}
