package house.greenhouse.bovinesandbuttercups.client;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.BovinesModelSetTypes;
import house.greenhouse.bovinesandbuttercups.client.particle.BloomParticle;
import house.greenhouse.bovinesandbuttercups.client.particle.ModelLocationParticle;
import house.greenhouse.bovinesandbuttercups.client.particle.ShroomParticle;
import house.greenhouse.bovinesandbuttercups.client.platform.BovinesClientHelperNeoForge;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.*;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.MoobloomRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.CowLayersLayer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.FlowerCrownLayer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.MooshroomDatapackMushroomLayer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.CustomCowModelLayers;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.FlowerCrownModel;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.FlowerCrownItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelLayers;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelSetUtil;
import house.greenhouse.bovinesandbuttercups.client.util.ClearTextureCacheReloadListener;
import house.greenhouse.bovinesandbuttercups.content.block.entity.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.content.particle.BovinesParticleTypes;
import house.greenhouse.bovinesandbuttercups.mixin.neoforge.client.EntityRenderersEventAddLayersAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.*;
import net.minecraft.client.resources.model.ModelBakery.TextureGetter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Mod(value = BovinesAndButtercups.MOD_ID, dist = Dist.CLIENT)
public class BovinesAndButtercupsNeoForgeClient {
    public BovinesAndButtercupsNeoForgeClient(IEventBus eventBus) {
        BovinesAndButtercupsClient.init(new BovinesClientHelperNeoForge());
    }

    @EventBusSubscriber(modid = BovinesAndButtercups.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BovinesAndButtercupsClient.registerItemRenderers();
            BovinesModelSetTypes.init();
//            BovinesAccessoriesIntegrationClient.init();
        }

        @SubscribeEvent
        public static void registerClientReloadListeners(AddClientReloadListenersEvent event) {
            event.addListener(BovinesAndButtercups.asResource("clear_texture_cache"), new ClearTextureCacheReloadListener());
        }

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerMobEffect(LockdownClientEffectExtensions.INSTANCE, BovinesEffects.LOCKDOWN);
        }

        @SubscribeEvent
        public static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
            event.registerEntityModifier(MushroomCowRenderer.class, (mushroomCow, state) -> {

            });
        }

        private static final Map<ResourceLocation, UnbakedModel> MODEL_CACHE = new HashMap<>();

        @SubscribeEvent
        public static void additionalModels(ModelEvent.RegisterAdditional event) {
            List<ResourceLocation> models = BovinesModelSetUtil.getModels(Minecraft.getInstance().getResourceManager(), Runnable::run).join();
            for (ResourceLocation entry : models) {
                UnbakedModel unbaked = BovinesModelSetUtil.getUnbakedModel(entry);
                if (unbaked != null) {
                    unbaked.resolveDependencies(model -> {
                        event.register(model);
                        return null;
                    });
                    MODEL_CACHE.put(entry, unbaked);
                }
            }
            event.register(FlowerCrownItemRenderer.BASE);
        }

        @SubscribeEvent
        public static void bakeModels(ModelEvent.ModifyBakingResult event) {
            List<ResourceLocation> models = BovinesModelSetUtil.getModels(Minecraft.getInstance().getResourceManager(), Runnable::run).join();
            for (ResourceLocation entry : models) {
                UnbakedModel unbaked = MODEL_CACHE.get(entry);
                if (unbaked != null) {
                    event.getBakingResult().standaloneModels().put(entry, bakeModel(entry, unbaked, event.getTextureGetter(), event.getModelBakery()));
                }
            }
            MODEL_CACHE.clear();
        }

        private static BakedModel bakeModel(ResourceLocation key, UnbakedModel model, Function<Material, TextureAtlasSprite> textureGetter, ModelBakery bakery) {
            TextureGetter getter = new ModelBakery.TextureGetter() {

                @Override
                public TextureAtlasSprite get(ModelDebugName name, Material material) {
                    return textureGetter.apply(material);
                }

                @Override
                public TextureAtlasSprite reportMissingReference(ModelDebugName name, String reference) {
                    return textureGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()));
                }
            };

            return UnbakedModel.bakeWithTopModelValues(model, bakery.new ModelBakerImpl(getter, key::toString), BlockModelRotation.X0_Y0);
        }

        @SubscribeEvent
        public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(BovinesModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
            event.registerLayerDefinition(BovinesModelLayers.BABY_MOOBLOOM_MODEL_LAYER, () -> CowModel.createBodyLayer().apply(CowModel.BABY_TRANSFORMER));
            event.registerLayerDefinition(BovinesModelLayers.WARM_COW_MODEL_LAYER, CustomCowModelLayers::createWarm);
            event.registerLayerDefinition(BovinesModelLayers.BABY_WARM_COW_MODEL_LAYER, () -> CustomCowModelLayers.createWarm().apply(CowModel.BABY_TRANSFORMER));
            event.registerLayerDefinition(BovinesModelLayers.COLD_COW_MODEL_LAYER, CustomCowModelLayers::createCold);
            event.registerLayerDefinition(BovinesModelLayers.BABY_COLD_COW_MODEL_LAYER, () -> CustomCowModelLayers.createCold().apply(CowModel.BABY_TRANSFORMER));
            event.registerLayerDefinition(BovinesModelLayers.LUSH_COW_MODEL_LAYER, CustomCowModelLayers::createLush);
            event.registerLayerDefinition(BovinesModelLayers.BABY_LUSH_COW_MODEL_LAYER, () -> CustomCowModelLayers.createLush().apply(CowModel.BABY_TRANSFORMER));
            event.registerLayerDefinition(BovinesModelLayers.SCULK_COW_MODEL_LAYER, CustomCowModelLayers::createSculk);
            event.registerLayerDefinition(BovinesModelLayers.BABY_SCULK_COW_MODEL_LAYER, () -> CustomCowModelLayers.createSculk().apply(CowModel.BABY_TRANSFORMER));

            event.registerLayerDefinition(BovinesModelLayers.BUFFALO_COW_MODEL_LAYER, CustomCowModelLayers::createBuffalo);
            event.registerLayerDefinition(BovinesModelLayers.BABY_BUFFALO_COW_MODEL_LAYER, () -> CustomCowModelLayers.createBuffalo().apply(CowModel.BABY_TRANSFORMER));
            event.registerLayerDefinition(BovinesModelLayers.HIGHLAND_COW_MODEL_LAYER, CustomCowModelLayers::createHighland);
            event.registerLayerDefinition(BovinesModelLayers.BABY_HIGHLAND_COW_MODEL_LAYER, () -> CustomCowModelLayers.createHighland().apply(CowModel.BABY_TRANSFORMER));
            event.registerLayerDefinition(BovinesModelLayers.OX_COW_MODEL_LAYER, CustomCowModelLayers::createOx);
            event.registerLayerDefinition(BovinesModelLayers.BABY_OX_COW_MODEL_LAYER, () -> CustomCowModelLayers.createOx().apply(CowModel.BABY_TRANSFORMER));
            event.registerLayerDefinition(BovinesModelLayers.FLAT_COW_MODEL_LAYER, CustomCowModelLayers::createFlat);
            event.registerLayerDefinition(BovinesModelLayers.BABY_FLAT_COW_MODEL_LAYER, () -> CustomCowModelLayers.createFlat().apply(CowModel.BABY_TRANSFORMER));

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
            event.registerBlockEntityRenderer(BovinesBlockEntityTypes.PLACEABLE_EDIBLE, PlaceableEdibleBlockRenderer::new);
        }

        @SubscribeEvent
        public static void registerRenderLayers(EntityRenderersEvent.AddLayers event) {
            MushroomCowRenderer mushroomCowRenderer = event.getRenderer(EntityType.MOOSHROOM);
            mushroomCowRenderer.addLayer(new CowLayersLayer(mushroomCowRenderer));
            mushroomCowRenderer.addLayer(new MooshroomDatapackMushroomLayer<>(mushroomCowRenderer, event.getContext().getBlockRenderDispatcher()));

            List<LivingEntityRenderer<?, ?, ?>> renderers = new ArrayList<>();
            for (PlayerSkin.Model skin : event.getSkins()) {
                if (event.getSkin(skin) instanceof LivingEntityRenderer<?, ?, ?> livingRenderer) {
                    livingRenderer.addLayer(new FlowerCrownLayer(livingRenderer, modelLayerLocation -> event.getContext().bakeLayer((ModelLayerLocation) modelLayerLocation), event.getContext().getModelManager()));
                    renderers.add(livingRenderer);
                }
            }

            ((EntityRenderersEventAddLayersAccessor)event).bovinesandbuttercups$getRenderers().forEach((entityType, entityRenderer) -> {
                if (entityRenderer instanceof LivingEntityRenderer<?, ?, ?> livingRenderer && !renderers.contains(livingRenderer)) {
                    Model model = livingRenderer.getModel();
                    if (model instanceof HumanoidModel<?> || model instanceof IllagerModel<?> || model instanceof VillagerModel)
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
