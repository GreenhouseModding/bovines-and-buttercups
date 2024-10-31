package house.greenhouse.bovinesandbuttercups.client.platform;

import dev.emi.trinkets.api.TrinketsApi;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.EquipmentChecking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BovinesClientHelperFabric implements BovinesClientHelper {
    @Override
    public BakedModel getModel(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().getModelManager().getModel(resourceLocation);
    }

    @Override
    public ItemStack getEquippedFlowerCrownForRendering(LivingEntity entity) {
        if (FabricLoader.getInstance().isModLoaded("accessories")) {
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

        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            var trinketComponent = TrinketsApi.getTrinketComponent(entity);
            if (trinketComponent.isPresent()) {
                var flowerCrowns = trinketComponent.get().getEquipped(stack -> stack.has(BovinesDataComponents.FLOWER_CROWN));
                if (!flowerCrowns.isEmpty())
                    return flowerCrowns.getFirst().getB();
            }
        }

        if (entity.getItemBySlot(EquipmentSlot.HEAD).has(BovinesDataComponents.FLOWER_CROWN))
            return entity.getItemBySlot(EquipmentSlot.HEAD);

        return ItemStack.EMPTY;
    }
}
