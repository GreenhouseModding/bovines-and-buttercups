package house.greenhouse.bovinesandbuttercups.client.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface BovinesClientHelper {
    ModelBakery getModelBakery();

    BakedModel getModel(ResourceLocation resourceLocation);

    ItemStack getEquippedFlowerCrownForRendering(LivingEntity entity);

    BakedModel createPlaceableEdibleModel(List<Pair<PlaceableEdibleSelector, BakedModel>> selectors);

    void tesselateBlock(BlockRenderDispatcher dispatcher, BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos, PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType, boolean checkSides, RandomSource random, long seed, int packedOverlay);
}
