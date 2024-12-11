package house.greenhouse.bovinesandbuttercups.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import house.greenhouse.bovinesandbuttercups.util.AdvancementUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;
import java.util.Objects;

@Mixin(SimpleJsonResourceReloadListener.class)
public class SimpleJsonResourceReloadListenerMixin {
    @ModifyArg(method = "scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"))
    private static Object bovinesandbuttercups$addAdvancementSourceToJson(Object original, @Local(ordinal = 1) ResourceLocation fileToId, @Local Map.Entry<ResourceLocation, Resource> entry) {
        JsonElement element = (JsonElement) original;
        if (entry.getKey().getPath().startsWith("advancement/") && entry.getValue().sourcePackId().equals("vanilla")) {
            if (fileToId.equals(ResourceLocation.withDefaultNamespace("husbandry/bred_all_animals")))
                return AdvancementUtil.addMoobloomToBredAllAnimals(element.getAsJsonObject());
            if (fileToId.equals(ResourceLocation.withDefaultNamespace("husbandry/balanced_diet")))
                return AdvancementUtil.addRichHoneyBottleToBalancedDiet(element.getAsJsonObject());
            if (fileToId.equals(ResourceLocation.withDefaultNamespace("adventure/honey_block_slide")))
                return AdvancementUtil.addRichHoneyBlockToHoneyBlockSlide(element.getAsJsonObject());
        }
        return element;
    }
}
