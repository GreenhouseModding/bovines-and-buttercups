package house.greenhouse.bovinesandbuttercups.client;

import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.MoobloomModel;
import house.greenhouse.bovinesandbuttercups.integration.accessories.client.BovinesAccessoriesIntegrationClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.particle.BloomParticle;
import house.greenhouse.bovinesandbuttercups.client.particle.ModelLocationParticle;
import house.greenhouse.bovinesandbuttercups.client.particle.ShroomParticle;
import house.greenhouse.bovinesandbuttercups.client.platform.BovinesClientHelperFabric;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelLayers;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomFlowerPotBlockRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomFlowerRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomHugeMushroomBlockRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomMushroomPotBlockRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.block.CustomMushroomRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.MoobloomRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.FlowerCrownModel;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.CustomFlowerItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.CustomHugeMushroomItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.CustomMushroomItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.FlowerCrownItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.NectarBowlItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.bovinestate.BovineBlockstateTypes;
import house.greenhouse.bovinesandbuttercups.client.util.BovineStateModelUtil;
import house.greenhouse.bovinesandbuttercups.client.util.ClearTextureCacheReloadListener;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncConditionedTextureModifier;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncCowTypeClientboundPacket;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncLockdownEffectsClientboundPacket;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlockEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.registry.BovinesEntityTypes;
import house.greenhouse.bovinesandbuttercups.registry.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesParticleTypes;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class BovinesAndButtercupsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BovinesAndButtercupsClient.init(new BovinesClientHelperFabric());
        BovinesAccessoriesIntegrationClient.init();
        BovineBlockstateTypes.init();

        EntityModelLayerRegistry.registerModelLayer(BovinesModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(BovinesModelLayers.FLOWER_CROWN_MODEL_LAYER, () -> FlowerCrownModel.createLayer(new CubeDeformation(0.75F)));
        EntityModelLayerRegistry.registerModelLayer(BovinesModelLayers.PIGLIN_FLOWER_CROWN_MODEL_LAYER, () -> FlowerCrownModel.createLayer(new CubeDeformation(1.5F, 0.5F, 0.5F)));
        EntityRendererRegistry.register(BovinesEntityTypes.MOOBLOOM, MoobloomRenderer::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
            private final ClearTextureCacheReloadListener listener = new ClearTextureCacheReloadListener();

            @Override
            public ResourceLocation getFabricId() {
                return BovinesAndButtercups.asResource("clear_texture_cache");
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return listener.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }
        });

        PreparableModelLoadingPlugin.register(BovineStateModelUtil::getModels, (data, context) -> {
            context.addModels(data);
            context.resolveModel().register((ctx) -> BovineStateModelUtil.getUnbakedModel(ctx.id(), ctx::getOrLoadModel));
        });
        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(FlowerCrownItemRenderer.BASE));

        ClientPlayNetworking.registerGlobalReceiver(SyncConditionedTextureModifier.TYPE, (packet, context) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(SyncCowTypeClientboundPacket.TYPE, (packet, context) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(SyncLockdownEffectsClientboundPacket.TYPE, (packet, context) -> packet.handle());

        registerBlockLayers();
        registerBlockRenderers();
        registerItemRenderers();
        registerParticleFactories();
    }

    public static void registerBlockRenderers() {
        BlockEntityRenderers.register(BovinesBlockEntityTypes.CUSTOM_FLOWER, CustomFlowerRenderer::new);
        BlockEntityRenderers.register(BovinesBlockEntityTypes.CUSTOM_MUSHROOM, CustomMushroomRenderer::new);
        BlockEntityRenderers.register(BovinesBlockEntityTypes.POTTED_CUSTOM_FLOWER, CustomFlowerPotBlockRenderer::new);
        BlockEntityRenderers.register(BovinesBlockEntityTypes.POTTED_CUSTOM_MUSHROOM, CustomMushroomPotBlockRenderer::new);
        BlockEntityRenderers.register(BovinesBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK, CustomHugeMushroomBlockRenderer::new);
    }

    public static void registerItemRenderers() {
        BuiltinItemRendererRegistry.INSTANCE.register(BovinesItems.CUSTOM_FLOWER, CustomFlowerItemRenderer::render);
        BuiltinItemRendererRegistry.INSTANCE.register(BovinesItems.CUSTOM_MUSHROOM, CustomMushroomItemRenderer::render);
        BuiltinItemRendererRegistry.INSTANCE.register(BovinesItems.CUSTOM_MUSHROOM_BLOCK, CustomHugeMushroomItemRenderer::render);
        BuiltinItemRendererRegistry.INSTANCE.register(BovinesItems.NECTAR_BOWL, NectarBowlItemRenderer::render);
        BuiltinItemRendererRegistry.INSTANCE.register(BovinesItems.FLOWER_CROWN, FlowerCrownItemRenderer::render);
    }

    public static void registerParticleFactories() {
        ParticleFactoryRegistry.getInstance().register(BovinesParticleTypes.MODEL_LOCATION, new ModelLocationParticle.Provider());
        ParticleFactoryRegistry.getInstance().register(BovinesParticleTypes.BLOOM, BloomParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(BovinesParticleTypes.SHROOM, ShroomParticle.Provider::new);
    }

    public static void registerBlockLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.BIRD_OF_PARADISE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_BIRD_OF_PARADISE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.BUTTERCUP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_BUTTERCUP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.CHARGELILY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.CHARGELILY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.FREESIA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_FREESIA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.HYACINTH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_HYACINTH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.LIMELIGHT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_LIMELIGHT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.LINGHOLM, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_LINGHOLM, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.PINK_DAISY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_PINK_DAISY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.SNOWDROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_SNOWDROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.TROPICAL_BLUE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_TROPICAL_BLUE, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.CUSTOM_FLOWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_CUSTOM_FLOWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.CUSTOM_MUSHROOM, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BovinesBlocks.POTTED_CUSTOM_MUSHROOM, RenderType.cutout());
    }
}
