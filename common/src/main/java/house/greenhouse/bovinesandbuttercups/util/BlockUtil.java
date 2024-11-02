package house.greenhouse.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.access.VoxelShapeFunctionAccess;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockUtil {
    public static Component getOrCreateBlockNameTranslationKey(ResourceLocation location) {
        return Component.translatable("block." + location.getNamespace() + "." + location.getPath());
    }

    private static final Codec<BooleanOp> BOOLEAN_OP_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<BooleanOp, T>> decode(DynamicOps<T> ops, T input) {
            var dataResult = ops.getStringValue(input);
            if (dataResult.isError())
                return DataResult.error(() -> "BooleanOp value must be a string.");
            BooleanOp op = null;
            switch (dataResult.getOrThrow()) {
                case "false" -> op = BooleanOp.FALSE;
                case "not_or" -> op = BooleanOp.NOT_OR;
                case "only_second" -> op = BooleanOp.ONLY_SECOND;
                case "not_First" -> op = BooleanOp.NOT_FIRST;
                case "only_first" -> op = BooleanOp.ONLY_FIRST;
                case "not_second" -> op = BooleanOp.NOT_SECOND;
                case "not_same" -> op = BooleanOp.NOT_SAME;
                case "not_and" -> op = BooleanOp.NOT_AND;
                case "and" -> op = BooleanOp.AND;
                case "same" -> op = BooleanOp.SAME;
                case "causes" -> op = BooleanOp.CAUSES;
                case "first" -> op = BooleanOp.FIRST;
                case "caused_by" -> op = BooleanOp.CAUSED_BY;
                case "or" -> op = BooleanOp.OR;
                case "true" -> op = BooleanOp.TRUE;
                default -> {
                }
            }
            if (op != null)
                return DataResult.success(Pair.of(op, input));
            return DataResult.error(() -> "Could not get BooleanOp value from " + dataResult.getOrThrow() + ".");
        }

        @Override
        public <T> DataResult<T> encode(BooleanOp input, DynamicOps<T> ops, T prefix) {
            return null;
        }
    };

    private static final Codec<VoxelShape> DIRECT_VOXEL_SHAPE_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Vec3.CODEC.fieldOf("min").forGetter(voxelShape -> new Vec3(voxelShape.min(Direction.Axis.X), voxelShape.min(Direction.Axis.Y), voxelShape.min(Direction.Axis.Z))),
            Vec3.CODEC.fieldOf("max").forGetter(voxelShape -> new Vec3(voxelShape.max(Direction.Axis.X), voxelShape.max(Direction.Axis.Y), voxelShape.max(Direction.Axis.Z)))
    ).apply(inst, (t1, t2) -> Shapes.create(t1.x, t1.y, t1.z, t2.x, t2.y, t2.z)));
    private static final Codec<VoxelShape> JOINED_VOXEL_SHAPE_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            DIRECT_VOXEL_SHAPE_CODEC.listOf().fieldOf("shapes").forGetter(BlockUtil::shapes),
            BOOLEAN_OP_CODEC.optionalFieldOf("function", BooleanOp.OR).forGetter(BlockUtil::operator)
    ).apply(inst, BlockUtil::createJoined));

    public static final Codec<VoxelShape> VOXEL_SHAPE_CODEC = Codec.either(JOINED_VOXEL_SHAPE_CODEC, DIRECT_VOXEL_SHAPE_CODEC).xmap(either -> either.map(shape -> shape, Function.identity()), shape -> {
        if (((VoxelShapeFunctionAccess)shape).bovinesandbuttercups$getJoinFunction() != null)
            return Either.left(shape);
        return Either.right(shape);
    });

    private static List<VoxelShape> shapes(VoxelShape shape) {
        List<VoxelShape> shapes = new ArrayList<>();
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            shapes.add(Shapes.create(minX, minY, minZ, maxX, maxY, maxZ));
        });
        return shapes;
    }

    private static BooleanOp operator(VoxelShape shape) {
        return ((VoxelShapeFunctionAccess)shape).bovinesandbuttercups$getJoinFunction();
    }

    private static VoxelShape createJoined(List<VoxelShape> shapes, BooleanOp function) {
        var otherShapes = new ArrayList<>(shapes);
        otherShapes.removeFirst();
        return otherShapes.stream().reduce(shapes.getFirst(), (shape, shape2) -> Shapes.join(shape, shape2, function));
    }
}
