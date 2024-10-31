package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.content.block.CustomHugeMushroomBlock;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomMushroom;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomHugeMushroomBlockEntity extends BlockEntity {
    @Nullable
    private ItemCustomMushroom customMushroom;

    public CustomHugeMushroomBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovinesBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK, worldPosition, blockState);
    }

    @Nullable
    public ItemCustomMushroom getMushroomType() {
        return customMushroom;
    }

    public void setMushroomType(@Nullable ItemCustomMushroom value) {
        customMushroom = value;
        updateState();
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

    public void updateState() {
        Level level = this.getLevel();
        BlockPos pos = this.getBlockPos();
        BlockState newState = this.getBlockState();

        if (level.getBlockState(pos.above()).is(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK) && level.getBlockEntity(pos.above()) instanceof CustomHugeMushroomBlockEntity hugeMushroomBlock && Objects.equals(hugeMushroomBlock.getMushroomType(), this.getMushroomType()))
            newState = newState.setValue(CustomHugeMushroomBlock.UP, Boolean.FALSE);

        if (level.getBlockState(pos.below()).is(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK) && level.getBlockEntity(pos.below()) instanceof CustomHugeMushroomBlockEntity hugeMushroomBlock && Objects.equals(hugeMushroomBlock.getMushroomType(), this.getMushroomType()))
            newState = newState.setValue(CustomHugeMushroomBlock.DOWN, Boolean.FALSE);

        if (level.getBlockState(pos.west()).is(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK) && level.getBlockEntity(pos.west()) instanceof CustomHugeMushroomBlockEntity hugeMushroomBlock && Objects.equals(hugeMushroomBlock.getMushroomType(), this.getMushroomType()))
            newState = newState.setValue(CustomHugeMushroomBlock.WEST, Boolean.FALSE);

        if (level.getBlockState(pos.north()).is(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK) && level.getBlockEntity(pos.north()) instanceof CustomHugeMushroomBlockEntity hugeMushroomBlock && Objects.equals(hugeMushroomBlock.getMushroomType(), this.getMushroomType()))
            newState = newState.setValue(CustomHugeMushroomBlock.NORTH, Boolean.FALSE);

        if (level.getBlockState(pos.east()).is(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK) && level.getBlockEntity(pos.east()) instanceof CustomHugeMushroomBlockEntity hugeMushroomBlock && Objects.equals(hugeMushroomBlock.getMushroomType(), this.getMushroomType()))
            newState = newState.setValue(CustomHugeMushroomBlock.EAST, Boolean.FALSE);

        if (level.getBlockState(pos.south()).is(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK) && level.getBlockEntity(pos.south()) instanceof CustomHugeMushroomBlockEntity hugeMushroomBlock && Objects.equals(hugeMushroomBlock.getMushroomType(), this.getMushroomType()))
            newState = newState.setValue(CustomHugeMushroomBlock.SOUTH, Boolean.FALSE);

        level.setBlock(pos, newState, 3);
    }
}
