package house.greenhouse.bovinesandbuttercups.content.block.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.content.block.PlaceableEdibleBlock;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdibleType;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class PlaceableEdibleBlockEntity extends BlockEntity implements Nameable {
    private static final Codec<Map<HolderSet<Item>, AttachmentState>> ATTACHMENTS_CODEC = Codec.simpleMap(RegistryCodecs.homogeneousList(Registries.ITEM), AttachmentState.CODEC, Keyable.forStrings(() -> Stream.of("items", "state"))).codec();

    @Nullable
    private ItemEdibleType type;
    private final Map<HolderSet<Item>, AttachmentState> attachments = new LinkedHashMap<>();
    private List<EdibleBlockType.ParticleEntry> particles;

    public PlaceableEdibleBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovinesBlockEntityTypes.PLACEABLE_EDIBLE, worldPosition, blockState);
    }

    public ItemEdibleType getEdibleType() {
        if (type == null) {
            type = new ItemEdibleType(level.registryAccess().lookupOrThrow(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE).getOrThrow(EdibleBlockType.MISSING_KEY));
            resetParticles();
        }
        return type;
    }

    public void setEdibleType(ItemEdibleType value) {
        type = value;
        resetParticles();
    }

    public Map<HolderSet<Item>, AttachmentState> getAttachments() {
        return ImmutableMap.copyOf(attachments);
    }

    public String attachmentsToString() {
        StringBuilder builder = new StringBuilder();
        attachments.forEach((holders, attachmentState) -> {
            if (!builder.isEmpty())
                builder.append(",");
            String key = holders.unwrapKey().map(itemTagKey -> "#" + itemTagKey.location()).orElseGet(() -> String.join(",", holders.stream().map(itemHolder -> itemHolder.unwrapKey().map(k -> k.location().toString()).orElseThrow()).toList()));
            builder.append(key).append("=").append(attachmentState.toString());
        });
        return builder.toString();
    }

    public ItemInteractionResult addAttachmentItem(ItemStack stack, Player player) {
        if (level.isClientSide)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        Optional<Map.Entry<HolderSet<Item>, EdibleBlockType.AttachmentEntry>> items = type.holder().value().attachable().entrySet().stream().filter(holders -> holders.getKey().contains(stack.getItemHolder())).findFirst();
        if (items.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!attachments.isEmpty() && attachments.get(items.get().getKey()).items().size() >= Math.min(items.get().getValue().maxCount(), getBlockState().getValue(PlaceableEdibleBlock.BITES)))
            return ItemInteractionResult.CONSUME;
        attachments.compute(items.get().getKey(), (set, previous) -> {
            ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
            boolean active = false;
            if (previous != null) {
                builder.addAll(previous.items);
                active = previous.active;
            }
            ItemStack stack2 = stack.copy();
            stack2.setCount(1);
            builder.add(stack2);
            return new AttachmentState(builder.build(), active);
        });
        if (items.get().getValue().sound().isPresent() && level.random.nextFloat() < items.get().getValue().sound().get().chance())
            level.playSound(null, getBlockPos(), items.get().getValue().sound().get().sound().value(), SoundSource.BLOCKS, items.get().getValue().sound().get().volume().randomise(level.random), items.get().getValue().sound().get().pitch().randomise(level.random));

        stack.consume(1, player);

        level.setBlock(getBlockPos(), getBlockState().setValue(PlaceableEdibleBlock.LIGHT, getLightValue()), Block.UPDATE_NONE);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);

        return ItemInteractionResult.SUCCESS;
    }

    public ItemInteractionResult removeAttachmentItem(ItemStack stack) {
        if (level.isClientSide)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        List<Map.Entry<HolderSet<Item>, EdibleBlockType.AttachmentEntry>> items = type.holder().value().attachable().entrySet().stream().toList();
        if (items.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        Map.Entry<HolderSet<Item>, EdibleBlockType.AttachmentEntry> item = items.getLast();
        if (!attachments.containsKey(item.getKey()) || attachments.get(item.getKey()).items.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack last = attachments.get(item.getKey()).items().getLast();

        if (item.getValue().activations().stream().noneMatch(activationEntry -> !activationEntry.setTo() && activationEntry.ingredient().test(stack)))
            return ItemInteractionResult.CONSUME;

        if (attachments.get(item.getKey()).items().size() == 1)
            attachments.remove(item.getKey());
        else {
            attachments.computeIfPresent(item.getKey(), (set, previous) -> {
                List<ItemStack> list = new ArrayList<>(previous.items());
                list.removeLast();
                boolean active = previous.active;
                return new AttachmentState(ImmutableList.copyOf(list), active);
            });
        }

        Block.popResource(level, getBlockPos(), last);

        if (item.getValue().sound().isPresent() && level.random.nextFloat() < item.getValue().sound().get().chance())
            level.playSound(null, getBlockPos(), item.getValue().sound().get().sound().value(), SoundSource.BLOCKS, item.getValue().sound().get().volume().randomise(level.random), item.getValue().sound().get().pitch().randomise(level.random));

        level.setBlock(getBlockPos(), getBlockState().setValue(PlaceableEdibleBlock.LIGHT, getLightValue()), Block.UPDATE_NONE);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);

        return ItemInteractionResult.SUCCESS;
    }


    public int getLightValue() {
        return Math.min(getAttachments().values().stream().flatMap(attachmentState -> !attachmentState.active() ? Stream.of(0) : getEdibleType().holder().value().attachable().values().stream().flatMap(attachmentEntry -> attachmentEntry.lightLevel().entrySet().stream().map(lightValue -> {
            if (lightValue.getKey().test(this))
                return lightValue.getValue();
            return 0;
        }))).reduce(0, Integer::sum), 15);
    }

    public ItemInteractionResult activate(ItemStack stack, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        List<Pair<HolderSet<Item>, List<EdibleBlockType.ActivationEntry>>> activations = type.holder().value().attachable().entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue().activations().stream().filter(activationEntry -> activationEntry.ingredient().test(stack)).toList())).toList();
        if (activations.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        LootParams params = new LootParams.Builder((ServerLevel)level)
                .withParameter(LootContextParams.BLOCK_STATE, getBlockState())
                .withParameter(LootContextParams.ORIGIN, hitResult.getLocation())
                .withParameter(LootContextParams.TOOL, stack)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.BLOCK_ENTITY, this)
                .create(LootContextParamSets.BLOCK);
        LootContext context = new LootContext.Builder(params).create(Optional.empty());

        if (attachments.isEmpty() || activations.stream().noneMatch(pair -> pair.getSecond().stream().anyMatch(activationEntry -> (activationEntry.condition().isEmpty() || activationEntry.condition().stream().allMatch(condition -> condition.test(context))) && activationEntry.setTo() != (attachments.containsKey(pair.getFirst()) && attachments.get(pair.getFirst()).active()))))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        activations.forEach(entry ->
                entry.getSecond().forEach(activationEntry -> {
                    if (activationEntry.condition().stream().anyMatch(condition -> !condition.test(context)))
                        return;
                    setAttachmentActive(entry.getFirst(), activationEntry.setTo());
                    if (activationEntry.sound().isPresent() && level.getRandom().nextFloat() < activationEntry.sound().get().chance())
                        level.playSound(null, getBlockPos(), activationEntry.sound().get().sound().value(), SoundSource.BLOCKS, activationEntry.sound().get().volume().randomise(level.random), activationEntry.sound().get().pitch().randomise(level.random));
                    if (!activationEntry.particles().isEmpty()) {
                        activationEntry.particles().forEach((key, value) -> {
                            if (key.test(this))
                                value.forEach(particleEntry -> {
                                    if (level.getRandom().nextFloat() < particleEntry.chance())
                                        level.addParticle(particleEntry.particle(), particleEntry.position().x, particleEntry.position().y, particleEntry.position().z, particleEntry.speed().x, particleEntry.speed().y, particleEntry.speed().z);
                                });
                        });
                    }
                })
        );

        if (stack.isDamageableItem())
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        else
            player.getItemInHand(hand).shrink(1);

        level.setBlock(getBlockPos(), getBlockState().setValue(PlaceableEdibleBlock.LIGHT, getLightValue()), Block.UPDATE_NONE);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);

        return ItemInteractionResult.SUCCESS;
    }

    public void setAttachmentActive(HolderSet<Item> items, boolean value) {
        attachments.computeIfPresent(items, (set, previous) -> new AttachmentState(previous.items, value));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("type"))
            setEdibleType(new ItemEdibleType(EdibleBlockType.CODEC.decode(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("type")).getOrThrow().getFirst()));

        attachments.clear();
        if (tag.contains("attachments"))
            attachments.putAll(ATTACHMENTS_CODEC.decode(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("attachments")).getOrThrow().getFirst());
        resetParticles();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (getEdibleType() != null)
            tag.put("type", EdibleBlockType.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), getEdibleType().holder()).getOrThrow());

        if (!attachments.isEmpty())
            tag.put("attachments", ATTACHMENTS_CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), attachments).getOrThrow());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        components.set(BovinesDataComponents.EDIBLE_TYPE, getEdibleType());
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        setEdibleType(input.get(BovinesDataComponents.EDIBLE_TYPE));
    }


    public List<EdibleBlockType.ParticleEntry> getParticles() {
        if (particles != null || type == null || !type.holder().isBound())
            return particles;
        ImmutableList.Builder<EdibleBlockType.ParticleEntry> builder = ImmutableList.builder();
        builder.addAll(closestBlockValues());
        particles = builder.build();
        return particles;
    }

    private List<EdibleBlockType.ParticleEntry> closestBlockValues() {
        EdibleBlockType type = this.type.holder().value();

        return type.particlePositions().entrySet().stream()
                .filter(entry -> (entry.getKey().biteCount().isEmpty() || entry.getKey().biteCount().get().test(getBlockState().getValue(PlaceableEdibleBlock.BITES))) && entry.getKey().attachments().entrySet().stream().allMatch(e -> e.getValue().count().test(!attachments.containsKey(e.getKey()) ? 0 : attachments.get(e.getKey()).items().size()) && (e.getValue().active().isEmpty() || e.getValue().active().get() == (attachments.containsKey(e.getKey()) && attachments.get(e.getKey()).active)))
        ).flatMap(entry -> entry.getValue().stream()).toList();
    }

    private void resetParticles() {
        particles = null;
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
        if (type.holder().isBound())
            return BlockUtil.getOrCreateBlockNameTranslationKey(type.holder().unwrapKey().orElseThrow().location());
        return BlockUtil.getOrCreateBlockNameTranslationKey(BovinesAndButtercups.asResource("missing_edible"));
    }

    public record AttachmentState(List<ItemStack> items, boolean active) {
        public static final Codec<AttachmentState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ItemStack.STRICT_SINGLE_ITEM_CODEC.listOf(1, 16).fieldOf("items").forGetter(AttachmentState::items),
                Codec.BOOL.fieldOf("active").forGetter(AttachmentState::active)
        ).apply(inst, AttachmentState::new));

        @Override
        public String toString() {
            return String.join(",", items.stream().map(stack -> stack.getItemHolder().unwrapKey().get().location() + "=" + stack.getCount()).toList()) + ",active." + active;
        }
    }

    public record EdibleBlockEntityValues(ItemEdibleType placeableEdible, int bites, Map<HolderSet<Item>, PlaceableEdibleBlockEntity.AttachmentState> state) {
        public static EdibleBlockEntityValues fromBlockEntity(PlaceableEdibleBlockEntity blockEntity) {
            return new EdibleBlockEntityValues(Optional.of(blockEntity.getEdibleType()).orElseGet(() -> new ItemEdibleType(blockEntity.level.registryAccess().registryOrThrow(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE).getHolderOrThrow(EdibleBlockType.MISSING_KEY))), blockEntity.getBlockState().getValue(PlaceableEdibleBlock.BITES), blockEntity.getAttachments());
        }
    }
}
