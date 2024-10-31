package house.greenhouse.bovinesandbuttercups.client.api.model;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;

import java.util.List;
import java.util.Map;

public class BovinesModelUtil {
    public static final UnbakedModel EMPTY_MODEL = new BlockModel(null, List.of(), Map.of(), null, null, ItemTransforms.NO_TRANSFORMS, List.of());
}
