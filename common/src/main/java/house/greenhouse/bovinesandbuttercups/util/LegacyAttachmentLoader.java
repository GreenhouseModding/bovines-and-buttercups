package house.greenhouse.bovinesandbuttercups.util;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class LegacyAttachmentLoader {
    public static void loadFromLegacyAttachment(LivingEntity entity, Tag tag) {
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, entity.level().registryAccess());
        if (tag instanceof CompoundTag compound)
            BovinesAndButtercups.getHelper().setCowVariantAttachment(entity, new CowVariantAttachment(
                    CowVariant.CODEC.decode(ops, compound.get("current")).getOrThrow().getFirst(),
                    !compound.contains("previous") ? Optional.empty() : Optional.of(CowVariant.CODEC.decode(ops, compound.get("previous")).getOrThrow().getFirst())
            ));
        else if (tag instanceof StringTag stringTag)
            BovinesAndButtercups.getHelper().setCowVariantAttachment(entity, new CowVariantAttachment(
                    CowVariant.CODEC.decode(ops, stringTag).getOrThrow().getFirst(),
                    Optional.empty()
            ));
    }
}
