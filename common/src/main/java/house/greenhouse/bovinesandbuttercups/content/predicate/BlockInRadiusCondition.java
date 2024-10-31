package house.greenhouse.bovinesandbuttercups.content.predicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.codec.BovinesCodecs;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParams;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Comparator;

public record BlockInRadiusCondition(BlockPredicate predicate, AABB radius) implements LootItemCondition {
    public static final MapCodec<BlockInRadiusCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BlockPredicate.CODEC.fieldOf("block").forGetter(BlockInRadiusCondition::predicate),
            BovinesCodecs.AABB.fieldOf("radius").forGetter(BlockInRadiusCondition::radius)
    ).apply(inst, BlockInRadiusCondition::new));
    public static final LootItemConditionType TYPE = new LootItemConditionType(CODEC);

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParam(LootContextParams.ORIGIN);
        AABB aabb = radius.move(origin);
        return BlockPos.betweenClosedStream(aabb).map(BlockPos::immutable).sorted(Comparator.comparing(pos -> origin.distanceTo(pos.getCenter()))).toList().stream().anyMatch(pos -> {
            BlockState state = context.getLevel().getBlockState(pos);
            boolean bl = predicate.matches(context.getLevel(), pos);
            if (bl && context.hasParam(BovinesLootContextParams.BREEDING_TYPE) && context.hasParam(BovinesLootContextParams.CHILD)) {
                Entity child = context.getParam(BovinesLootContextParams.CHILD);
                if (child instanceof Moobloom moobloom) {
                    VoxelShape shape = state.getCollisionShape(context.getLevel(), pos);
                    moobloom.addParticlePosition(context.getParam(BovinesLootContextParams.BREEDING_TYPE), shape.isEmpty() || state.isCollisionShapeFullBlock(context.getLevel(), pos) ? pos.getCenter() : shape.bounds().getCenter().add(Vec3.atLowerCornerOf(pos)));
                } else if (child instanceof LivingEntity living && BovinesRegistries.COW_TYPE_TYPE.stream().anyMatch(cowTypeType -> cowTypeType.isApplicable(child))) {
                    VoxelShape shape = state.getCollisionShape(context.getLevel(), pos);
                    BovinesAndButtercups.getHelper().addParticlePosition(living, context.getParam(BovinesLootContextParams.BREEDING_TYPE), shape.isEmpty() || state.isCollisionShapeFullBlock(context.getLevel(), pos) ? pos.getCenter() : shape.bounds().getCenter().add(Vec3.atLowerCornerOf(pos)));
                }
            }
            return bl;
        });
    }

    @Override
    public LootItemConditionType getType() {
        return TYPE;
    }

    public static class Builder implements LootItemCondition.Builder {
        private final BlockPredicate block;
        private AABB radius = AABB.ofSize(Vec3.ZERO, 0, 0, 0);

        public Builder(BlockPredicate block) {
            this.block = block;
        }

        public Builder(BlockPredicate.Builder block) {
            this.block = block.build();
        }

        public Builder withRadius(double xz, double y) {
            radius = AABB.ofSize(Vec3.ZERO, xz, y, xz);
            return this;
        }

        public Builder withOffset(double x, double y, double z) {
            radius = radius.move(x, y, z);
            return this;
        }

        @Override
        public LootItemCondition build() {
            return new BlockInRadiusCondition(block, radius);
        }
    }
}
