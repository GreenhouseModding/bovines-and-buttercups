package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomMushroom;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CustomMushroomPotBlockEntity extends BlockEntity implements Nameable {
    @Nullable
    private ItemCustomMushroom customMushroom;

    public CustomMushroomPotBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovinesBlockEntityTypes.POTTED_CUSTOM_MUSHROOM, worldPosition, blockState);
    }

    @Nullable public ItemCustomMushroom getMushroomType() {
        return customMushroom;
    }

    public void setMushroomType(@Nullable ItemCustomMushroom value) {
        if (value != null && !value.holder().value().hasPotted()) {
            BovinesAndButtercups.LOG.warn("Failed to set custom mushroom to block at {{},{},{}}. Custom mushroom variant \"{}\" does not support potted blocks.", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), value.holder().getRegisteredName());
            return;
        }
        customMushroom = value;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("custom_mushroom"))
            setMushroomType(new ItemCustomMushroom(CustomMushroomType.CODEC.decode(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("custom_mushroom")).getOrThrow().getFirst()));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (getMushroomType() != null)
            tag.put("custom_mushroom", CustomMushroomType.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), getMushroomType().holder()).getOrThrow());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        components.set(BovinesDataComponents.CUSTOM_MUSHROOM, getMushroomType());
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput input) {
        setMushroomType(input.get(BovinesDataComponents.CUSTOM_MUSHROOM));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Override
    public Component getName() {
        if (customMushroom.holder().isBound())
            return BlockUtil.getOrCreateBlockNameTranslationKey(customMushroom.holder().unwrapKey().orElseThrow().location().withPath(s -> "potted_" + s));
        return BlockUtil.getOrCreateBlockNameTranslationKey(BovinesAndButtercups.asResource("potted_missing_mushroom"));
    }
}
