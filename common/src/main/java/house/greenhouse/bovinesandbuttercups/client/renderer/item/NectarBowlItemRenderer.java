package house.greenhouse.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import house.greenhouse.bovinesandbuttercups.client.bovinestate.BovineStatesAssociationRegistry;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class NectarBowlItemRenderer {
    public static void render(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        ResourceLocation resourceLocation = BovinesAndButtercups.asResource("bovinesandbuttercups/item/buttercup_nectar_bowl/inventory");
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (stack.has(BovinesDataComponents.NECTAR)) {
            Optional<ResourceLocation> modelLocationWithoutVariant = Optional.ofNullable(stack.get(BovinesDataComponents.NECTAR)).map(itemNectar -> itemNectar.nectar().value().modelLocation());
            if (modelLocationWithoutVariant.isPresent())
                resourceLocation = BovineStatesAssociationRegistry.getItem(modelLocationWithoutVariant.get()).orElse(BovinesAndButtercups.asResource("bovinesandbuttercups/item/buttercup_nectar_bowl/inventory"));
        } else
            resourceLocation = BovinesAndButtercups.asResource("bovinesandbuttercups/item/buttercup_nectar_bowl/inventory");

        BakedModel nectarBowlModel = BovinesAndButtercupsClient.getHelper().getModel(resourceLocation);

        boolean left = context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        poseStack.translate(0.5F, 0.5F, 0.5F);

        boolean bl = context == ItemDisplayContext.GUI && !nectarBowlModel.usesBlockLight();
        MultiBufferSource.BufferSource source = null;

        if (bl) {
            Lighting.setupForFlatItems();
            source = Minecraft.getInstance().renderBuffers().bufferSource();
        }

        Minecraft.getInstance().getItemRenderer().render(stack, context, left, poseStack, source == null ? bufferSource : source, light, overlay, nectarBowlModel);

        if (bl) {
            source.endBatch();
            Lighting.setupFor3DItems();
        }
    }
}
