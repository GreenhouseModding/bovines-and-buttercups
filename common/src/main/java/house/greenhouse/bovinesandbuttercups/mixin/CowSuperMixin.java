package house.greenhouse.bovinesandbuttercups.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Cow.class)
public abstract class CowSuperMixin extends Animal {
    protected CowSuperMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    public abstract void thunderHit(ServerLevel level, LightningBolt bolt);

    @Unique
    public void bovinesandbuttercups$superThunderHit(ServerLevel level, LightningBolt bolt) {
        super.thunderHit(level, bolt);
    }
}
