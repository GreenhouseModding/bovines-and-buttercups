package house.greenhouse.bovinesandbuttercups.mixin;

import house.greenhouse.bovinesandbuttercups.access.BeeGoalAccess;
import house.greenhouse.bovinesandbuttercups.content.entity.goal.MoveToMoobloomGoal;
import house.greenhouse.bovinesandbuttercups.content.entity.goal.PollinateMoobloomGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal implements BeeGoalAccess {
    @Nullable @Unique private PollinateMoobloomGoal bovinesandbuttercups$pollinateMoobloomGoal;

    @Shadow
    public abstract GoalSelector getGoalSelector();

    protected BeeMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public PollinateMoobloomGoal bovinesandbuttercups$getPollinateMoobloomGoal() {
        return this.bovinesandbuttercups$pollinateMoobloomGoal;
    }

    @Override
    public void bovinesandbuttercups$setPollinateMoobloomGoal(PollinateMoobloomGoal goal) {
        this.bovinesandbuttercups$pollinateMoobloomGoal = goal;
    }
}
