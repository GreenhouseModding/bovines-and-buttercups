package house.greenhouse.bovinesandbuttercups.client.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.api.CowTypeRenderState;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import house.greenhouse.bovinesandbuttercups.client.model.PlaceableEdibleMultiPartBakedModel;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MooshroomConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.List;

public class BovinesClientHelperNeoForge implements BovinesClientHelper {
    @Override
    public ModelBakery getModelBakery() {
        return Minecraft.getInstance().getModelManager().getModelBakery();
    }

    @Override
    public BakedModel getModel(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().getModelManager().getStandaloneModel(resourceLocation);
    }

    @Override
    public ItemStack getEquippedFlowerCrownForRendering(LivingEntity entity) {
//        if (ModList.get().isLoaded("accessories")) {
//            var accessoriesCapability = AccessoriesCapability.getOptionally(entity);
//            if (accessoriesCapability.isPresent()) {
//                var flowerCrown = accessoriesCapability.get().getFirstEquipped(stack -> stack.has(BovinesDataComponents.FLOWER_CROWN), EquipmentChecking.COSMETICALLY_OVERRIDABLE);
//                if (flowerCrown != null) {
//                    var container = accessoriesCapability.get().getContainer(flowerCrown.reference().type());
//                    if (container != null && container.shouldRender(flowerCrown.reference().slot()))
//                        return flowerCrown.stack();
//                }
//            }
//        }
//
//        if (ModList.get().isLoaded("curios")) {
//            var curiosInventory = CuriosApi.getCuriosInventory(entity);
//            if (curiosInventory.isPresent()) {
//                var flowerCrown = curiosInventory.get().findFirstCurio(stack -> stack.has(BovinesDataComponents.FLOWER_CROWN));
//                if (flowerCrown.isPresent() && flowerCrown.get().slotContext().visible())
//                    return flowerCrown.get().stack();
//            }
//        }

        if (entity.getItemBySlot(EquipmentSlot.HEAD).has(BovinesDataComponents.FLOWER_CROWN))
            return entity.getItemBySlot(EquipmentSlot.HEAD);

        return ItemStack.EMPTY;
    }

    @Override
    public BakedModel createPlaceableEdibleModel(List<Pair<PlaceableEdibleSelector, BakedModel>> selectors) {
        return new PlaceableEdibleMultiPartBakedModel(selectors);
    }

    @Override
    public void tesselateBlock(BlockRenderDispatcher dispatcher, BlockAndTintGetter level, BakedModel model, BlockState state, BlockPos pos, PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType, boolean checkSides, RandomSource random, long seed, int packedOverlay) {
        dispatcher.getModelRenderer().tesselateBlock(level, model, state, pos, poseStack, bufferSource.getBuffer(renderType), checkSides, random, seed, packedOverlay, model.getModelData(level, pos, state, ModelData.EMPTY), renderType);
    }
}
