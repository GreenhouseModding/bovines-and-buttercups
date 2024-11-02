package house.greenhouse.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.EdibleBlockBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlaceableEdibleItemRenderer {
    public static void render(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        BakedModel bakedModel = EdibleBlockBovinesModelSetType.getItemModel(BovinesModelSetRegistry.get(BovinesAndButtercups.asResource("missing_edible")));
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (stack.has(BovinesDataComponents.EDIBLE_TYPE)) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(stack.get(BovinesDataComponents.EDIBLE_TYPE).holder().unwrapKey().get().location());

            if (modelSet != null) {
                var newModel = EdibleBlockBovinesModelSetType.getItemModel(modelSet);
                if (newModel != null)
                    bakedModel = newModel;
            }
        }

        boolean left = context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        poseStack.translate(0.5F, 0.5F, 0.5F);

        boolean bl = context == ItemDisplayContext.GUI && !bakedModel.usesBlockLight();
        MultiBufferSource.BufferSource source = null;

        if (bl) {
            Lighting.setupForFlatItems();
            source = Minecraft.getInstance().renderBuffers().bufferSource();
        }

        Minecraft.getInstance().getItemRenderer().render(stack, context, left, poseStack, source == null ? bufferSource : source, light, overlay, bakedModel);

        if (bl) {
            source.endBatch();
            Lighting.setupFor3DItems();
        }
    }
}
