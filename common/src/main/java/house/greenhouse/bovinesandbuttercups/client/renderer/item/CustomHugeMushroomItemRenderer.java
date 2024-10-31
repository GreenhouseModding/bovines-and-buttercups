package house.greenhouse.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSet;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.BovinesModelSetTypes;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.StateDefinitionBovinesModelSetType;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CustomHugeMushroomItemRenderer {

    public static void render(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        BakedModel bakedModel = StateDefinitionBovinesModelSetType.getItemModel(BovinesModelSetRegistry.get(BovinesAndButtercups.asResource("missing_mushroom_block")));
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (stack.has(BovinesDataComponents.CUSTOM_MUSHROOM)) {
            @Nullable BovinesModelSet modelSet = BovinesModelSetRegistry.get(stack.get(BovinesDataComponents.CUSTOM_MUSHROOM).holder().unwrapKey().get().location().withPath(s -> s + "_block"));

            if (modelSet != null) {
                var newModel = StateDefinitionBovinesModelSetType.getItemModel(modelSet);
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
