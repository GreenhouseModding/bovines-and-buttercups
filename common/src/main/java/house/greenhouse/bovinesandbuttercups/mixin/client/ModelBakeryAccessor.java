package house.greenhouse.bovinesandbuttercups.mixin.client;

import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelBakery.class)
public interface ModelBakeryAccessor {
    @Accessor("ITEM_MODEL_GENERATOR")
    static ItemModelGenerator bovinesandbuttercups$getItemModelGenerator() {
        throw new RuntimeException("");
    }

    @Accessor("missingModel")
    UnbakedModel bovinesandbuttercups$getMissingModel();

    @Invoker("getModel")
    UnbakedModel bovinesandbuttercups$getModel(ResourceLocation location);
}
