package house.greenhouse.bovinesandbuttercups.content.entity;

import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class MoobloomFabric extends Moobloom implements Shearable {
    public MoobloomFabric(EntityType<? extends Moobloom> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ConventionalItemTags.SHEAR_TOOLS) && readyForShearing()) {
            shear(SoundSource.PLAYERS);
            gameEvent(GameEvent.SHEAR, player);
            if (!level().isClientSide)
                stack.hurtAndBreak(1, player, getSlotForHand(hand));

            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(SoundSource source) {
        List<ItemStack> stacks = new ArrayList<>();
        level().playSound(null, this, BovinesSoundEvents.MOOBLOOM_SHEAR, source, 1.0f, 1.0f);
        if (!level().isClientSide) {
            ((ServerLevel)level()).sendParticles(ParticleTypes.EXPLOSION, getX(), getY(0.5), getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            discard();
            Cow cowEntity = EntityType.COW.create(level());
            if (cowEntity != null) {
                cowEntity.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
                cowEntity.setHealth(getHealth());
                cowEntity.yBodyRot = yBodyRot;
                if (hasCustomName()) {
                    cowEntity.setCustomName(getCustomName());
                    cowEntity.setCustomNameVisible(isCustomNameVisible());
                }
                if (isPersistenceRequired()) {
                    cowEntity.setPersistenceRequired();
                }
                cowEntity.setInvulnerable(isInvulnerable());
                level().addFreshEntity(cowEntity);
                for (int i = 0; i < 5; ++i) {
                    if (getCowType().value().configuration().flower().blockState().isPresent()) {
                        stacks.add(new ItemStack(getCowType().value().configuration().flower().blockState().get().getBlock()));
                    } else if (getCowType().value().configuration().flower().customType().isPresent()) {
                        ItemStack itemStack = new ItemStack(BovinesItems.CUSTOM_FLOWER);
                        itemStack.set(BovinesDataComponents.CUSTOM_FLOWER, new ItemCustomFlower(getCowType().value().configuration().flower().customType().get()));
                        stacks.add(itemStack);
                    }
                }
            }
        }

        for (ItemStack stack : stacks) {
            ItemEntity item = spawnAtLocation(stack, getBbHeight());
            if (item != null) {
                item.setNoPickUpDelay();
            }
        }
    }

    @Override
    public boolean readyForShearing() {
        return isAlive() && !isBaby() && shouldAllowShearing();
    }
}
