package house.greenhouse.bovinesandbuttercups.client;

import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.CustomFlowerItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.CustomHugeMushroomItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.CustomMushroomItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.FlowerCrownItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.NectarBowlItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.PlaceableEdibleItemRenderer;
import house.greenhouse.bovinesandbuttercups.content.item.CustomFlowerItem;
import house.greenhouse.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import house.greenhouse.bovinesandbuttercups.content.item.CustomMushroomItem;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.content.item.NectarBowlItem;
import house.greenhouse.bovinesandbuttercups.content.item.PlaceableEdibleItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class BovinesBEWLR extends BlockEntityWithoutLevelRenderer {
    public static final BlockEntityWithoutLevelRenderer BLOCK_ENTITY_WITHOUT_LEVEL_RENDERER = new BovinesBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    public static final IClientItemExtensions ITEM_EXTENSIONS = new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return BovinesBEWLR.BLOCK_ENTITY_WITHOUT_LEVEL_RENDERER;
        }
    };

    public BovinesBEWLR(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        switch (stack.getItem()) {
            case CustomFlowerItem customFlowerItem ->
                    CustomFlowerItemRenderer.render(stack, context, poseStack, bufferSource, light, overlay);
            case CustomMushroomItem customMushroomItem ->
                    CustomMushroomItemRenderer.render(stack, context, poseStack, bufferSource, light, overlay);
            case CustomHugeMushroomItem customHugeMushroomItem ->
                    CustomHugeMushroomItemRenderer.render(stack, context, poseStack, bufferSource, light, overlay);
            case NectarBowlItem nectarBowlItem ->
                    NectarBowlItemRenderer.render(stack, context, poseStack, bufferSource, light, overlay);
            case FlowerCrownItem flowerCrownItem ->
                    FlowerCrownItemRenderer.render(stack, context, poseStack, bufferSource, light, overlay);
            case PlaceableEdibleItem placeableEdibleItem ->
                    PlaceableEdibleItemRenderer.render(stack, context, poseStack, bufferSource, light, overlay);
            default -> {
            }
        }
    }
}
