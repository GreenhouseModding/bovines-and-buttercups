package house.greenhouse.bovinesandbuttercups.client.bovinestate;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BovineBlockstateTypes {
    public static final StateDefinition<Block, BlockState> EMPTY_STATE = (new StateDefinition.Builder<Block, BlockState>(Blocks.AIR)).create(Block::defaultBlockState, BlockState::new);

    public static final StateDefinition<Block, BlockState> GENERIC = BovinesBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("generic"), EMPTY_STATE);
    public static final StateDefinition<Block, BlockState> FLOWER = BovinesBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("flower"), BovinesBlocks.CUSTOM_FLOWER.getStateDefinition());
    public static final StateDefinition<Block, BlockState> MUSHROOM = BovinesBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("mushroom"), BovinesBlocks.CUSTOM_MUSHROOM.getStateDefinition());
    public static final StateDefinition<Block, BlockState> POTTED_FLOWER = BovinesBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("potted_flower"), BovinesBlocks.POTTED_CUSTOM_FLOWER.getStateDefinition());
    public static final StateDefinition<Block, BlockState> POTTED_MUSHROOM = BovinesBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("potted_mushroom"), BovinesBlocks.POTTED_CUSTOM_MUSHROOM.getStateDefinition());
    public static final StateDefinition<Block, BlockState> MUSHROOM_BLOCK = BovinesBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("mushroom_block"), BovinesBlocks.CUSTOM_MUSHROOM_BLOCK.getStateDefinition());

    public static void init() {

    }
}
