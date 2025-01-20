package house.greenhouse.bovinesandbuttercups.content.entity;

import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class MoobloomFabric extends Moobloom implements Shearable {
    public MoobloomFabric(EntityType<? extends Moobloom> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ConventionalItemTags.SHEAR_TOOLS) && readyForShearing()) {
            gameEvent(GameEvent.SHEAR, player);
            if (!level().isClientSide) {
                shear((ServerLevel)level(), SoundSource.PLAYERS, stack);
                stack.hurtAndBreak(1, player, getSlotForHand(hand));
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(ServerLevel level, SoundSource soundSource, ItemStack shears) {
        level().playSound(null, this, BovinesSoundEvents.MOOBLOOM_SHEAR, soundSource, 1.0f, 1.0f);
        convertTo(EntityType.COW, ConversionParams.single(this, false, false), (cow) -> {
            ((ServerLevel) level()).sendParticles(ParticleTypes.EXPLOSION, getX(), getY(0.5), getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            if (getCowVariant().value().configuration().lootTable().isEmpty())
                return;
            dropFromShearingLootTable(level, ResourceKey.create(Registries.LOOT_TABLE, getCowVariant().value().configuration().lootTable().orElseThrow()), shears, (p_390218_, p_390219_) -> {
                for (int i = 0; i < p_390219_.getCount(); i++) {
                    p_390218_.addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(1.0), this.getZ(), p_390219_.copyWithCount(1)));
                }
            });
        });
    }

    @Override
    public boolean readyForShearing() {
        return isAlive() && !isBaby() && shouldAllowShearing();
    }
}
