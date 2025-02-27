package house.greenhouse.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.BreedCowWithVariantTrigger;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParamSets;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParams;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MooshroomChildTypeUtil {
    public static Pair<Holder<CowVariant<MooshroomConfiguration>>, Optional<Holder<CowVariant<MooshroomConfiguration>>>> chooseMooshroomBabyVariant(MushroomCow parent, MushroomCow other, MushroomCow child, @Nullable Player player) {
        List<Holder<CowVariant<MooshroomConfiguration>>> eligibleCowTypes = new ArrayList<>();

        for (Holder.Reference<CowVariant<?>> cowVariant : parent.level().registryAccess().registryOrThrow(BovinesRegistryKeys.COW_VARIANT).holders().filter(type -> type.isBound() && type.value().type() == BovinesCowTypes.MOOSHROOM_TYPE && ((MooshroomConfiguration)type.value().configuration()).settings().offspringConditions() != OffspringConditions.EMPTY).toList()) {
            Holder<CowVariant<MooshroomConfiguration>> mooshroomVariant = (Holder) cowVariant;
            var conditions = mooshroomVariant.value().configuration().settings().offspringConditions();

            LootParams.Builder params = new LootParams.Builder((ServerLevel) parent.level());
            params.withParameter(LootContextParams.THIS_ENTITY, parent);
            params.withParameter(BovinesLootContextParams.PARTNER, other);
            params.withParameter(BovinesLootContextParams.CHILD, child);
            params.withParameter(LootContextParams.ORIGIN, parent.position());
            params.withParameter(BovinesLootContextParams.BREEDING_TYPE, cowVariant);
            LootContext thisContext = new LootContext.Builder(params.create(BovinesLootContextParamSets.BREEDING)).create(Optional.empty());

            params.withParameter(LootContextParams.THIS_ENTITY, other);
            params.withParameter(BovinesLootContextParams.PARTNER, parent);
            LootContext otherContext = new LootContext.Builder(params.create(BovinesLootContextParamSets.BREEDING)).create(Optional.empty());

            if (conditions.thisConditions().stream().allMatch(condition -> condition.test(thisContext))
                    && conditions.otherConditions().stream().allMatch(condition -> condition.test(otherContext))
                    || (conditions.thisConditions().stream().allMatch(condition -> condition.test(otherContext))
                    && conditions.otherConditions().stream().allMatch(condition -> condition.test(thisContext))))
                eligibleCowTypes.add(mooshroomVariant);
        }

        if (!eligibleCowTypes.isEmpty()) {
            int random = parent.getRandom().nextInt(eligibleCowTypes.size());
            var randomType = eligibleCowTypes.get(random);

            createParticles(child, randomType, parent.position());

            if (parent.getLoveCause() != null)
                BreedCowWithVariantTrigger.INSTANCE.trigger(parent.getLoveCause(), parent, other, child, true, (Holder) randomType);
            return randomType.value().configuration().settings().offspringConditions().inheritance().handleInheritance(randomType, BovinesAndButtercups.getHelper().getCowVariantAttachment(parent), BovinesAndButtercups.getHelper().getCowVariantAttachment(other));
        }

        BovinesAndButtercups.getHelper().clearParticlePositions(child);

        var parentType = CowVariantAttachment.getCowVariantHolderFromEntity(parent, BovinesCowTypes.MOOSHROOM_TYPE);
        var otherType = CowVariantAttachment.getCowVariantHolderFromEntity(other, BovinesCowTypes.MOOSHROOM_TYPE);
        if (parentType == null || otherType == null)
            return null;

        if (!otherType.equals(parentType) && parent.getRandom().nextBoolean()) {
            if (parent.getLoveCause() != null)
                BreedCowWithVariantTrigger.INSTANCE.trigger(parent.getLoveCause(), parent, other, child, false, (Holder<CowVariant<?>>)(Holder<?>)otherType);
            return Pair.of(otherType, Optional.ofNullable(CowVariantAttachment.getPreviousCowVariantHolderFromEntity(other, BovinesCowTypes.MOOSHROOM_TYPE)));
        }

        if (parent.getLoveCause() != null)
            BreedCowWithVariantTrigger.INSTANCE.trigger(parent.getLoveCause(), parent, other, child, false, (Holder<CowVariant<?>>)(Holder<?>)parentType);
        return Pair.of(parentType, Optional.ofNullable(CowVariantAttachment.getPreviousCowVariantHolderFromEntity(parent, BovinesCowTypes.MOOSHROOM_TYPE)));
    }

    private static void createParticles(MushroomCow child, Holder<CowVariant<MooshroomConfiguration>> type, Vec3 parentPos) {
        if (!type.isBound() || type.value().configuration().settings().particle().isEmpty())
            return;

        if (BovinesAndButtercups.getHelper().getParticlePositions(child).isEmpty() && !child.level().isClientSide())
            ((ServerLevel)child.level()).sendParticles(type.value().configuration().settings().particle().get(), child.getX(), child.getY(0.5), child.getZ(), 6, 0.05, 0.05, 0.05, 0.01);

        for (Vec3 pos : BovinesAndButtercups.getHelper().getParticlePositions(child).get(type))
            createParticleTrail(child.level(), child.position(), pos, parentPos, type.value().configuration().settings().particle().get());

        BovinesAndButtercups.getHelper().clearParticlePositions(child);
    }

    private static void createParticleTrail(LevelAccessor level, Vec3 childPos, Vec3 pos, Vec3 parentPos, ParticleOptions options) {
        double value = (1 - (1 / (pos.distanceTo(childPos) + 1))) / 4;

        for (double d = 0.0; d < 1.0; d += value)
            ((ServerLevel)level).sendParticles(options, Mth.lerp(d, pos.x(), parentPos.x()), Mth.lerp(d, pos.y(), parentPos.y()), Mth.lerp(d, pos.z(), parentPos.z()), 1, 0.05, 0.05,  0.05, 0.01);
    }

}
