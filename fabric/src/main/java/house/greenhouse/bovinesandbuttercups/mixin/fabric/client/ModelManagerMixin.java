package house.greenhouse.bovinesandbuttercups.mixin.fabric.client;

import house.greenhouse.bovinesandbuttercups.client.BovinesAndButtercupsFabricClient;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SpecialBlockModelRenderer;
import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Inject(method = "loadModels", at = @At("HEAD"))
    private static void bovinesandbuttercups$storeModelBakery(ProfilerFiller profiler, Map<ResourceLocation, AtlasSet.StitchResult> atlasPreperations, ModelBakery modelBakery, Object2IntMap<BlockState> modelGroups, EntityModelSet entityModelSet, SpecialBlockModelRenderer specialBlockModelRenderer, CallbackInfoReturnable<ModelManager.ReloadState> cir) {
        BovinesAndButtercupsFabricClient.setBakery(modelBakery);
    }
}