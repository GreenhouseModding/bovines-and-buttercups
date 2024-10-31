package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.item.CustomFlowerItem;
import house.greenhouse.bovinesandbuttercups.content.item.CustomMushroomItem;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin {
    @Shadow
    protected abstract boolean isEmpty();

    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$useDataDefinedItemOnPot(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (stack.getItem() instanceof CustomFlowerItem) {
            if (this.isEmpty()) {
                level.setBlock(pos, BovinesBlocks.POTTED_CUSTOM_FLOWER.defaultBlockState(), 3);
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                if (stack.has(BovinesDataComponents.CUSTOM_FLOWER))
                    ((CustomFlowerPotBlockEntity) level.getBlockEntity(pos)).setFlowerType(stack.get(BovinesDataComponents.CUSTOM_FLOWER));
                level.getBlockEntity(pos).setChanged();
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                player.awardStat(Stats.POT_FLOWER);
                stack.consume(1, player);
                cir.setReturnValue(ItemInteractionResult.sidedSuccess(level.isClientSide));
            } else
                cir.setReturnValue(ItemInteractionResult.CONSUME);
        } else if (stack.getItem() instanceof CustomMushroomItem) {
            if (this.isEmpty()) {
                level.setBlock(pos, BovinesBlocks.POTTED_CUSTOM_MUSHROOM.defaultBlockState(), 3);
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                if (stack.has(BovinesDataComponents.CUSTOM_MUSHROOM))
                    ((CustomMushroomPotBlockEntity) level.getBlockEntity(pos)).setMushroomType(stack.get(BovinesDataComponents.CUSTOM_MUSHROOM));
                level.getBlockEntity(pos).setChanged();
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                player.awardStat(Stats.POT_FLOWER);
                stack.consume(1, player);
                cir.setReturnValue(ItemInteractionResult.sidedSuccess(level.isClientSide));
            } else
                cir.setReturnValue(ItemInteractionResult.CONSUME);
        }
    }
}
