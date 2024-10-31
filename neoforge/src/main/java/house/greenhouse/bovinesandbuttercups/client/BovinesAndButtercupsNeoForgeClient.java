package house.greenhouse.bovinesandbuttercups.client;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.particle.BloomParticle;
import house.greenhouse.bovinesandbuttercups.client.particle.ModelLocationParticle;
import house.greenhouse.bovinesandbuttercups.client.particle.ShroomParticle;
import house.greenhouse.bovinesandbuttercups.client.platform.BovinesClientHelperNeoForge;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.FlowerCrownLayer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.MooshroomDatapackMushroomLayer;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelLayers;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomFlowerPotBlockRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomFlowerRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomHugeMushroomBlockRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomMushroomPotBlockRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomMushroomRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.MoobloomRenderer;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.BovinesModelSetTypes;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.CowLayersLayer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.FlowerCrownModel;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.FlowerCrownItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.util.BovineModelSetUtil;
import house.greenhouse.bovinesandbuttercups.client.util.ClearTextureCacheReloadListener;
import house.greenhouse.bovinesandbuttercups.integration.accessories.client.BovinesAccessoriesIntegrationClient;
import house.greenhouse.bovinesandbuttercups.mixin.client.ModelBakeryAccessor;
import house.greenhouse.bovinesandbuttercups.mixin.neoforge.client.EntityRenderersEventAddLayersAccessor;
import house.greenhouse.bovinesandbuttercups.content.block.entity.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.particle.BovinesParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.ArrayList;
import java.util.List;

@Mod(value = BovinesAndButtercups.MOD_ID, dist = Dist.CLIENT)
public class BovinesAndButtercupsNeoForgeClient {
    public BovinesAndButtercupsNeoForgeClient(IEventBus eventBus) {
        BovinesAndButtercupsClient.init(new BovinesClientHelperNeoForge());
    }

    @EventBusSubscriber(modid = BovinesAndButtercups.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BovinesAccessoriesIntegrationClient.init();
            BovinesModelSetTypes.init();
        }

        public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new ClearTextureCacheReloadListener());
        }

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerMobEffect(LockdownClientEffectExtensions.INSTANCE, BovinesEffects.LOCKDOWN);

            event.registerItem(BovinesBEWLR.ITEM_EXTENSIONS, BovinesItems.CUSTOM_FLOWER);
            event.registerItem(BovinesBEWLR.ITEM_EXTENSIONS, BovinesItems.CUSTOM_MUSHROOM);
            event.registerItem(BovinesBEWLR.ITEM_EXTENSIONS, BovinesItems.CUSTOM_MUSHROOM_BLOCK);
            event.registerItem(BovinesBEWLR.ITEM_EXTENSIONS, BovinesItems.FLOWER_CROWN);
            event.registerItem(BovinesBEWLR.ITEM_EXTENSIONS, BovinesItems.NECTAR_BOWL);
        }

        @SubscribeEvent
        public static void bakeModels(ModelEvent.ModifyBakingResult event) {
            List<ResourceLocation> models = BovineModelSetUtil.getModels(Minecraft.getInstance().getResourceManager(), Runnable::run).join();
            for (ResourceLocation entry : models) {
                UnbakedModel unbaked = BovineModelSetUtil.getUnbakedModel(entry, ((ModelBakeryAccessor)event.getModelBakery())::bovinesandbuttercups$getModel);
                if (unbaked != null) {
                    unbaked.resolveParents(location -> ((ModelBakeryAccessor)event.getModelBakery()).bovinesandbuttercups$getModel(location));
                    ModelResourceLocation modelResource = ModelResourceLocation.standalone(entry);
                    BakedModel model = unbaked.bake(event.getModelBakery().new ModelBakerImpl((rl, material) -> material.sprite(), modelResource), event.getTextureGetter(), BlockModelRotation.X0_Y0);
                    event.getModels().put(modelResource, model);
                }
            }

            List<ResourceLocation> flowerCrownModels = List.of(FlowerCrownItemRenderer.BASE);
            for (ResourceLocation entry : flowerCrownModels) {
                UnbakedModel unbaked = ((ModelBakeryAccessor)event.getModelBakery()).bovinesandbuttercups$getModel(entry);
                unbaked.resolveParents(location -> ((ModelBakeryAccessor)event.getModelBakery()).bovinesandbuttercups$getModel(location));
                ModelResourceLocation modelResource = ModelResourceLocation.standalone(entry);
                BakedModel model = unbaked.bake(event.getModelBakery().new ModelBakerImpl((rl, material) -> material.sprite(), modelResource), event.getTextureGetter(), BlockModelRotation.X0_Y0);
                event.getModels().put(modelResource, model);
            }
        }

        @SubscribeEvent
        public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(BovinesModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
            event.registerLayerDefinition(BovinesModelLayers.FLOWER_CROWN_MODEL_LAYER, () -> FlowerCrownModel.createLayer(new CubeDeformation(0.75F)));
            event.registerLayerDefinition(BovinesModelLayers.PIGLIN_FLOWER_CROWN_MODEL_LAYER, () -> FlowerCrownModel.createLayer(new CubeDeformation(1.5F, 0.5F, 0.5F)));
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BovinesEntityTypes.MOOBLOOM, MoobloomRenderer::new);
            event.registerBlockEntityRenderer(BovinesBlockEntityTypes.CUSTOM_FLOWER, CustomFlowerRenderer::new);
            event.registerBlockEntityRenderer(BovinesBlockEntityTypes.CUSTOM_MUSHROOM, CustomMushroomRenderer::new);
            event.registerBlockEntityRenderer(BovinesBlockEntityTypes.POTTED_CUSTOM_FLOWER, CustomFlowerPotBlockRenderer::new);
            event.registerBlockEntityRenderer(BovinesBlockEntityTypes.POTTED_CUSTOM_MUSHROOM, CustomMushroomPotBlockRenderer::new);
            event.registerBlockEntityRenderer(BovinesBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK, CustomHugeMushroomBlockRenderer::new);
        }

        @SubscribeEvent
        public static void registerRenderLayers(EntityRenderersEvent.AddLayers event) {
            MushroomCowRenderer mushroomCowRenderer = event.getRenderer(EntityType.MOOSHROOM);
            mushroomCowRenderer.addLayer(new CowLayersLayer<>(mushroomCowRenderer));
            mushroomCowRenderer.addLayer(new MooshroomDatapackMushroomLayer<>(mushroomCowRenderer, event.getContext().getBlockRenderDispatcher()));

            // TODO: Check if player renderers are a part of the renderers map.
            List<LivingEntityRenderer<?, ?>> renderers = new ArrayList<>();

            for (PlayerSkin.Model skin : event.getSkins()) {
                if (event.getSkin(skin) instanceof LivingEntityRenderer<?, ?> livingRenderer) {
                    livingRenderer.addLayer(new FlowerCrownLayer(livingRenderer, modelLayerLocation -> event.getContext().bakeLayer((ModelLayerLocation) modelLayerLocation), event.getContext().getModelManager()));
                    renderers.add(livingRenderer);
                }
            }

            ((EntityRenderersEventAddLayersAccessor)event).bovinesandbuttercups$getRenderers().forEach((entityType, entityRenderer) -> {
                if (entityRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer && !renderers.contains(livingRenderer)) {
                    Model model = livingRenderer.getModel();
                    if (model instanceof HumanoidModel<?> || model instanceof IllagerModel<?> || model instanceof VillagerModel<?>)
                        livingRenderer.addLayer(new FlowerCrownLayer(livingRenderer, modelLayerLocation -> event.getContext().bakeLayer((ModelLayerLocation) modelLayerLocation), event.getContext().getModelManager()));
                }
            });
        }

        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
            event.registerSpecial(BovinesParticleTypes.MODEL_LOCATION, new ModelLocationParticle.Provider());
            event.registerSpriteSet(BovinesParticleTypes.BLOOM, BloomParticle.Provider::new);
            event.registerSpriteSet(BovinesParticleTypes.SHROOM, ShroomParticle.Provider::new);
        }
    }
}
