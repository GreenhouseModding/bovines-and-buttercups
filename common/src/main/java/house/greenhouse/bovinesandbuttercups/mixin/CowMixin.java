package house.greenhouse.bovinesandbuttercups.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Cow.class)
public abstract class CowMixin extends Animal {
    protected CowMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Unique
    public void bovinesandbuttercups$superThunderHit(ServerLevel level, LightningBolt bolt) {
        super.thunderHit(level, bolt);
    }
}
