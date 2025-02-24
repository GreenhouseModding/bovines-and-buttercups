package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v100;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NectarDecomponentizeEquipmentFix extends DataFix {
    public NectarDecomponentizeEquipmentFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    private static final Map<String, String> COMPONENT_TO_ITEM;

    @Override
    protected TypeRewriteRule makeRule() {
        return cap(getInputSchema().getTypeRaw(References.ITEM_STACK));
    }

    private <IS> TypeRewriteRule cap(Type<IS> itemStackType) {
        OpticFinder<List<IS>> armorItemsFinder = DSL.fieldFinder("ArmorItems", DSL.list(itemStackType));
        OpticFinder<List<IS>> handItemsFinder = DSL.fieldFinder("HandItems", DSL.list(itemStackType));
        OpticFinder<IS> bodyItemFinder = DSL.fieldFinder("body_armor_item", itemStackType);
        return fixTypeEverywhereTyped("Fix Nectar in equipment slots",
                this.getInputSchema().getType(References.ENTITY),
                this.getOutputSchema().getType(References.ENTITY),
                typed -> {
                    Typed<?> newTyped = typed;

                    Optional<List<IS>> armorItems = typed.getOptional(armorItemsFinder);
                    if (armorItems.isPresent())
                        newTyped = setDynamicList(typed, armorItemsFinder);
                    Optional<List<IS>> handItems = typed.getOptional(handItemsFinder);
                    if (handItems.isPresent())
                        newTyped = setDynamicList(typed, handItemsFinder);
                    Optional<IS> bodyArmorItem = typed.getOptional(bodyItemFinder);
                    if (bodyArmorItem.isPresent())
                        newTyped = typed.updateTyped(bodyItemFinder, is ->
                                is.update(DSL.remainderFinder(), dynamic -> {
                                    if (dynamic.get("components").result().isPresent() && dynamic.get("components").result().get().get("bovinesandbuttercups:nectar").result().isPresent() && dynamic.get("id").asString().result().isPresent() && dynamic.get("id").asString().result().get().equals("bovinesandbuttercups:nectar_bowl")) {
                                        Dynamic<?> componentsDynamic = dynamic.get("components").orElseEmptyList();
                                        String newId = COMPONENT_TO_ITEM.getOrDefault(componentsDynamic.get("bovinesandbuttercups:nectar").asString("bovinesandbuttercups:buttercup"), "bovinesandbuttercups:buttercup_nectar_bowl");
                                        componentsDynamic = componentsDynamic.remove("bovinesandbuttercups:nectar");
                                        return dynamic.set("components", componentsDynamic)
                                                .set("id", dynamic.createString(NamespacedSchema.ensureNamespaced(newId)));
                                    }
                                    return dynamic;
                                }));
                    return newTyped;
                });
    }

    private <IS> Typed<?> setDynamicList(Typed<?> typed, OpticFinder<List<IS>> opticFinder) {
        return typed.updateTyped(opticFinder, is -> is.update(DSL.remainderFinder(), dynamic -> {
            if (dynamic.get("components").result().isPresent() && dynamic.get("components").result().get().get("bovinesandbuttercups:nectar").result().isPresent() && dynamic.get("id").asString().result().isPresent() && dynamic.get("id").asString().result().get().equals("bovinesandbuttercups:nectar_bowl")) {
                Dynamic<?> componentsDynamic = dynamic.get("components").orElseEmptyList();
                String newId = COMPONENT_TO_ITEM.getOrDefault(componentsDynamic.get("bovinesandbuttercups:nectar").asString("bovinesandbuttercups:buttercup"), "bovinesandbuttercups:buttercup_nectar_bowl");
                componentsDynamic = componentsDynamic.remove("bovinesandbuttercups:nectar");
                return dynamic.set("components", componentsDynamic)
                        .set("id", dynamic.createString(NamespacedSchema.ensureNamespaced(newId)));
            }
            return dynamic;
        }));
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
