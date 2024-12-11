//package house.greenhouse.bovinesandbuttercups.integration.accessories;
//
//import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
//import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
//import io.wispforest.accessories.api.events.AccessoryChangeCallback;
//import net.minecraft.world.entity.LivingEntity;
//
///*
// * I fucking love Architectury devs.
// * Why are we using a Fabric specific lib for code that's meant to be as multiplatform as possible?
// */
//public class BovinesAccessoriesEvents {
//    public static void init() {
//        if (!BovinesAndButtercups.getHelper().isModLoaded("accessories"))
//            return;
//        AccessoryChangeCallback.EVENT.register((prevStack, currentStack, reference, stateChange) -> {
//            LivingEntity entity = reference.entity();
//            if (currentStack.getItem() instanceof FlowerCrownItem fcItem && !entity.level().isClientSide() && !entity.isSilent())
//                entity.level().playSeededSound(null, entity.getX(), entity.getY(), entity.getZ(), fcItem.getEquipSound(), entity.getSoundSource(), 1.0F, 1.0F, entity.getRandom().nextLong());
//        });
//    }
//}
