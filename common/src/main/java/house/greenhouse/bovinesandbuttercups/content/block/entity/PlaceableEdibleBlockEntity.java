package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.api.block.PlaceableEdibleType;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemPlaceableEdible;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlaceableEdibleBlockEntity extends BlockEntity {
    @Nullable
    private ItemPlaceableEdible type;

    public PlaceableEdibleBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovinesBlockEntityTypes.POTTED_CUSTOM_FLOWER, worldPosition, blockState);
    }

    public ItemPlaceableEdible getEdibleType() {
        return type;
    }

    public void setEdibleType(ItemPlaceableEdible value) {
        type = value;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("type"))
            setEdibleType(new ItemPlaceableEdible(PlaceableEdibleType.CODEC.decode(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("type")).getOrThrow().getFirst()));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (getEdibleType() != null)
            tag.put("type", PlaceableEdibleType.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), getEdibleType().holder()).getOrThrow());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        components.set(BovinesDataComponents.PLACEABLE_EDIBLE, getEdibleType());
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        setEdibleType(input.get(BovinesDataComponents.PLACEABLE_EDIBLE));
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
