package house.greenhouse.bovinesandbuttercups.content.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class BreedCowWithVariantTrigger extends SimpleCriterionTrigger<BreedCowWithVariantTrigger.TriggerInstance> {
    public static final BreedCowWithVariantTrigger INSTANCE = new BreedCowWithVariantTrigger();
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("breed_cow_with_variant");
    public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(BreedCowWithVariantTrigger.TriggerInstance::player),
            CowType.CODEC.optionalFieldOf("type").forGetter(TriggerInstance::type),
            RegistryCodecs.homogeneousList(BovinesRegistryKeys.COW_VARIANT).optionalFieldOf("variants", HolderSet.direct()).forGetter(TriggerInstance::variants),
            ContextAwarePredicate.CODEC.optionalFieldOf("parent").forGetter(TriggerInstance::parent),
            ContextAwarePredicate.CODEC.optionalFieldOf("partner").forGetter(TriggerInstance::partner),
            ContextAwarePredicate.CODEC.optionalFieldOf("child").forGetter(TriggerInstance::child),
            Codec.BOOL.optionalFieldOf("different_from_parents").forGetter(TriggerInstance::differentFromParents)
    ).apply(inst, TriggerInstance::new));

    private BreedCowWithVariantTrigger() {}

    public void trigger(ServerPlayer serverPlayer, Animal parent, Animal partner, AgeableMob child, boolean differentFromParents, Holder<CowVariant<?>> type) {
        LootContext parentContext = EntityPredicate.createContext(serverPlayer, parent);
        LootContext partnerContext = EntityPredicate.createContext(serverPlayer, partner);
        LootContext childContext = child != null ? EntityPredicate.createContext(serverPlayer, child) : null;
        trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(parentContext, partnerContext, childContext, differentFromParents, type));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  Optional<Holder<CowType<?>>> type,
                                  HolderSet<CowVariant<?>> variants,
                                  Optional<ContextAwarePredicate> parent,
                                  Optional<ContextAwarePredicate> partner,
                                  Optional<ContextAwarePredicate> child,
                                  Optional<Boolean> differentFromParents) implements SimpleInstance {

        public boolean matches(LootContext parentContext, LootContext partnerContext, LootContext childContext, boolean isNewType, Holder<CowVariant<?>> variant) {
            return variant.isBound() && (type.isEmpty() || type.get().isBound() && type.get().value() == variant.value().type()) &&
                    (variants.size() == 0 || variants.contains(variant)) &&
                    (this.child.isEmpty() || this.child.get().matches(childContext)) &&
                    (
                            (this.parent.isEmpty() || this.parent.get().matches(parentContext)) && (this.partner.isEmpty() || this.partner.get().matches(partnerContext)) ||
                            (this.parent.isEmpty() || this.parent.get().matches(partnerContext)) && (this.partner.isEmpty() || this.partner.get().matches(parentContext))
                    ) &&
                    (differentFromParents.isEmpty() || isNewType == differentFromParents.get());
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}
