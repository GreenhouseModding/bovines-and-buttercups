package house.greenhouse.bovinesandbuttercups.mixin;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Entity.class, priority = 500) // Prioritise before Fabric.
public abstract class EntityMixin {
    @Shadow public abstract Level level();

    @ModifyVariable(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"), argsOnly = true)
    private CompoundTag bovinesandbuttercups$loadFromLegacyAttachments(CompoundTag tag) {
        // DFU at home!!!
        if ((Entity)(Object)this instanceof LivingEntity living && BovinesRegistries.COW_TYPE.stream().anyMatch(type -> type.isApplicable(living)) && tag.contains(BovinesAndButtercups.getHelper().getAttachmentKey())) {
            CompoundTag attachmentTag = tag.getCompound(BovinesAndButtercups.getHelper().getAttachmentKey());
            if (attachmentTag.contains("bovinesandbuttercups:cow_type"))
                attachmentTag.put("bovinesandbuttercups:cow_variant", attachmentTag.get("bovinesandbuttercups:cow_type"));
            if (attachmentTag.contains("bovinesandbuttercups:cow_variant")) {
                RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, level().registryAccess());

                if (attachmentTag.get("bovinesandbuttercups:cow_variant") instanceof CompoundTag compound) {
                    var current = CowVariant.CODEC.decode(ops, compound.get("current")).result().map(Pair::getFirst);
                    var previous = CowVariant.CODEC.decode(ops, compound.get("previous")).result().map(Pair::getFirst);
                    Tag copiedAttachmentTag = attachmentTag.copy();
                    if (current.isEmpty() && compound.getString("current").equals("bovinesandbuttercups:bird_of_paradise")) {
                        ((CompoundTag)copiedAttachmentTag).putString("current", "bovinesandbuttercups:alstroemeria");
                    }
                    if (previous.isEmpty() && compound.contains("previous") && compound.getString("previous").equals("bovinesandbuttercups:bird_of_paradise")) {
                        ((CompoundTag)copiedAttachmentTag).putString("previous", "bovinesandbuttercups:alstroemeria");
                        attachmentTag.put("bovinesandbuttercups:cow_variant", copiedAttachmentTag);
                    }
                } else if (attachmentTag.get("bovinesandbuttercups:cow_variant") instanceof StringTag stringTag) {
                    var current = CowVariant.CODEC.decode(ops, stringTag).result().map(Pair::getFirst);
                    if (current.isEmpty() && stringTag.getAsString().equals("bovinesandbuttercups:bird_of_paradise"))
                        attachmentTag.putString("bovinesandbuttercups:cow_variant", "bovinesandbuttercups:alstroemeria");
                }
            }
        }
        return tag;
    }
}