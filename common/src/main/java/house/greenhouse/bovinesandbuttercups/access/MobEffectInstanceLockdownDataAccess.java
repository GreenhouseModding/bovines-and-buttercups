package house.greenhouse.bovinesandbuttercups.access;

import house.greenhouse.bovinesandbuttercups.util.LockdownData;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public interface MobEffectInstanceLockdownDataAccess {
    List<LockdownData> bovinesandbuttercups$getLockdownData();
    MobEffectInstance bovinesandbuttercups$setLockdownData(List<LockdownData> data);
}
