package house.greenhouse.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.content.component.FlowerCrown;
import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryModelBakerImplInvoker;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FlowerCrownItemRenderer implements SpecialModelRenderer<FlowerCrown> {
    private static final Map<FlowerCrown, TextureMap> FLOWER_CROWN_TO_TEXTURE_MAP = new HashMap<>();
    private static final Map<TextureMap, BakedModel> MODEL_MAP = new HashMap<>();
    public static final ResourceLocation BASE = BovinesAndButtercups.asResource("item/base_flower_crown");

    public static final FlowerCrownItemRenderer INSTANCE = new FlowerCrownItemRenderer();

    public static void clearModelMap() {
        MODEL_MAP.clear();
    }

    protected FlowerCrownItemRenderer() {}

    @Override
    public void render(@Nullable FlowerCrown flowerCrown, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        BakedModel model;
        if (flowerCrown == null) {
            model = BovinesAndButtercupsClient.getHelper().getModel(BASE);
        } else
            model = MODEL_MAP.computeIfAbsent(FLOWER_CROWN_TO_TEXTURE_MAP.computeIfAbsent(flowerCrown, TextureMap::new), FlowerCrownItemRenderer::createModel);
        ItemRenderer.renderItem(displayContext, poseStack, bufferSource, packedLight, packedOverlay, new int[0], model, RenderType.cutout(), ItemStackRenderState.FoilType.NONE);
    }

    @Override
    public @Nullable FlowerCrown extractArgument(ItemStack stack) {
        return stack.get(BovinesDataComponents.FLOWER_CROWN);
    }

    
    private static BakedModel createModel(TextureMap component) {
        Map<String, TextureSlots.SlotContents> textureMap = new HashMap<>();

        textureMap.put("particle", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.top())));
        textureMap.put("layer0", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.centerLeft())));
        textureMap.put("layer1", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.centerRight())));
        textureMap.put("layer2", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.topLeft())));
        textureMap.put("layer3", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.topRight())));
        textureMap.put("layer4", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.top())));

        textureMap.put("top_left", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.topLeft())));
        textureMap.put("top", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.top())));
        textureMap.put("top_right", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.topRight())));
        textureMap.put("center_left", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.centerLeft())));
        textureMap.put("center_right", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.centerRight())));
        textureMap.put("bottom_left", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.bottomLeft())));
        textureMap.put("bottom", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.bottom())));
        textureMap.put("bottom_right", new TextureSlots.Value(new Material(TextureAtlas.LOCATION_BLOCKS, component.bottomRight())));

        String textureString =
                "top_left=" + component.topLeft() +
                        ",top=" + component.top() +
                        ",top_right=" + component.topRight() +
                        ",center_left=" + component.centerLeft() +
                        ",center_right=" + component.centerRight() +
                        ",bottom_left=" + component.bottomLeft() +
                        ",bottom=" + component.bottom() +
                        ",bottom_right=" + component.bottomRight();

        BlockModel blockModel = new BlockModel(BASE, List.of(), new TextureSlots.Data(textureMap), true, BlockModel.GuiLight.FRONT, BovinesAndButtercupsClient.getHelper().getModel(BASE).getTransforms());
        ModelBakery.ModelBakerImpl impl = BovinesAndButtercupsClient.getHelper().getModelBakery().new ModelBakerImpl(new ModelBakery.TextureGetter() {
            @Override
            public TextureAtlasSprite get(ModelDebugName debugName, Material material) {
                TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(material.atlasLocation()).getSprite(material.texture());
                if (sprite.atlasLocation() == MissingTextureAtlasSprite.getLocation())
                    reportMissingReference(debugName, material.toString());
                return sprite;
            }

            @Override
            public TextureAtlasSprite reportMissingReference(ModelDebugName debugName, String material) {
                return Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(MissingTextureAtlasSprite.getLocation());
            }
        }, () -> new ModelResourceLocation(BovinesAndButtercups.asResource("item/custom_flower_crown"), textureString).toString());

        blockModel.resolveDependencies(model ->
                ((ModelBakeryModelBakerImplInvoker)impl).bovinesandbuttercups$getModel(model));
        return UnbakedModel.bakeWithTopModelValues(blockModel, impl, BlockModelRotation.X0_Y0);
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

    public static class Unbaked implements SpecialModelRenderer.Unbaked {
        public static final ResourceLocation ID = BovinesAndButtercups.asResource("flower_crown");
        public static final Unbaked INSTANCE = new Unbaked();
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(() -> INSTANCE);

        protected Unbaked() {}

        @Override
        public @Nullable SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return FlowerCrownItemRenderer.INSTANCE;
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
