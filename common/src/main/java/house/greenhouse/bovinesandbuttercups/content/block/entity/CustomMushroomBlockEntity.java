package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomMushroom;
import house.greenhouse.bovinesandbuttercups.registry.BovinesBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CustomMushroomBlockEntity extends BlockEntity {
    @Nullable
    private ItemCustomMushroom customMushroom;

    public CustomMushroomBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovinesBlockEntityTypes.CUSTOM_MUSHROOM, worldPosition, blockState);
    }

    @Nullable public ItemCustomMushroom getMushroomType() {
        return customMushroom;
    }

    public void setMushroomType(@Nullable ItemCustomMushroom value) {
        customMushroom = value;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        setMushroomType(new ItemCustomMushroom(CustomMushroomType.CODEC.decode(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("custom_mushroom")).getOrThrow().getFirst()));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("custom_mushroom", CustomMushroomType.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), getMushroomType().holder()).getOrThrow());
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
}
