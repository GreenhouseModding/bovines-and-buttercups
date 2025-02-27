package house.greenhouse.bovinesandbuttercups.mixin;

import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(MushroomCow.class)
public interface MushroomCowAccessor {
    @Accessor("lastLightningBoltUUID")
    void bovinesandbuttercups$setLastLightningBoltUUID(UUID uuid);
}