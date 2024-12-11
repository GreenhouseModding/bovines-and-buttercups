//package house.greenhouse.bovinesandbuttercups.integration.trinkets;
//
//import dev.emi.trinkets.api.event.TrinketEquipCallback;
//import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
//import net.fabricmc.loader.api.FabricLoader;
//
//public class BovinesTrinketsEvents {
//    public static void init() {
//        if (!FabricLoader.getInstance().isModLoaded("trinkets"))
//            return;
//        TrinketEquipCallback.EVENT.register((stack, slot, entity) -> {
//            if (stack.getItem() instanceof FlowerCrownItem fcItem && !entity.level().isClientSide() && !entity.isSilent())
//                entity.level().playSeededSound(null, entity.getX(), entity.getY(), entity.getZ(), fcItem.getEquipSound(), entity.getSoundSource(), 1.0F, 1.0F, entity.getRandom().nextLong());
//        });
//    }
//}
