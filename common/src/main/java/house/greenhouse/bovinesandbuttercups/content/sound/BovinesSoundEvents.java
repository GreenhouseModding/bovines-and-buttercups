package house.greenhouse.bovinesandbuttercups.content.sound;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class BovinesSoundEvents {
    public static final SoundEvent COW_CONVERT = register(BovinesAndButtercups.asResource("entity.cow.convert"), SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.cow.convert")));
    public static final SoundEvent MOOBLOOM_EAT = register(BovinesAndButtercups.asResource("entity.moobloom.eat"), SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.eat")));
    public static final SoundEvent MOOBLOOM_MILK = register(BovinesAndButtercups.asResource("entity.moobloom.milk"), SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.milk")));
    public static final SoundEvent MOOBLOOM_SHEAR = register(BovinesAndButtercups.asResource("entity.moobloom.shear"), SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.shear")));
    public static final SoundEvent MOOBLOOM_CONVERT = register(BovinesAndButtercups.asResource("entity.moobloom.convert"), SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.convert")));
    public static final SoundEvent MOOBLOOM_PLANT = register(BovinesAndButtercups.asResource("entity.moobloom.plant"), SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("entity.moobloom.plant")));
    public static final Holder<SoundEvent> EQUIP_FLOWER_CROWN = registerHolder(BovinesAndButtercups.asResource("item.armor.equip_flower_crown"), SoundEvent.createVariableRangeEvent(BovinesAndButtercups.asResource("item.armor.equip_flower_crown")));

    public static void registerAll() {}

    private static SoundEvent register(ResourceLocation id, SoundEvent event) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, event);
    }

    private static Holder<SoundEvent> registerHolder(ResourceLocation id, SoundEvent event) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, event);
    }
}
