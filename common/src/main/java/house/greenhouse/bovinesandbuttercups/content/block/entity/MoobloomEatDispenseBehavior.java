package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.mixin.DefaultDispenseItemBehaviorAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class MoobloomEatDispenseBehavior extends OptionalDispenseItemBehavior {
    public static final MoobloomEatDispenseBehavior INSTANCE = new MoobloomEatDispenseBehavior(DispenserBlock.DISPENSER_REGISTRY.get(Items.BONE_MEAL));
    private final DispenseItemBehavior boneMealBehavior;

    protected MoobloomEatDispenseBehavior(DispenseItemBehavior boneMealBehavior) {
        this.boneMealBehavior = boneMealBehavior;
    }

    public static void registerBehavior(MoobloomEatDispenseBehavior behavior) {
        DispenserBlock.registerBehavior(Items.BONE_MEAL, behavior);
    }

    @Override
    public ItemStack execute(BlockSource source, ItemStack stack) {
        setSuccess(true);
        if (handleMoobloom(source, stack)) {
            return stack;
        }

        if (boneMealBehavior instanceof OptionalDispenseItemBehavior optional) {
            ((DefaultDispenseItemBehaviorAccessor)optional).bovinesandbuttercups$invokeExecute(source, stack);
            if (!optional.isSuccess())
                setSuccess(false);
            return stack;
        }

        boneMealBehavior.dispense(source, stack);
        return stack;
    }

    private boolean handleMoobloom(BlockSource source, ItemStack stack) {
        ServerLevel level = source.level();
        BlockPos relativePos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        List<Moobloom> mooblooms = level.getEntitiesOfClass(Moobloom.class, new AABB(relativePos), EntitySelector.NO_SPECTATORS);

        if (mooblooms.isEmpty())
            return false;
        else {
            for (Moobloom moobloom : mooblooms) {
                moobloom.feed();
                stack.shrink(1);
            }

            return true;
        }
    }
}
