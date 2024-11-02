package house.greenhouse.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryAccessor;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FlowerCrownItemRenderer {
    private static final Map<FlowerCrown, TextureMap> FLOWER_CROWN_TO_TEXTURE_MAP = new HashMap<>();
    private static final Map<TextureMap, BakedModel> MODEL_MAP = new HashMap<>();
    public static final ResourceLocation BASE = BovinesAndButtercups.asResource("item/base_flower_crown");

    public static void clearModelMap() {
        MODEL_MAP.clear();
    }

    public static void render(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        BakedModel model;
        if (!stack.has(BovinesDataComponents.FLOWER_CROWN)) {
            model = BovinesAndButtercupsClient.getHelper().getModel(BASE);
        } else
            model = MODEL_MAP.computeIfAbsent(FLOWER_CROWN_TO_TEXTURE_MAP.computeIfAbsent(stack.get(BovinesDataComponents.FLOWER_CROWN), TextureMap::new), FlowerCrownItemRenderer::createModel);

        boolean left = context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        pose.translate(0.5F, 0.5F, 0.5F);

        boolean bl = context == ItemDisplayContext.GUI && !model.usesBlockLight();
        MultiBufferSource.BufferSource source = null;

        if (bl) {
            Lighting.setupForFlatItems();
            source = Minecraft.getInstance().renderBuffers().bufferSource();
        }

        Minecraft.getInstance().getItemRenderer().render(stack, context, left, pose, source == null ? buffer : source, light, overlay, model);

        if (bl) {
            source.endBatch();
            Lighting.setupFor3DItems();
        }
    }

    private static BakedModel createModel(TextureMap component) {
        Map<String, Either<Material, String>> textureMap = new HashMap<>();

        textureMap.put("particle", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.top())));
        textureMap.put("layer0", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.centerLeft())));
        textureMap.put("layer1", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.centerRight())));
        textureMap.put("layer2", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.topLeft())));
        textureMap.put("layer3", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.topRight())));
        textureMap.put("layer4", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.top())));

        textureMap.put("top_left", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.topLeft())));
        textureMap.put("top", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.top())));
        textureMap.put("top_right", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.topRight())));
        textureMap.put("center_left", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.centerLeft())));
        textureMap.put("center_right", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.centerRight())));
        textureMap.put("bottom_left", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.bottomLeft())));
        textureMap.put("bottom", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.bottom())));
        textureMap.put("bottom_right", Either.left(new Material(InventoryMenu.BLOCK_ATLAS, component.bottomRight())));

        String textureString =
                "top_left=" + component.topLeft() +
                ",top=" + component.top() +
                ",top_right=" + component.topRight() +
                ",center_left=" + component.centerLeft() +
                ",center_right=" + component.centerRight() +
                ",bottom_left=" + component.bottomLeft() +
                ",bottom=" + component.bottom() +
                ",bottom_right=" + component.bottomRight();

        BlockModel blockModel = new BlockModel(BASE, List.of(), textureMap, false, BlockModel.GuiLight.FRONT, BovinesAndButtercupsClient.getHelper().getModel(BASE).getTransforms(), List.of());
        blockModel.resolveParents(rl -> {
            UnbakedModel unbaked = ((ModelBakeryAccessor)BovinesAndButtercupsClient.getModelBakery()).bovinesandbuttercups$getModel(rl);
            if (unbaked instanceof BlockModel bm && bm.getRootModel() == ModelBakery.GENERATION_MARKER)
                unbaked = ModelBakeryAccessor.bovinesandbuttercups$getItemModelGenerator().generateBlockModel(Material::sprite, bm);
            return unbaked;
        });

        return blockModel
                .bake(
                        BovinesAndButtercupsClient.getModelBakery().new ModelBakerImpl((modelLocation, material) -> material.sprite(), new ModelResourceLocation(BovinesAndButtercups.asResource("item/custom_flower_crown"), textureString)),
                        blockModel,
                        Material::sprite,
                        BlockModelRotation.X0_Y0,
                        false
                );
    }

    protected record TextureMap(ResourceLocation topLeft, ResourceLocation top, ResourceLocation topRight,
                              ResourceLocation centerLeft, ResourceLocation centerRight,
                              ResourceLocation bottomLeft, ResourceLocation bottom, ResourceLocation bottomRight) {
        private TextureMap(FlowerCrown component) {
            this(
                    component.topLeft().value().itemTextures().topLeft(),
                    component.top().value().itemTextures().top(),
                    component.topRight().value().itemTextures().topRight(),
                    component.centerLeft().value().itemTextures().centerLeft(),
                    component.centerRight().value().itemTextures().centerRight(),
                    component.bottomLeft().value().itemTextures().bottomLeft(),
                    component.bottom().value().itemTextures().bottom(),
                    component.bottomRight().value().itemTextures().bottomRight()
            );
        }

        @Override
        public boolean equals(Object other) {
            if (other == this)
                return true;
            if (!(other instanceof TextureMap textureMap))
                return false;
            return textureMap.topLeft.equals(topLeft) && textureMap.top.equals(top) && textureMap.topRight.equals(topRight)
                    && textureMap.centerLeft.equals(centerLeft) && textureMap.centerRight.equals(centerRight)
                    && textureMap.bottomLeft.equals(bottomLeft) && textureMap.bottom.equals(bottom) && textureMap.bottomRight.equals(bottomRight);
        }

        @Override
        public int hashCode() {
            return Objects.hash(topLeft, top, topRight, centerLeft, centerRight, bottomLeft, bottom, bottomRight);
        }
    }

}
