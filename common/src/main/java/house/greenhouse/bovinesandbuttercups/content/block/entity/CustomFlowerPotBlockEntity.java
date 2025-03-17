package house.greenhouse.bovinesandbuttercups.content.block.entity;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
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

public class CustomFlowerPotBlockEntity extends BlockEntity implements Nameable {
    @Nullable
    private ItemCustomFlower customFlower;

    public CustomFlowerPotBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovinesBlockEntityTypes.POTTED_CUSTOM_FLOWER, worldPosition, blockState);
    }

    @Nullable
    public ItemCustomFlower getFlowerType() {
        return customFlower;
    }

    public void setFlowerType(@Nullable ItemCustomFlower value) {
        customFlower = value;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("custom_flower"))
            setFlowerType(new ItemCustomFlower(CustomFlowerType.CODEC.decode(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("custom_flower")).getOrThrow().getFirst()));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (getFlowerType() != null)
            tag.put("custom_flower", CustomFlowerType.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), getFlowerType().holder()).getOrThrow());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        components.set(BovinesDataComponents.CUSTOM_FLOWER, getFlowerType());
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput input) {
        setFlowerType(input.get(BovinesDataComponents.CUSTOM_FLOWER));
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
        return BlockUtil.getOrCreateBlockNameTranslationKey(BovinesAndButtercups.asResource("potted_missing_flower"));
    }

    @Override
    public Component getCustomName() {
        if (customFlower.holder().isBound())
            return BlockUtil.getOrCreateBlockNameTranslationKey(customFlower.holder().unwrapKey().orElseThrow().location().withPath(s -> "potted_" + s));
        return null;
    }
}
