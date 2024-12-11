package house.greenhouse.bovinesandbuttercups.content.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CowSubPredicate(Optional<Holder<CowType<?>>> type, Optional<Boolean> hasSnow) implements EntitySubPredicate {
    public static final MapCodec<CowSubPredicate> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            RegistryFixedCodec.create(BovinesRegistryKeys.COW_TYPE).optionalFieldOf("cow_type").forGetter(CowSubPredicate::type),
            Codec.BOOL.optionalFieldOf("has_snow").forGetter(CowSubPredicate::hasSnow)
    ).apply(inst, CowSubPredicate::new));

    @Override
    public MapCodec<? extends EntitySubPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        if (!(entity instanceof LivingEntity living))
            return false;

        if (level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_TYPE).stream().noneMatch(cowType -> cowType.type().isApplicable(entity)))
            return false;

        if (type.isPresent()) {
            CowTypeAttachment attachment = BovinesAndButtercups.getHelper().getCowTypeAttachment(living);
            if (attachment == null)
                return false;
            if (attachment.cowType().isBound() && attachment.cowType().value().type().isApplicable(entity) && attachment.cowType().is(type.get()))
                return false;
        }

        if (hasSnow.isPresent()) {
            CowTypeAttachment attachment = BovinesAndButtercups.getHelper().getCowTypeAttachment(living);
            if (attachment == null)
                return false;
            if (attachment.cowType().isBound() && !attachment.cowType().value().configuration().hasSnow(entity))
                return false;
        }

        return true;
    }
}
