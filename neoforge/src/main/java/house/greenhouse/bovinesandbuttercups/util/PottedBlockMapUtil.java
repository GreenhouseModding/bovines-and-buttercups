package house.greenhouse.bovinesandbuttercups.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;

import java.util.Map;
import java.util.function.Supplier;

public class PottedBlockMapUtil {
    private static Map<Block, Block> POTTED_CONTENT_MAP = null;

    public static Map<Block, Block> getPottedContentMap() {
        if (POTTED_CONTENT_MAP == null) {
            ImmutableMap.Builder<Block, Block> builder = ImmutableMap.builder();
            for (Map.Entry<ResourceLocation, Supplier<? extends Block>> forgeEntry : ((FlowerPotBlock) Blocks.FLOWER_POT).getFullPotsView().entrySet()) {
                builder.put(BuiltInRegistries.BLOCK.getValue(forgeEntry.getKey()), forgeEntry.getValue().get());
            }
            POTTED_CONTENT_MAP = builder.build();
        }
        return POTTED_CONTENT_MAP;
    }
}
