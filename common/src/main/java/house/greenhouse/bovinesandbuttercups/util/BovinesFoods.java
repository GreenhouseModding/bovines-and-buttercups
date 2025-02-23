package house.greenhouse.bovinesandbuttercups.util;

import house.greenhouse.bovinesandbuttercups.access.MobEffectInstanceLockdownDataAccess;
import house.greenhouse.bovinesandbuttercups.content.effect.BovinesEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;

import java.util.List;

public class BovinesFoods {
    public static class Properties {
        public static final FoodProperties RICH_HONEY_BOTTLE = new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(0.3F)
                .alwaysEdible()
                .build();
    }

    public static class Consumables {
        public static final Consumable ALSTROEMERIA_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 7200))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.SLOW_FALLING)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable BUTTERCUP_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 2400))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.POISON)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable CAMELLIA_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 2400))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.HUNGER)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable CHARGELILY_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 1200))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.DIG_SPEED)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable FREESIA_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 9600))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.WATER_BREATHING)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable HYACINTH_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 2400))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.WITHER)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable LIMELIGHT_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 2400))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.REGENERATION)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable LINGHOLM_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 2400))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.MOVEMENT_SPEED)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable NIGHTSHADE_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 2400))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.INVISIBILITY)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable PINK_DAISY_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 2400))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.DAMAGE_BOOST)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable SNOWDROP_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 7200))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.DIG_SLOWDOWN)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable SOMBERCUP_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 2400))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.DARKNESS)))))
                .hasConsumeParticles(false)
                .build();
        public static final Consumable TROPICAL_BLUE_NECTAR = Consumable.builder()
                .consumeSeconds(1.6F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .onConsume(new ApplyStatusEffectsConsumeEffect(((MobEffectInstanceLockdownDataAccess)new MobEffectInstance(BovinesEffects.LOCKDOWN, 9600))
                        .bovinesandbuttercups$setLockdownData(List.of(new LockdownData(MobEffects.FIRE_RESISTANCE)))))
                .hasConsumeParticles(false)
                .build();

        public static final Consumable RICH_HONEY_BOTTLE = Consumable.builder()
                .consumeSeconds(2.0F)
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.HONEY_DRINK)
                .onConsume(new RemoveStatusEffectsConsumeEffect(MobEffects.POISON))
                .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.ABSORPTION, 800, 0)))
                .hasConsumeParticles(false)
                .build();


        static {

        }
    }
}