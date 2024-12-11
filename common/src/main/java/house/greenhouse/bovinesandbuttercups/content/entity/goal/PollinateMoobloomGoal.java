package house.greenhouse.bovinesandbuttercups.content.entity.goal;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.mixin.BeeAccessor;
import house.greenhouse.bovinesandbuttercups.mixin.MobAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

public class PollinateMoobloomGoal extends Bee.BaseBeeGoal {
    private final Bee bee;
    private static final int MIN_POLLINATION_TICKS = 400;
    private static final int MIN_FIND_FLOWER_COW_RETRY_COOLDOWN = 20;
    private static final int MAX_FIND_FLOWER_COW_RETRY_COOLDOWN = 60;
    private static final double ARRIVAL_THRESHOLD = 0.1D;
    private static final int POSITION_CHANGE_CHANCE = 25;
    private static final float SPEED_MODIFIER = 0.6F;
    private static final float HOVER_POS_OFFSET = 0.33333334F;
    private int successfulPollinatingTicks;
    private int lastSoundPlayedTick;
    private boolean pollinating;
    @Nullable
    private Vec3 hoverPos;
    private int pollinatingTicks;
    private static final int MAX_POLLINATING_TICKS = 600;
    private int remainingCooldownBeforeLocatingNewCow;
    private Moobloom moobloom;

    public PollinateMoobloomGoal(Bee bee) {
        bee.super();
        this.bee = bee;
        remainingCooldownBeforeLocatingNewCow = Mth.nextInt(bee.getRandom(), MIN_FIND_FLOWER_COW_RETRY_COOLDOWN, MAX_FIND_FLOWER_COW_RETRY_COOLDOWN);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canBeeUse() {
        if (this.remainingCooldownBeforeLocatingNewCow > 0) {
            return false;
        } if (bee.hasNectar()) {
            return false;
        } else if (bee.level().isRaining()) {
            return false;
        } else if (BovinesAndButtercups.getHelper().getPollinatingMoobloom(bee).isPresent()) {
            Optional<Entity> entity = Optional.ofNullable(((ServerLevel)bee.level()).getEntity(BovinesAndButtercups.getHelper().getPollinatingMoobloom(bee).get()));
            if (entity.isPresent() && entity.get() instanceof Moobloom moobloom) {
                this.moobloom = moobloom;
                setMoobloom();
                return true;
            }
            return false;
        } else if (this.moobloom == null && this.bee.hasSavedFlowerPos()) {
            return false;
        } else {
            Optional<Moobloom> moobloom = findMoobloom();
            if (moobloom.isPresent()) {
                this.moobloom = moobloom.get();
                BovinesAndButtercups.getHelper().setPollinatingMoobloom(bee, moobloom.get().getUUID());
                setMoobloom();
                return true;
            } else {
                this.remainingCooldownBeforeLocatingNewCow = Mth.nextInt(bee.getRandom(), 20, 60);
                return false;
            }
        }
    }

    private void setMoobloom() {
        moobloom.setStandingStillForBeeTicks(MAX_POLLINATING_TICKS);
        moobloom.setBee(bee);
        bee.setSavedFlowerPos(null);
        ((MobAccessor)this.bee).bovinesandbuttercups$getNavigation().moveTo(moobloom.position().x(), moobloom.position().y() + moobloom.getBoundingBox().getYsize() * 1.5, moobloom.position().z(), 1.0);
    }

    @Override
    public boolean canBeeContinueToUse() {
        if (!this.pollinating) {
            return false;
        } else if (this.moobloom == null) {
            return false;
        } else if (bee.level().isRaining()) {
            return false;
        } else if (this.hasPollinatedLongEnough()) {
            return bee.getRandom().nextFloat() < 0.2F;
        } if (bee.tickCount % 20 == 0 && (!moobloom.isAlive() || moobloom.getLastHurtByMobTimestamp() > moobloom.tickCount - 100)) {
            moobloom.setStandingStillForBeeTicks(0);
            moobloom.setBee(null);
            BovinesAndButtercups.getHelper().setPollinatingMoobloom(bee, null);
            bee.setSavedFlowerPos(null);
            this.moobloom = null;
            return false;
        } else {
            return true;
        }
    }

    private boolean hasPollinatedLongEnough() {
        return this.successfulPollinatingTicks > MIN_POLLINATION_TICKS;
    }

    public boolean isPollinating() {
        return this.pollinating;
    }

    public void stopPollinating() {
        this.pollinating = false;
    }

    public void tickCooldown() {
        if (this.remainingCooldownBeforeLocatingNewCow > 0) {
            --this.remainingCooldownBeforeLocatingNewCow;
        }
    }

    public void start() {
        this.successfulPollinatingTicks = 0;
        this.pollinatingTicks = 0;
        this.lastSoundPlayedTick = 0;
        this.pollinating = true;
        bee.resetTicksWithoutNectarSinceExitingHive();
    }

    public void stop() {
        if (this.hasPollinatedLongEnough()) {
            ((BeeAccessor)bee).bovinesandbuttercups$invokeSetHasNectar(true);
            BovinesAndButtercups.getHelper().setProducesRichHoney(bee, true);
            moobloom.setPollinatedResetTicks(1800);
            if (!moobloom.level().isClientSide) {
                ((ServerLevel) moobloom.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, moobloom.position().x(), moobloom.position().y() + 1.4D, moobloom.position().z(), 8, 0.5, 0.1, 0.4, 0.0);
            }
        }

        this.pollinating = false;
        bee.getNavigation().stop();
        if (this.moobloom != null) {
            moobloom.setStandingStillForBeeTicks(0);
            moobloom.setBee(null);
            BovinesAndButtercups.getHelper().setPollinatingMoobloom(bee, null);
            this.moobloom = null;
        }
        this.remainingCooldownBeforeLocatingNewCow = 200;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.moobloom == null) return;
        ++this.pollinatingTicks;
        if (this.pollinatingTicks > MAX_POLLINATING_TICKS) {
            moobloom.setStandingStillForBeeTicks(0);
            moobloom.setBee(null);
            bee.setSavedFlowerPos(null);
            BovinesAndButtercups.getHelper().setPollinatingMoobloom(bee, null);
            this.moobloom = null;
        } else if (BovinesAndButtercups.getHelper().getPollinatingMoobloom(bee).isPresent() && !bee.level().isClientSide()) {
            Entity entity = ((ServerLevel)bee.level()).getEntity(BovinesAndButtercups.getHelper().getPollinatingMoobloom(bee).get());
            if ((entity instanceof Moobloom moobloom)) {
                moobloom.setStandingStillForBeeTicks(MAX_POLLINATING_TICKS);
                moobloom.setBee(this.bee);
                Vec3 vec3 = moobloom.position().add(0.0f, moobloom.getBoundingBox().getYsize() * 1.3, 0.0f);
                if (vec3.distanceTo(bee.position()) > 1.0D) {
                    this.hoverPos = vec3;
                    this.setWantedPos();
                } else {
                    if (this.hoverPos == null) {
                        this.hoverPos = vec3;
                    }

                    boolean flag = bee.position().distanceTo(this.hoverPos) <= ARRIVAL_THRESHOLD;
                    boolean flag1 = true;
                    if (!flag && this.pollinatingTicks > MAX_POLLINATING_TICKS) {
                        moobloom.setStandingStillForBeeTicks(0);
                        moobloom.setBee(null);
                        bee.setSavedFlowerPos(null);
                        BovinesAndButtercups.getHelper().setPollinatingMoobloom(bee, null);
                        this.moobloom = null;
                    } else {
                        if (flag) {
                            boolean flag2 = bee.getRandom().nextInt(POSITION_CHANGE_CHANCE) == 0;
                            if (flag2) {
                                this.hoverPos = new Vec3(vec3.x() + (double)this.getOffset(), vec3.y(), vec3.z() + (double)this.getOffset());
                                bee.getNavigation().stop();
                            } else {
                                flag1 = false;
                            }

                            bee.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
                        }

                        if (flag1) {
                            this.setWantedPos();
                        }

                        ++this.successfulPollinatingTicks;
                        if (bee.getRandom().nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                            this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                            bee.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    private void setWantedPos() {
        bee.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), SPEED_MODIFIER);
    }

    private float getOffset() {
        return (bee.getRandom().nextFloat() * 2.0F - 1.0F) * HOVER_POS_OFFSET;
    }

    private Optional<Moobloom> findMoobloom() {
        Moobloom moobloom = ((ServerLevel)this.bee.level()).getNearestEntity(Moobloom.class, TargetingConditions.forNonCombat().selector((entity, serverLevel) -> entity.getLastHurtByMobTimestamp() <= entity.tickCount - 100 && serverLevel.getBlockState(entity.blockPosition().above(2)).isAir() && !entity.isBaby() && ((Moobloom)entity).bee == null), null, bee.getX(), bee.getY(), bee.getZ(), bee.getBoundingBox().inflate(32.0F, 16.0, 32.0F));
        return Optional.ofNullable(moobloom);
    }
}
