package house.greenhouse.bovinesandbuttercups.content.entity;

import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.IShearable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoobloomNeoForge extends Moobloom implements IShearable {
    public MoobloomNeoForge(EntityType<? extends Moobloom> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack shears, Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            level().playSound(null, this, BovinesSoundEvents.MOOBLOOM_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0f, 1.0f);
            convertTo(EntityType.COW, ConversionParams.single(this, false, false), (cow) -> {
                ((ServerLevel) level()).sendParticles(ParticleTypes.EXPLOSION, getX(), getY(0.5), getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                if (getCowType().value().configuration().lootTable().isEmpty())
                    return;
                dropFromShearingLootTable((ServerLevel) level, ResourceKey.create(Registries.LOOT_TABLE, getCowType().value().configuration().lootTable().orElseThrow()), shears, (p_390218_, p_390219_) -> {
                    for (int i = 0; i < p_390219_.getCount(); i++) {
                        p_390218_.addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(1.0), this.getZ(), p_390219_.copyWithCount(1)));
                    }
                });
            });
        }
        return List.of();
    }

    @Override
    public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        return isAlive() && !isBaby() && shouldAllowShearing();
    }
}
