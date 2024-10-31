package house.greenhouse.bovinesandbuttercups.content.sound;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.registry.HolderRegistrationCallback;
import house.greenhouse.bovinesandbuttercups.registry.RegistrationCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class BovinesSoundEvents {
    public static final SoundEvent MOOBLOOM_EAT = SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.eat"));
    public static final SoundEvent MOOBLOOM_MILK = SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.milk"));
    public static final SoundEvent MOOBLOOM_SHEAR = SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.shear"));
    public static final SoundEvent MOOBLOOM_CONVERT = SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.convert"));
    public static Holder<SoundEvent> EQUIP_FLOWER_CROWN;

    public static void registerAll(RegistrationCallback<SoundEvent> callback) {
        callback.register(BuiltInRegistries.SOUND_EVENT, BovinesAndButtercups.asResource("entity.moobloom.eat"), MOOBLOOM_EAT);
        callback.register(BuiltInRegistries.SOUND_EVENT, BovinesAndButtercups.asResource("entity.moobloom.milk"), MOOBLOOM_MILK);
        callback.register(BuiltInRegistries.SOUND_EVENT, BovinesAndButtercups.asResource("entity.moobloom.shear"), MOOBLOOM_SHEAR);
        callback.register(BuiltInRegistries.SOUND_EVENT, BovinesAndButtercups.asResource("entity.moobloom.convert"), MOOBLOOM_CONVERT);
    }

    public static void registerHolders(HolderRegistrationCallback<SoundEvent> callback) {
        EQUIP_FLOWER_CROWN = callback.register(BuiltInRegistries.SOUND_EVENT, BovinesAndButtercups.asResource("item.armor.equip_flower_crown"), SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("item.armor.equip_flower_crown")));
    }
}
