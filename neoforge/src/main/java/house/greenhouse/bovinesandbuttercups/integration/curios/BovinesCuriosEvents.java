//package house.greenhouse.bovinesandbuttercups.integration.curios;
//
//import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
//import house.greenhouse.bovinesandbuttercups.content.item.FlowerCrownItem;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.neoforge.common.NeoForge;
//import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
//import top.theillusivec4.curios.api.event.CurioChangeEvent;
//
//public class BovinesCuriosEvents {
//    public static void init() {
//        if (!BovinesAndButtercups.getHelper().isModLoaded("curios"))
//            return;
//        NeoForge.EVENT_BUS.addListener(BovinesCuriosEvents::onCurioChange);
//        NeoForge.EVENT_BUS.addListener(BovinesCuriosEvents::onEntityJoinLevel);
//    }
//
//    private static boolean entityJoining = false;
//
//    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
//        entityJoining = true;
//    }
//
//    public static void onCurioChange(CurioChangeEvent event) {
//        Entity entity = event.getEntity();
//        if (entity.isSilent() || ItemStack.isSameItemSameComponents(event.getFrom(), event.getTo()) || !(event.getTo().getItem() instanceof FlowerCrownItem fcItem) || entityJoining) {
//            entityJoining = false;
//            return;
//        }
//        entity.level().playSeededSound(null, entity.getX(), entity.getY(), entity.getZ(), fcItem.getEquipSound(), entity.getSoundSource(), 1.0F, 1.0F, entity.getRandom().nextLong());
//        entityJoining = false;
//    }
//}