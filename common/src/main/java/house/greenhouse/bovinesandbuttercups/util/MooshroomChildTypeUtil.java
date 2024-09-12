package house.greenhouse.bovinesandbuttercups.util;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.cowtype.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.content.advancements.criterion.BreedCowWithTypeTrigger;
import house.greenhouse.bovinesandbuttercups.content.advancements.criterion.MutationTrigger;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import house.greenhouse.bovinesandbuttercups.registry.BovinesLootContextParamSets;
import house.greenhouse.bovinesandbuttercups.registry.BovinesLootContextParams;
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
    public static Holder<CowType<MooshroomConfiguration>> chooseMooshroomBabyType(MushroomCow parent, MushroomCow other, MushroomCow child, @Nullable Player player) {
        List<Holder<CowType<MooshroomConfiguration>>> eligibleCowTypes = new ArrayList<>();

        for (Holder.Reference<CowType<?>> cowType : parent.level().registryAccess().registryOrThrow(BovinesRegistryKeys.COW_TYPE).holders().filter(type -> type.isBound() && type.value().type() == BovinesCowTypeTypes.MOOSHROOM_TYPE && ((MooshroomConfiguration)type.value().configuration()).offspringConditions() != OffspringConditions.EMPTY).toList()) {
            Holder<CowType<MooshroomConfiguration>> mooshroomType = (Holder) cowType;
            var conditions = mooshroomType.value().configuration().offspringConditions();

            LootParams.Builder params = new LootParams.Builder((ServerLevel) parent.level());
            params.withParameter(LootContextParams.THIS_ENTITY, parent);
            params.withParameter(BovinesLootContextParams.PARTNER, other);
            params.withParameter(BovinesLootContextParams.CHILD, child);
            params.withParameter(LootContextParams.ORIGIN, parent.position());
            params.withParameter(BovinesLootContextParams.BREEDING_TYPE, cowType);
            LootContext thisContext = new LootContext.Builder(params.create(BovinesLootContextParamSets.BREEDING)).create(Optional.empty());

            params.withParameter(LootContextParams.THIS_ENTITY, other);
            params.withParameter(BovinesLootContextParams.PARTNER, parent);
            LootContext otherContext = new LootContext.Builder(params.create(BovinesLootContextParamSets.BREEDING)).create(Optional.empty());

            if (conditions.thisConditions().stream().allMatch(condition -> condition.test(thisContext))
                    && conditions.otherConditions().stream().allMatch(condition -> condition.test(otherContext)))
                eligibleCowTypes.add(mooshroomType);
        }

        if (!eligibleCowTypes.isEmpty()) {
            int random = parent.getRandom().nextInt(eligibleCowTypes.size());
            var randomType = eligibleCowTypes.get(random);

            createParticles(child, randomType, parent.position());

            if (parent.getLoveCause() != null)
                MutationTrigger.INSTANCE.trigger(parent.getLoveCause(), parent, other, child, (Holder) randomType);
            BreedCowWithTypeTrigger.INSTANCE.trigger(parent.getLoveCause(), parent, other, child, (Holder) randomType);
            return randomType;
        }

        BovinesAndButtercups.getHelper().clearParticlePositions(child);

        var parentType = CowTypeAttachment.getCowTypeHolderFromEntity(parent, BovinesCowTypeTypes.MOOSHROOM_TYPE);
        var otherType = CowTypeAttachment.getCowTypeHolderFromEntity(other, BovinesCowTypeTypes.MOOSHROOM_TYPE);
        if (parentType == null || otherType == null)
            return null;

        if (!otherType.equals(parentType) && parent.getRandom().nextBoolean()) {
            BreedCowWithTypeTrigger.INSTANCE.trigger(parent.getLoveCause(), parent, other, child, (Holder<CowType<?>>)(Holder<?>)otherType);
            return otherType;
        }

        BreedCowWithTypeTrigger.INSTANCE.trigger(parent.getLoveCause(), parent, other, child, (Holder<CowType<?>>)(Holder<?>)parentType);
        return parentType;
    }

    private static void createParticles(MushroomCow child, Holder<CowType<MooshroomConfiguration>> type, Vec3 parentPos) {
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
