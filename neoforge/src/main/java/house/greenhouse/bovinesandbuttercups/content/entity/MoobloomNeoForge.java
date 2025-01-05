package house.greenhouse.bovinesandbuttercups.content.entity;

import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.IShearable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MoobloomNeoForge extends Moobloom implements IShearable {
    public MoobloomNeoForge(EntityType<? extends Moobloom> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        List<ItemStack> stacks = new ArrayList<>();
        level().playSound(null, this, BovinesSoundEvents.MOOBLOOM_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0f, 1.0f);
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
                    if (getCowVariant().value().configuration().flower().blockState().isPresent()) {
                        stacks.add(new ItemStack(getCowVariant().value().configuration().flower().blockState().get().getBlock()));
                    } else if (getCowVariant().value().configuration().flower().customType().isPresent()) {
                        ItemStack itemStack = new ItemStack(BovinesItems.CUSTOM_FLOWER);
                        itemStack.set(BovinesDataComponents.CUSTOM_FLOWER, new ItemCustomFlower(getCowVariant().value().configuration().flower().customType().get()));
                        stacks.add(itemStack);
                    }
                }
            }
        }
        return stacks;
    }

    @Override
    public void spawnShearedDrop(Level level, BlockPos pos, ItemStack drop) {
        ItemEntity itemEntity = this.spawnAtLocation(drop, this.getBbHeight());
        if (itemEntity != null) {
            itemEntity.setNoPickUpDelay();
        }
    }

    @Override
    public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        return isAlive() && !isBaby() && shouldAllowShearing();
    }
}
