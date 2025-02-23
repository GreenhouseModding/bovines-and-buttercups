package house.greenhouse.bovinesandbuttercups.content.entity;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowVariantAttachment;
import house.greenhouse.bovinesandbuttercups.api.variant.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.BreedCowWithVariantTrigger;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.mixin.EntityAccessor;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncMoobloomSnowLayerClientboundPacket;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowVariants;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParamSets;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParams;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.util.SnowLayerUtil;
import house.greenhouse.bovinesandbuttercups.util.dfu.BovinesDataFixer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Moobloom extends Cow {
    private static final EntityDataAccessor<Integer> FLOWER_SPREAD_ATTEMPTS = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> POLLINATED_RESET_TICKS = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STANDING_STILL_FOR_BEE_TICKS = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ALLOW_SHEARING = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ALLOW_CONVERSION = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_SNOW = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SNOW_LAYER_PERSISTENT = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.BOOLEAN);

    public static final int TICKS_UNTIL_SPREAD = 80;

    @Nullable
    public Bee bee;
    private boolean hasRefreshedDimensionsForLaying;
    @Nullable
    private BlockPos previousPos = null;
    private int ticksUntilSpread = -1;
    @Nullable private UUID lastLightningBoltUUID;
    private final Map<Holder<CowVariant<?>>, List<Vec3>> particlePositions = new HashMap<>();

    public final AnimationState layDownAnimationState = new AnimationState();
    public final AnimationState getUpAnimationState = new AnimationState();

    public Moobloom(EntityType<? extends Moobloom> entityType, Level level) {
        super(entityType, level);
        bee = null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLOWER_SPREAD_ATTEMPTS, 0);
        builder.define(POLLINATED_RESET_TICKS, 0);
        builder.define(STANDING_STILL_FOR_BEE_TICKS, 0);
        builder.define(ALLOW_SHEARING, true);
        builder.define(ALLOW_CONVERSION, true);
        builder.define(HAS_SNOW, false);
        builder.define(SNOW_LAYER_PERSISTENT, false);
    }

    @Override
    public void registerGoals() {
        goalSelector.addGoal(2, new Moobloom.LookAtBeeGoal());
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Moobloom.class, 2.0F, 1.0F, 1.0F, moobloomEntity -> moobloomEntity instanceof Moobloom && ((Moobloom) moobloomEntity).getStandingStillForBeeTicks() > 0));
        super.registerGoals();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("flower_spread_attempts", getFlowerSpreadAttempts());
        if (previousPos != null)
            tag.put("previous_flower_pos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, previousPos).getOrThrow());
        tag.putInt("ticks_until_spread", ticksUntilSpread);
        tag.putInt("pollinated_reset_ticks", getPollinatedResetTicks());
        tag.putBoolean("allow_shearing", shouldAllowShearing());
        tag.putBoolean("allow_conversion", shouldAllowConversion());
        tag.putBoolean("has_snow", hasSnow());
        if (isSnowLayerPersistent())
            tag.putBoolean("snow_layer_persistent", true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        backwardsCompat(tag);
        if (tag.contains("variant")) {
            var variantDataResult = CowVariantAttachment.CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, level().registryAccess()), tag.get("variant"));
            if (!variantDataResult.hasResultOrPartial())
                BovinesAndButtercups.LOG.error(variantDataResult.error().get().message());
            else
                BovinesAndButtercups.getHelper().setCowVariantAttachment(this, variantDataResult.getOrThrow().getFirst());
        }
        if (tag.contains("flower_spread_attempts", Tag.TAG_INT))
            setFlowerSpreadAttempts(tag.getInt("flower_spread_attempts"));
        if (tag.contains("previous_flower_pos", Tag.TAG_INT_ARRAY))
            previousPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, tag.get("previous_flower_pos")).getOrThrow().getFirst();
        if (tag.contains("ticks_until_spread", Tag.TAG_INT))
            ticksUntilSpread = tag.getInt("ticks_until_spread");
        if (tag.contains("pollinated_reset_ticks", Tag.TAG_INT))
            setPollinatedResetTicks(tag.getInt("pollinated_reset_ticks"));
        if (tag.contains("allow_shearing", Tag.TAG_BYTE))
            setAllowShearing(tag.getBoolean("allow_shearing"));
        if (tag.contains("allow_conversion", Tag.TAG_BYTE))
            setAllowConversion(tag.getBoolean("allow_conversion"));
        if (tag.contains("has_snow", Tag.TAG_BYTE))
            setSnow(tag.getBoolean("has_snow"));
        if (tag.contains("snow_layer_persistent", Tag.TAG_BYTE))
            setPersistentSnowLayer(tag.getBoolean("snow_layer_persistent"));
    }

    public void backwardsCompat(CompoundTag tag) {
        if (tag.contains("Type", Tag.TAG_STRING)) {
            Optional<Holder.Reference<CowVariant<?>>> cowVariant = level().registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).get(ResourceLocation.parse(tag.getString("Type")));
            if (cowVariant.isEmpty()) {
                BovinesAndButtercups.LOG.error("Could not deserialize legacy cow type tag \"{}\" into a cow type holder.", tag.getString("Type"));
                return;
            }
            if (!cowVariant.get().isBound() || cowVariant.get().value().type() != BovinesCowTypes.MOOBLOOM_TYPE)  {
                BovinesAndButtercups.LOG.error("Cow Variant \"{}\" is not bound or is not a moobloom.", cowVariant);
                return;
            }
            if (tag.contains("PreviousType", Tag.TAG_STRING)) {
                Optional<Holder.Reference<CowVariant<?>>> previousCowType = level().registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).get(ResourceLocation.parse(tag.getString("Type")));
                if (previousCowType.isEmpty()) {
                    BovinesAndButtercups.LOG.error("Could not deserialize legacy cow type tag \"{}\" into a cow type holder.", tag.getString("Type"));
                    return;
                }
                if (!previousCowType.get().isBound() || previousCowType.get().value().type() != BovinesCowTypes.MOOBLOOM_TYPE) {
                    BovinesAndButtercups.LOG.error("Previous Cow Variant \"{}\" is not bound or is not a moobloom.", cowVariant);
                    return;
                }
                BovinesAndButtercups.getHelper().setCowVariantAttachment(this, new CowVariantAttachment(cowVariant.get(), previousCowType.map(cowVariantReference -> cowVariantReference)));
                CowVariantAttachment.sync(this);
            } else {
                setCowVariant((Holder) cowVariant.get());
                CowVariantAttachment.sync(this);
            }
        }
        if (tag.contains("PollinatedResetTicks", Tag.TAG_INT))
            setPollinatedResetTicks(tag.getInt("PollinatedResetTicks"));
        if (tag.contains("AllowShearing", Tag.TAG_BYTE))
            setAllowShearing(tag.getBoolean("AllowShearing"));
    }

    public void setBee(@Nullable Bee value) {
        bee = value;
    }

    public static boolean canMoobloomSpawn(EntityType<? extends Moobloom> type, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(level, pos) && getTotalSpawnWeight(level, pos) > 0;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt bolt) {
        UUID uuid = bolt.getUUID();
        if (getCowVariant().isBound() && getCowVariant().value().configuration().allowsConversion(this) && !uuid.equals(lastLightningBoltUUID)) {
            if (getPreviousCowVariant() == null) {
                if (getCowVariant().value().configuration().settings().thunderConverts().isEmpty()) {
                    super.thunderHit(level, bolt);
                    return;
                }

                List<WeightedEntry.Wrapper<Holder<CowVariant<MoobloomConfiguration>>>> compatibleList = getCowVariant().value().configuration().settings().filterThunderConverts(BovinesCowTypes.MOOBLOOM_TYPE);

                if (compatibleList.isEmpty()) {
                    super.thunderHit(level, bolt);
                    return;
                } else if (compatibleList.size() == 1) {
                    setCurrentWithPreviousCowType(compatibleList.getFirst().data());
                    CowVariantAttachment.sync(this);
                } else {
                    int totalWeight = level.getRandom().nextInt(compatibleList.stream().map(holderWrapper -> holderWrapper.weight().asInt()).reduce(Integer::sum).orElse(0));
                    for (WeightedEntry.Wrapper<Holder<CowVariant<MoobloomConfiguration>>> cct : compatibleList) {
                        totalWeight -= cct.weight().asInt();
                        if (totalWeight < 0) {
                            setCurrentWithPreviousCowType(cct.data());
                            CowVariantAttachment.sync(this);
                            break;
                        }
                    }
                }
            } else {
                setCowVariant(getPreviousCowVariant());
                CowVariantAttachment.sync(this);
            }
            lastLightningBoltUUID = uuid;
            playSound(BovinesSoundEvents.MOOBLOOM_CONVERT, 2.0F, 1.0F);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (isInvulnerableTo(level, source)) {
            return false;
        }
        if (bee != null) {
            setStandingStillForBeeTicks(0);
            bee = null;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            if (getStandingStillForBeeTicks() > 0)
                layDownAnimationState.startIfStopped(tickCount);
            else if (layDownAnimationState.isStarted() && getStandingStillForBeeTicks() == 0) {
                layDownAnimationState.stop();
                getUpAnimationState.startIfStopped(tickCount);
            }

            if (getUpAnimationState.isStarted() && getUpAnimationState.getTimeInMillis(level().getGameTime()) >= 1000)
                getUpAnimationState.stop();
        } else {
            if (ticksUntilSpread > 0)
                --ticksUntilSpread;

            if ((previousPos == null || blockPosition().distSqr(previousPos) > 4 || ticksUntilSpread == 0) && getFlowerSpreadAttempts() > 0) {
                if (spreadFlowers()) {
                    level().playSound(null, blockPosition(), BovinesSoundEvents.MOOBLOOM_PLANT, getSoundSource(), 1.0F, (random.nextFloat() * 0.2F) + 0.9F);
                    gameEvent(GameEvent.ENTITY_ACTION);
                    setFlowerSpreadAttempts(getFlowerSpreadAttempts() - 1);
                    ticksUntilSpread = TICKS_UNTIL_SPREAD;
                    previousPos = blockPosition();
                }
                if (getFlowerSpreadAttempts() <= 0) {
                    previousPos = null;
                    ticksUntilSpread = -1;
                }
            }

            if (bee != null && !bee.isAlive()) {
                setStandingStillForBeeTicks(0);
                bee = null;
            }
            if (getStandingStillForBeeTicks() > 0 && !level().isClientSide())
                setStandingStillForBeeTicks(getStandingStillForBeeTicks() - 1);

            if (!hasSnow() && isInSnowyWeather() && !isSnowLayerPersistent() && !level().isClientSide() && random.nextFloat() < 0.4F) {
                setSnow(true);
                BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(this, new SyncMoobloomSnowLayerClientboundPacket(getId(), true));
            }
            if (hasSnow() && !isSnowLayerPersistent() && level().getBiome(blockPosition()).is(BiomeTags.SNOW_GOLEM_MELTS) && random.nextFloat() < 0.4F) {
                setSnow(false);
                BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(this, new SyncMoobloomSnowLayerClientboundPacket(getId(), false));
            }

            if (getPollinatedResetTicks() > 0)
                setPollinatedResetTicks(getPollinatedResetTicks() - 1);
        }


        if (getStandingStillForBeeTicks() > 0) {
            if (!hasRefreshedDimensionsForLaying) {
                refreshDimensions();
                ((EntityAccessor)this).bovinesandbuttercups$setEyeHeight(getDimensions(getPose()).height() * 0.85F);
                hasRefreshedDimensionsForLaying = true;
            }
            if (!level().isClientSide() && bee != null)
                getLookControl().setLookAt(bee);
        } else if (hasRefreshedDimensionsForLaying) {
            refreshDimensions();
            ((EntityAccessor)this).bovinesandbuttercups$setEyeHeight(getDimensions(getPose()).height() * 0.85F);
            hasRefreshedDimensionsForLaying = false;
        }
    }

    public boolean isInSnowyWeather() {
        BlockPos pos = blockPosition();
        if (isSnowingAt(pos))
            return true;
        pos =  BlockPos.containing(blockPosition().getX(), getBoundingBox().maxY, blockPosition().getZ());
        return isSnowingAt(pos);
    }

    private boolean isSnowingAt(BlockPos pos) {
        if (!level().isRaining()) {
            return false;
        } else if (!level().canSeeSky(pos)) {
            return false;
        } else if (level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return false;
        } else {
            Biome biome = level().getBiome(pos).value();
            return biome.getPrecipitationAt(pos, level().getSeaLevel()) == Biome.Precipitation.SNOW;
        }
    }

    public boolean spreadFlowers() {
        if (level().isClientSide) return true;

        BlockState state = null;
        if (getCowVariant().value().configuration().flower().blockState().isPresent())
            state = getCowVariant().value().configuration().flower().blockState().get().getBlock().defaultBlockState();
        else if (getCowVariant().value().configuration().flower().customType().isPresent())
            state = BovinesBlocks.CUSTOM_FLOWER.defaultBlockState();

        if (state == null) {
            BovinesAndButtercups.LOG.warn("Moobloom with type '{}' tried to spread flowers without a valid flower type.", getCowVariant().getRegisteredName());
            return true;
        }

        int maxTries = 5;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        boolean hasSetAFlower = false;

        for(int i = 0; i < maxTries; ++i) {
            if (random.nextFloat() > 0.75F)
                continue;
            pos.setWithOffset(blockPosition(), random.nextIntBetweenInclusive(-1, 1), 0, random.nextIntBetweenInclusive(-1, 1));
            if (!level().getBlockState(pos).isAir() || !state.canSurvive(level(), pos))
                pos.offset(0, 1, 0);
            if (state.canSurvive(level(), pos) && level().getBlockState(pos).isAir()) {
                setBlockToFlower(state, pos.immutable());
                hasSetAFlower = true;
            }
        }
        gameEvent(GameEvent.BLOCK_PLACE, this);
        return hasSetAFlower;
    }

    public void setBlockToFlower(BlockState state, BlockPos pos) {
        if (level().isClientSide || !state.canSurvive(level(), pos.below())) return;
        ((ServerLevel) level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5D, pos.getY() + 0.3D, pos.getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
        if (state.getBlock() == BovinesBlocks.CUSTOM_FLOWER && getCowVariant().value().configuration().flower().customType().isPresent()) {
            level().setBlock(pos, state, Block.UPDATE_ALL);
            BlockEntity blockEntity = level().getBlockEntity(pos);
            if (blockEntity instanceof CustomFlowerBlockEntity customFlowerBlockEntity) {
                customFlowerBlockEntity.setFlowerType(new ItemCustomFlower(getCowVariant().value().configuration().flower().customType().get()));
                customFlowerBlockEntity.setChanged();
            }
        } else {
            level().setBlock(pos, state, Block.UPDATE_ALL);
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (getStandingStillForBeeTicks() > 0) {
            return super.getDefaultDimensions(pose).scale(1.0F, 0.7F);
        }
        return super.getDefaultDimensions(pose);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!isBaby()) {
            if (stack.is(Items.BONE_MEAL) && feed()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            } else if (stack.is(Items.BOWL)) {
                ItemStack stack2;
                if (getCowVariant().value().configuration().nectar().isPresent()) {
                    stack2= getCowVariant().value().configuration().nectar().get();
                } else {
                    return InteractionResult.PASS;
                }

                ItemStack stack3 = ItemUtils.createFilledResult(stack, player, stack2, false);
                player.setItemInHand(hand, stack3);
                playSound(BovinesSoundEvents.MOOBLOOM_MILK, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }
        }
        InteractionResult result = SnowLayerUtil.removeSnowIfShovel(this, player, hand, stack);
        if (result != InteractionResult.PASS)
            return result;
        return super.mobInteract(player, hand);
    }

    public boolean feed() {
        if (getFlowerSpreadAttempts() >= 8)
            return false;
        if (!level().isClientSide) {
            ((ServerLevel) level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, position().x(), position().y() + 1.6D, position().z(), 8, 0.5, 0.1, 0.4, 0.0);
            previousPos = blockPosition();
            ticksUntilSpread = TICKS_UNTIL_SPREAD;
            setFlowerSpreadAttempts(getFlowerSpreadAttempts() + 8);
        }
        playSound(BovinesSoundEvents.MOOBLOOM_EAT, 1.0f, (random.nextFloat() * 0.4F) + 0.8F);
        return true;
    }

    public Pair<Holder<CowVariant<MoobloomConfiguration>>, Optional<Holder<CowVariant<MoobloomConfiguration>>>> chooseBabyType(ServerLevel level, Moobloom otherParent, Moobloom child) {
        List<Holder<CowVariant<MoobloomConfiguration>>> eligibleCowTypes = new ArrayList<>();

        for (Holder.Reference<CowVariant<?>> cowVariant : level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).registryKeySet().stream().map(key -> level().registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).getOrThrow(key)).filter(type -> {
            return type.isBound() && type.value().type() == BovinesCowTypes.MOOBLOOM_TYPE && ((MoobloomConfiguration)type.value().configuration()).offspringConditions() != OffspringConditions.EMPTY;
        }).toList()) {
            Holder.Reference<CowVariant<MoobloomConfiguration>> moobloomType = (Holder.Reference) cowVariant;
            var conditions = moobloomType.value().configuration().offspringConditions();

            LootParams.Builder params = new LootParams.Builder(level);
            params.withParameter(LootContextParams.THIS_ENTITY, this);
            params.withParameter(BovinesLootContextParams.PARTNER, otherParent);
            params.withParameter(BovinesLootContextParams.CHILD, child);
            params.withParameter(LootContextParams.ORIGIN, position());
            params.withParameter(BovinesLootContextParams.BREEDING_TYPE, cowVariant);

            LootContext thisContext = new LootContext.Builder(params.create(BovinesLootContextParamSets.BREEDING)).create(Optional.empty());

            params.withParameter(LootContextParams.THIS_ENTITY, otherParent);
            params.withParameter(BovinesLootContextParams.PARTNER, this);
            LootContext otherContext = new LootContext.Builder(params.create(BovinesLootContextParamSets.BREEDING)).create(Optional.empty());

            if ((conditions.thisConditions().stream().allMatch(condition -> condition.test(thisContext))
                    && conditions.otherConditions().stream().allMatch(condition -> condition.test(otherContext)))
                    || (conditions.thisConditions().stream().allMatch(condition -> condition.test(otherContext))
                    && conditions.otherConditions().stream().allMatch(condition -> condition.test(thisContext))))
                eligibleCowTypes.add(moobloomType);
        }

        if (!eligibleCowTypes.isEmpty()) {
            int random = getRandom().nextInt(eligibleCowTypes.size());
            var randomType = eligibleCowTypes.get(random);

            child.createParticles(randomType, position());

            if (getLoveCause() != null)
                BreedCowWithVariantTrigger.INSTANCE.trigger(getLoveCause(), this, otherParent, child, true, (Holder) randomType);
            return randomType.value().configuration().offspringConditions().inheritance().handleInheritance(randomType, BovinesAndButtercups.getHelper().getCowVariantAttachment(this), BovinesAndButtercups.getHelper().getCowVariantAttachment(otherParent));
        }

        child.particlePositions.clear();

        if (!otherParent.getCowVariant().equals(getCowVariant()) && getRandom().nextBoolean()) {
            if (getLoveCause() != null)
                BreedCowWithVariantTrigger.INSTANCE.trigger(getLoveCause(), this, otherParent, child, false, (Holder<CowVariant<?>>)(Holder<?>)otherParent.getCowVariant());
            return Pair.of(otherParent.getCowVariant(), Optional.ofNullable(otherParent.getPreviousCowVariant()));
        }

        if (getLoveCause() != null)
            BreedCowWithVariantTrigger.INSTANCE.trigger(getLoveCause(), this, otherParent, child, false, (Holder<CowVariant<?>>)(Holder<?>) getCowVariant());
        return Pair.of(getCowVariant(), Optional.ofNullable(getPreviousCowVariant()));
    }

    public void addParticlePosition(Holder<CowVariant<?>> type, Vec3 pos) {
        particlePositions.computeIfAbsent(type, holder -> new ArrayList<>()).add(pos);
    }

    public void createParticles(Holder<CowVariant<MoobloomConfiguration>> type, Vec3 parentPos) {
        if (!type.isBound() || type.value().configuration().settings().particle().isEmpty())
            return;

        if (particlePositions.isEmpty() && !level().isClientSide())
            ((ServerLevel)level()).sendParticles(type.value().configuration().settings().particle().get(), getX(), getY(0.5), getZ(), 6, 0.05, 0.05, 0.05, 0.01);
        else
            for (Vec3 pos : particlePositions.get(type))
                createParticleTrail(pos, parentPos, type.value().configuration().settings().particle().get());

        particlePositions.clear();
    }

    public void createParticleTrail(Vec3 pos, Vec3 parentPos, ParticleOptions options) {
        double value = (1 - (1 / (pos.distanceTo(position()) + 1))) / 4;

        for (double d = 0.0; d < 1.0; d += value)
            ((ServerLevel)level()).sendParticles(options, Mth.lerp(d, pos.x(), parentPos.x()), Mth.lerp(d, pos.y(), parentPos.y()), Mth.lerp(d, pos.z(), parentPos.z()), 1, 0.05, 0.05,  0.05, 0.01);
    }

    @Override
    public Moobloom getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        Moobloom moobloom = BovinesEntityTypes.MOOBLOOM.create(level, EntitySpawnReason.BREEDING);
        var pair = chooseBabyType(level, (Moobloom)otherParent, moobloom);
        CowVariantAttachment.setCowVariant(moobloom, pair.getFirst(), pair.getSecond());
        return moobloom;
    }

    public Holder<CowVariant<MoobloomConfiguration>> getCowVariant() {
        return Optional.ofNullable(CowVariantAttachment.getCowVariantHolderFromEntity(this, BovinesCowTypes.MOOBLOOM_TYPE)).orElse((Holder) level().registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).getOrThrow(BovinesCowVariants.MoobloomKeys.MISSING_MOOBLOOM));
    }

    @Nullable
    public Holder<CowVariant<MoobloomConfiguration>> getPreviousCowVariant() {
        return CowVariantAttachment.getPreviousCowVariantHolderFromEntity(this, BovinesCowTypes.MOOBLOOM_TYPE);
    }

    public void setCowVariant(Holder<CowVariant<MoobloomConfiguration>> value) {
        CowVariantAttachment.setCowVariant(this, value);
    }

    public void setCowVariant(Holder<CowVariant<MoobloomConfiguration>> value, Holder<CowVariant<MoobloomConfiguration>> previous) {
        CowVariantAttachment.setCowVariant(this, value, previous);
    }

    public void setCurrentWithPreviousCowType(Holder<CowVariant<MoobloomConfiguration>> value) {
        CowVariantAttachment.setCowVariant(this, value, getCowVariant());
    }

    public int getFlowerSpreadAttempts() {
        return entityData.get(FLOWER_SPREAD_ATTEMPTS);
    }

    public void setFlowerSpreadAttempts(int value) {
        entityData.set(FLOWER_SPREAD_ATTEMPTS, value);
    }

    public int getPollinatedResetTicks() {
        return entityData.get(POLLINATED_RESET_TICKS);
    }

    public void setPollinatedResetTicks(int value) {
        entityData.set(POLLINATED_RESET_TICKS, value);
    }

    public int getStandingStillForBeeTicks() {
        return entityData.get(STANDING_STILL_FOR_BEE_TICKS);
    }

    public void setStandingStillForBeeTicks(int value) {
        entityData.set(STANDING_STILL_FOR_BEE_TICKS, value);
    }

    public boolean shouldAllowShearing() {
        return entityData.get(ALLOW_SHEARING);
    }

    public void setAllowShearing(boolean value) {
        entityData.set(ALLOW_SHEARING, value);
    }

    public boolean shouldAllowConversion() {
        return entityData.get(ALLOW_CONVERSION);
    }

    public void setAllowConversion(boolean value) {
        entityData.set(ALLOW_CONVERSION, value);
    }


    public boolean hasSnow() {
        return entityData.get(HAS_SNOW);
    }

    public void setSnow(boolean value) {
        entityData.set(HAS_SNOW, value);
    }

    public boolean isSnowLayerPersistent() {
        return entityData.get(SNOW_LAYER_PERSISTENT);
    }

    public void setPersistentSnowLayer(boolean value) {
        entityData.set(SNOW_LAYER_PERSISTENT, value);
    }

    public static int getTotalSpawnWeight(LevelAccessor level, BlockPos pos) {
        int totalWeight = 0;

        for (CowVariant<?> cowVariant : level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT).stream().filter(cowVariant -> cowVariant.configuration() instanceof MoobloomConfiguration).toList()) {
            if (!(cowVariant.configuration() instanceof MoobloomConfiguration configuration))
                continue;

            Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
            if (biome.isPresent())
                totalWeight += biome.get().weight().asInt();
        }
        return totalWeight;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnType, @Nullable SpawnGroupData data) {
        if (spawnType != EntitySpawnReason.STRUCTURE) {
            if (data == null) {
                data = new MoobloomGroupData();
            }
            setCowVariant(((MoobloomGroupData)data).getSpawnType(blockPosition(), level, level.getRandom()));
        }
        CowVariantAttachment.sync(this);
        return super.finalizeSpawn(level, difficulty, spawnType, data);
    }

    public class LookAtBeeGoal extends Goal {
        public LookAtBeeGoal() {
            setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return Moobloom.this.getStandingStillForBeeTicks() > 0;
        }

        @Override
        public void start() {
            Moobloom.this.getNavigation().stop();
        }
    }

    public static class MoobloomGroupData extends AgeableMob.AgeableMobGroupData {
        public MoobloomGroupData() {
            super(true);
        }

        public Holder<CowVariant<MoobloomConfiguration>> getSpawnType(BlockPos pos, ServerLevelAccessor level, RandomSource random) {
            if (getTotalSpawnWeight(level, pos) > 0)
                return getMoobloomSpawnTypeDependingOnBiome(level, pos, random);
            else
                return getMostCommonMoobloomSpawnVariant(level, random);
        }

        public Holder<CowVariant<MoobloomConfiguration>> getMostCommonMoobloomSpawnVariant(ServerLevelAccessor level, RandomSource random) {
            var registry = level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT);
            int largestWeight = 0;
            Holder<CowVariant<?>> finalCowVariant = registry.getOrThrow(BovinesCowVariants.MoobloomKeys.MISSING_MOOBLOOM);

            for (Holder<CowVariant<?>> cowVariant : registry.registryKeySet().stream().map(registry::getOrThrow).filter(cowVariant -> cowVariant.isBound() && cowVariant.value().configuration() instanceof MoobloomConfiguration && cowVariant.value().configuration() != cowVariant.value().type().defaultConfig()).toList()) {
                if (!(cowVariant.value().configuration() instanceof MoobloomConfiguration configuration)) continue;

                int max = configuration.settings().biomes().unwrap().stream().map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
                if (max > largestWeight) {
                    finalCowVariant = cowVariant;
                    largestWeight = max;
                }
            }

            return (Holder)finalCowVariant;
        }

        public Holder<CowVariant<MoobloomConfiguration>> getMoobloomSpawnTypeDependingOnBiome(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
            var registry = level.registryAccess().lookupOrThrow(BovinesRegistryKeys.COW_VARIANT);
            List<Holder<CowVariant<MoobloomConfiguration>>> moobloomList = new ArrayList<>();
            int totalWeight = 0;

            for (Holder.Reference<CowVariant<?>> cowVariant : registry.registryKeySet().stream().map(registry::getOrThrow).filter(cowVariant -> cowVariant.isBound() && cowVariant.value().configuration() instanceof MoobloomConfiguration && cowVariant.value().configuration() != cowVariant.value().type().defaultConfig()).toList()) {
                if (!(cowVariant.value().configuration() instanceof MoobloomConfiguration configuration)) continue;

                Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
                if (biome.isPresent()) {
                    moobloomList.add((Holder) cowVariant);
                    totalWeight += biome.get().weight().asInt();
                }
            }

            if (moobloomList.size() == 1) {
                return moobloomList.getFirst();
            } else if (!moobloomList.isEmpty()) {
                int r = Mth.nextInt(random, 0, totalWeight - 1);
                for (Holder<CowVariant<MoobloomConfiguration>> cowVariant : moobloomList) {
                    int max = cowVariant.value().configuration().settings().biomes().unwrap().stream().filter(wrapper -> wrapper.data().contains(level.getBiome(pos))).map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
                    r -= max;
                    if (r < 0.0)
                        return cowVariant;
                }
            }
            return (Holder)registry.getOrThrow(BovinesCowVariants.MoobloomKeys.MISSING_MOOBLOOM);
        }
    }
}
