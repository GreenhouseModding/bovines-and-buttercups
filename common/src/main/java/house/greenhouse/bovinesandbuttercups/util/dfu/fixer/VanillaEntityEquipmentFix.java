package house.greenhouse.bovinesandbuttercups.util.dfu.fixer;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.References;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class VanillaEntityEquipmentFix extends DataFix {
    private final String name;
    private final String componentName;
    private final Function<Dynamic<?>, Dynamic<?>> dynamicFunction;

    public VanillaEntityEquipmentFix(Schema outputSchema, String name, String componentName, Function<Dynamic<?>, Dynamic<?>> dynamicFunction) {
        super(outputSchema, true);
        this.name = name;
        this.componentName = componentName;
        this.dynamicFunction = dynamicFunction;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return cap(getInputSchema().getTypeRaw(References.ITEM_STACK));
    }

    private <IS> TypeRewriteRule cap(Type<IS> itemStackType) {
        OpticFinder<List<IS>> armorItemsFinder = DSL.fieldFinder("ArmorItems", DSL.list(itemStackType));
        OpticFinder<List<IS>> handItemsFinder = DSL.fieldFinder("HandItems", DSL.list(itemStackType));
        OpticFinder<IS> bodyItemFinder = DSL.fieldFinder("body_armor_item", itemStackType);
        return fixTypeEverywhereTyped(name,
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
                                    if (dynamic.get("components").result().isPresent()) {
                                        Dynamic<?> componentsDynamic = dynamic.get("components").orElseEmptyList();
                                        componentsDynamic = componentsDynamic.setFieldIfPresent(componentName, componentsDynamic.get(componentName)
                                                .map(dynamicFunction::apply)
                                                .result());
                                        return dynamic.set("components", componentsDynamic);
                                    }
                                    return dynamic;
                                }));
                    return newTyped;
                });
    }

    private <IS> Typed<?> setDynamicList(Typed<?> typed, OpticFinder<List<IS>> opticFinder) {
        return typed.updateTyped(opticFinder, is ->
                is.update(DSL.remainderFinder(), dynamic -> {
                    if (dynamic.get("components").result().isPresent()) {
                        Dynamic<?> componentsDynamic = dynamic.get("components").orElseEmptyList();
                        componentsDynamic = componentsDynamic.setFieldIfPresent(componentName, componentsDynamic.get(componentName)
                                .map(dynamicFunction::apply)
                                .result());
                        return dynamic.set("components", componentsDynamic);
                    }
                    return dynamic;
                }));
    }
}
