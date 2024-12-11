package house.greenhouse.bovinesandbuttercups.mixin.client;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelBakery.ModelBakerImpl.class)
public interface ModelBakeryModelBakerImplInvoker {
    @Invoker("getModel")
    UnbakedModel bovinesandbuttercups$getModel(ResourceLocation model);
}
