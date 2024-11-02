package house.greenhouse.bovinesandbuttercups.client.platform;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface BovinesClientHelper {
    BakedModel getModel(ResourceLocation resourceLocation);

    ItemStack getEquippedFlowerCrownForRendering(LivingEntity entity);

    BakedModel createPlaceableEdibleModel(List<Pair<PlaceableEdibleSelector, BakedModel>> selectors);
}
