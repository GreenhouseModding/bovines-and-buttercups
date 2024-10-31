package house.greenhouse.bovinesandbuttercups.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import house.greenhouse.bovinesandbuttercups.api.BovinesTags;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonStructureResolver.class)
public class PistonStructureResolverMixin {
    @ModifyReturnValue(method = "isSticky", at = @At("RETURN"))
    private static boolean bovinesandbuttercups$makeRichHoneyStickyForPistons(boolean original, BlockState state) {
        return original || state.is(BovinesBlocks.RICH_HONEY_BLOCK);
    }

    @ModifyReturnValue(method = "canStickToEachOther", at = @At("RETURN"))
    private static boolean bovinesandbuttercups$dontMakeRichHoneyStickToOtherPistonBlocks(boolean original, BlockState state, BlockState state2) {
        if (state.is(BovinesBlocks.RICH_HONEY_BLOCK) && !state2.is(BovinesTags.BlockTags.DOES_NOT_STICK_RICH_HONEY_BLOCK) || !state.is(BovinesTags.BlockTags.DOES_NOT_STICK_RICH_HONEY_BLOCK) && state2.is(BovinesBlocks.RICH_HONEY_BLOCK))
            return false;
        return original;
    }
}
