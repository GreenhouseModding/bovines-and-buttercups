package house.greenhouse.bovinesandbuttercups.util.dfu.fixer.v1;

import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import net.minecraft.util.datafix.fixes.References;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LegacyNectarFix extends DataFix {
    public LegacyNectarFix(Schema outputSchema) {
        super(outputSchema, true);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.writeFixAndRead(
                "Fix legacy nectar tag",
                this.getInputSchema().getType(References.ITEM_STACK),
                this.getOutputSchema().getType(References.ITEM_STACK),
                LegacyNectarFix::updateDynamic
        );
    }

    public static Dynamic<?> updateDynamic(Dynamic<?> dynamic) {
        if (dynamic.get("id").asString("").equals("bovinesandbuttercups:nectar_bowl")) {
            Dynamic<?> components = dynamic.get("components").orElseEmptyMap();

            var source = components.get("minecraft:custom_data").orElseEmptyMap().get("Source");
            components = components.set("bovinesandbuttercups:nectar", source.result().orElse((Dynamic) dynamic.createString("bovinesandbuttercups:buttercup")));

            var customData = components.get("minecraft:custom_data").orElseEmptyMap().remove("Source").remove("Effects");
            boolean shouldRemoveCustomData = customData.asMap(Dynamic::getValue, Dynamic::getValue).isEmpty();

            if (shouldRemoveCustomData)
                components = components.remove("minecraft:custom_data");
            else
                components = components.set("minecraft:custom_data", customData);

            return dynamic
                    .set("components", components);
        }
        return dynamic;
    }
}
