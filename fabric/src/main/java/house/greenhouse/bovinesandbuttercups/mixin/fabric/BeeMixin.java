package house.greenhouse.bovinesandbuttercups.mixin.fabric;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.access.BeeGoalAccess;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.entity.goal.MoveToMoobloomGoal;
import house.greenhouse.bovinesandbuttercups.content.entity.goal.PollinateMoobloomGoal;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal {
    @Shadow public abstract GoalSelector getGoalSelector();

    protected BeeMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(method = "registerGoals", at = @At(value = "TAIL"))
    private void bovinesandbuttercups$addMoobloomRelatedGoals(CallbackInfo ci) {
        Bee bee = (Bee)(Object)this;
        AvoidEntityGoal<Moobloom> avoidEntityGoal = new AvoidEntityGoal<>(bee, Moobloom.class, living -> true, 12.0F, 1.4F, 1.4F, living ->
                EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(living) && BovinesAndButtercups.getHelper().getAvoidingMoobloom(bee) != null && BovinesAndButtercups.getHelper().getAvoidingMoobloom(bee).getUUID() == living.getUUID());
        getGoalSelector().addGoal(2, avoidEntityGoal);
        PollinateMoobloomGoal pollinateGoal = new PollinateMoobloomGoal(bee);
        getGoalSelector().addGoal(3, pollinateGoal);
        getGoalSelector().addGoal(5, new MoveToMoobloomGoal((Bee)(Object)this));
        ((BeeGoalAccess)this).bovinesandbuttercups$setPollinateMoobloomGoal(pollinateGoal);
    }
}
