package house.greenhouse.bovinesandbuttercups.client.platform;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.client.api.model.condition.PlaceableEdibleSelector;
import house.greenhouse.bovinesandbuttercups.client.model.PlaceableEdibleMultiPartBakedModel;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.EquipmentChecking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class BovinesClientHelperNeoForge implements BovinesClientHelper {
    @Override
    public BakedModel getModel(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(resourceLocation));
    }

    @Override
    public ItemStack getEquippedFlowerCrownForRendering(LivingEntity entity) {
        if (ModList.get().isLoaded("accessories")) {
            var accessoriesCapability = AccessoriesCapability.getOptionally(entity);
            if (accessoriesCapability.isPresent()) {
                var flowerCrown = accessoriesCapability.get().getFirstEquipped(stack -> stack.has(BovinesDataComponents.FLOWER_CROWN), EquipmentChecking.COSMETICALLY_OVERRIDABLE);
                if (flowerCrown != null) {
                    var container = accessoriesCapability.get().getContainer(flowerCrown.reference().type());
                    if (container != null && container.shouldRender(flowerCrown.reference().slot()))
                        return flowerCrown.stack();
                }
            }
        }

        if (ModList.get().isLoaded("curios")) {
            var curiosInventory = CuriosApi.getCuriosInventory(entity);
            if (curiosInventory.isPresent()) {
                var flowerCrown = curiosInventory.get().findFirstCurio(stack -> stack.has(BovinesDataComponents.FLOWER_CROWN));
                if (flowerCrown.isPresent() && flowerCrown.get().slotContext().visible())
                    return flowerCrown.get().stack();
            }
        }

        if (entity.getItemBySlot(EquipmentSlot.HEAD).has(BovinesDataComponents.FLOWER_CROWN))
            return entity.getItemBySlot(EquipmentSlot.HEAD);

        return ItemStack.EMPTY;
    }

    @Override
    public BakedModel createPlaceableEdibleModel(List<Pair<PlaceableEdibleSelector, BakedModel>> selectors) {
        return new PlaceableEdibleMultiPartBakedModel(selectors);
    }
}
