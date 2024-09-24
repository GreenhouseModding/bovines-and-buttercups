package house.greenhouse.bovinesandbuttercups.api.cowtype;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record OffspringConditions(List<LootItemCondition> thisConditions, List<LootItemCondition> otherConditions, Inheritance inheritance) {
    public static final Codec<OffspringConditions> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("this_conditions", List.of()).forGetter(OffspringConditions::thisConditions),
            LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("other_conditions", List.of()).forGetter(OffspringConditions::otherConditions),
            Inheritance.CODEC.optionalFieldOf("inheritance", Inheritance.PARENT).forGetter(OffspringConditions::inheritance)
    ).apply(inst, OffspringConditions::new));
    public static final Codec<OffspringConditions> CODEC = Codec.either(LootItemCondition.DIRECT_CODEC.listOf(), DIRECT_CODEC).flatComapMap(either -> either.map(lootItemConditions -> new OffspringConditions(lootItemConditions, List.of(), Inheritance.PARENT), Function.identity()), conditions -> {
        if (conditions.otherConditions.isEmpty())
            return DataResult.success(Either.left(conditions.thisConditions));
        return DataResult.success(Either.right(conditions));
    });

    public static final OffspringConditions EMPTY = new OffspringConditions(List.of(), List.of(), Inheritance.PARENT);

    public enum Inheritance implements StringRepresentable {
        PARENT("parent_to_previous", (baby, parent, other) -> parentToPrevious(baby, parent)),
        OTHER("other_to_previous", (baby, parent, other) -> parentToPrevious(baby, other));

        private static Pair<Holder<CowType<?>>, Optional<Holder<CowType<?>>>> parentToPrevious(Holder<CowType<?>> baby, CowTypeAttachment parent) {
            if (parent.previousCowType().isPresent() && baby.is(parent.previousCowType().get()))
                return Pair.of(baby, Optional.of(parent.cowType()));
            return Pair.of(baby, parent.previousCowType());
        }

        public static final Codec<Inheritance> CODEC = StringRepresentable.fromEnum(Inheritance::values);
        private final String name;
        private final InheritanceOperation operation;

        Inheritance(String name, InheritanceOperation operation) {
            this.name = name;
            this.operation = operation;
        }

        public <C extends CowTypeConfiguration> Pair<Holder<CowType<C>>, Optional<Holder<CowType<C>>>> handleInheritance(Holder<CowType<C>> baby, CowTypeAttachment parent, CowTypeAttachment other) {
            return (Pair<Holder<CowType<C>>, Optional<Holder<CowType<C>>>>) operation.handleInheritance((Holder) baby, parent, other);
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return name;
        }
    }

    @FunctionalInterface
    private interface InheritanceOperation {
        Pair<Holder<CowType<?>>, Optional<Holder<CowType<?>>>> handleInheritance(Holder<CowType<?>> baby, CowTypeAttachment parent, CowTypeAttachment other);
    }
}