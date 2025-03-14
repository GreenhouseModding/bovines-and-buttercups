package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v100;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NectarDecomponentizeFix extends DataFix {
    public NectarDecomponentizeFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    public static final Map<String, String> COMPONENT_TO_ITEM;

    @Override
    protected TypeRewriteRule makeRule() {
        OpticFinder<Pair<String, String>> idFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        OpticFinder<?> componentsFinder = getInputSchema().getType(References.ITEM_STACK).findField("components");
        return fixTypeEverywhereTyped("Decomponentize Nectar fixer", getInputSchema().getType(References.ITEM_STACK), typed -> {
            Optional<Pair<String, String>> optional = typed.getOptional(idFinder);
            if (optional.isPresent() && Objects.equals(optional.get().getSecond(), "bovinesandbuttercups:nectar_bowl")) {
                Optional<? extends Typed<?>> componentTyped = typed.getOptionalTyped(componentsFinder);
                if (componentTyped.isPresent()) {
                    Typed<?> nonOptionalComponent = componentTyped.get();
                    Dynamic<?> dynamic = nonOptionalComponent.getOrCreate(DSL.remainderFinder());
                    String nectarId = dynamic.get("bovinesandbuttercups:nectar").asString("bovinesandbuttercups:buttercup_nectar_bowl");
                    dynamic = dynamic.remove("bovinesandbuttercups:nectar");
                    nonOptionalComponent = nonOptionalComponent.set(DSL.remainderFinder(), dynamic);
                    return typed.set(componentsFinder, nonOptionalComponent)
                            .set(idFinder, Pair.of(References.ITEM_NAME.typeName(), COMPONENT_TO_ITEM.getOrDefault(nectarId, "bovinesandbuttercups:buttercup_nectar_bowl")));
                }
            }
            return typed;
        });
    }

    static {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("bovinesandbuttercups:alstoemeria", "bovinesandbuttercups:alstroemeria_nectar_bowl");
        builder.put("bovinesandbuttercups:bird_of_paradise", "bovinesandbuttercups:alstroemeria_nectar_bowl");
        builder.put("bovinesandbuttercups:buttercup", "bovinesandbuttercups:buttercup_nectar_bowl");
        builder.put("bovinesandbuttercups:camellia", "bovinesandbuttercups:camellia_nectar_bowl");
        builder.put("bovinesandbuttercups:chargelily", "bovinesandbuttercups:chargelily_nectar_bowl");
        builder.put("bovinesandbuttercups:freesia", "bovinesandbuttercups:freesia_nectar_bowl");
        builder.put("bovinesandbuttercups:hyacinth", "bovinesandbuttercups:hyacinth_nectar_bowl");
        builder.put("bovinesandbuttercups:limelight", "bovinesandbuttercups:limelight_nectar_bowl");
        builder.put("bovinesandbuttercups:lingholm", "bovinesandbuttercups:lingholm_nectar_bowl");
        builder.put("bovinesandbuttercups:pink_daisy", "bovinesandbuttercups:pink_daisy_nectar_bowl");
        builder.put("bovinesandbuttercups:snowdrop", "bovinesandbuttercups:snowdrop_nectar_bowl");
        builder.put("bovinesandbuttercups:sombercup", "bovinesandbuttercups:sombercup_nectar_bowl");
        builder.put("bovinesandbuttercups:tropical_blue", "bovinesandbuttercups:tropical_blue_nectar_bowl");
        COMPONENT_TO_ITEM = builder.build();
    }
}
