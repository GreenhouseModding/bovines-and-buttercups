package house.greenhouse.bovinesandbuttercups.api.util;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.ConvertData;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConversionUtil {
    /**
     * Any entities that were converted by bovines.
     * Used within indirect implementations, such as mixins or events that reference multiple options at once.
     */
    public static final List<Entity> CONVERTED_BY_BOVINES = new ArrayList<>();

    public static boolean convert(Mob entity, ServerLevel level, List<WeightedEntry.Wrapper<ConvertData>> compatibleList) {
        return convert(entity, level, null, compatibleList);
    }

    public static boolean convert(Mob entity, ServerLevel level, @Nullable LightningBolt bolt, List<WeightedEntry.Wrapper<ConvertData>> compatibleList) {
        if (BovinesAndButtercups.getHelper().getCowVariantAttachment(entity) == null)
            return false;
        if (compatibleList.isEmpty()) {
            return false;
        } else if (compatibleList.size() == 1) {
            return convert(entity, level, bolt, compatibleList.getFirst().data().variant(), compatibleList.getFirst().data().setPrevious());
        } else {
            int totalWeight = level.getRandom().nextInt(compatibleList.stream().map(holderWrapper -> holderWrapper.weight().asInt()).reduce(Integer::sum).orElse(0));
            for (WeightedEntry.Wrapper<ConvertData> cct : compatibleList) {
                totalWeight -= cct.weight().asInt();
                if (totalWeight < 0) {
                    return convert(entity, level, bolt, cct.data().variant(), cct.data().setPrevious());
                }
            }
        }
        return true;
    }

    public static boolean revertFromPrevious(Mob entity, ServerLevel level) {
        return revertFromPrevious(entity, level, null);
    }

    public static boolean revertFromPrevious(Mob entity, ServerLevel level, @Nullable LightningBolt bolt) {
        CowVariantAttachment attachment = BovinesAndButtercups.getHelper().getCowVariantAttachment(entity);
        if (attachment == null || attachment.previousCowVariant().isEmpty())
            return false;
        Holder<CowVariant<?>> previous = attachment.previousCowVariant().get();
        if (!previous.isBound())
            return false;
        if (!previous.value().type().isApplicable(entity)) {
            BovinesAndButtercups.getHelper().getCowVariantAttachment(entity).cowVariant().value().configuration().preConversion(entity);
            return entity.convertTo(previous.value().type().getDefaultEntityType(), ConversionParams.single(entity, false, false), cow -> {
                if (bolt != null)
                    previous.value().configuration().postConversion(entity, cow, bolt);
                BovinesAndButtercups.getHelper().runNeoForgeConversionEventPost(entity, cow);
                cow.finalizeSpawn(level, level.getCurrentDifficultyAt(cow.blockPosition()), EntitySpawnReason.CONVERSION, null);
                CowVariantAttachment.setCowVariant(cow, (Holder) previous);
                CowVariantAttachment.sync(cow);
                if (bolt != null && previous.value().type().isLightningLogicIndirect()) {
                    CONVERTED_BY_BOVINES.add(cow);
                }
            }) != null;
        }
        BovinesAndButtercups.getHelper().getCowVariantAttachment(entity).cowVariant().value().configuration().preConversion(entity);
        CowVariantAttachment.setCowVariant(entity, (Holder) previous);
        CowVariantAttachment.sync(entity);
        if (bolt != null && previous.value().type().isLightningLogicIndirect())
            CONVERTED_BY_BOVINES.add(entity);
        previous.value().configuration().postConversion(entity, null, bolt);
        return true;
    }

    private static boolean convert(Mob entity, ServerLevel level, @Nullable LightningBolt bolt, Holder<CowVariant<?>> cowVariant, boolean setPrevious) {
        if (!cowVariant.isBound())
            return false;

        CowVariant<?> variant = cowVariant.value();
        if (!variant.type().isApplicable(entity)) {
            BovinesAndButtercups.getHelper().getCowVariantAttachment(entity).cowVariant().value().configuration().preConversion(entity);
            return entity.convertTo(variant.type().getDefaultEntityType(), ConversionParams.single(entity, false, false), cow -> {
                if (bolt != null)
                    variant.configuration().postConversion(entity, cow, bolt);
                BovinesAndButtercups.getHelper().runNeoForgeConversionEventPost(entity, cow);
                cow.finalizeSpawn(level, level.getCurrentDifficultyAt(cow.blockPosition()), EntitySpawnReason.CONVERSION, null);
                CowVariantAttachment.setCowVariant(cow, (Holder) cowVariant, !setPrevious ? Optional.empty() : (Optional) Optional.of(BovinesAndButtercups.getHelper().getCowVariantAttachment(entity).cowVariant()));
                CowVariantAttachment.sync(cow);
                if (bolt != null && variant.type().isLightningLogicIndirect()) {
                    CONVERTED_BY_BOVINES.add(cow);
                }
            }) != null;
        }
        BovinesAndButtercups.getHelper().getCowVariantAttachment(entity).cowVariant().value().configuration().preConversion(entity);
        CowVariantAttachment.setCowVariant(entity, (Holder) cowVariant, !setPrevious ? Optional.empty() : (Optional) Optional.of(BovinesAndButtercups.getHelper().getCowVariantAttachment(entity).cowVariant()));
        CowVariantAttachment.sync(entity);
        if (bolt != null && variant.type().isLightningLogicIndirect())
            CONVERTED_BY_BOVINES.add(entity);
        variant.configuration().postConversion(entity, null, bolt);
        return true;
    }
}
