package house.greenhouse.bovinesandbuttercups.content.entity;

import com.mojang.datafixers.util.Pair;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.attachment.CowTypeAttachment;
import house.greenhouse.bovinesandbuttercups.api.cowtype.OffspringConditions;
import house.greenhouse.bovinesandbuttercups.content.advancement.criterion.BreedCowWithTypeTrigger;
import house.greenhouse.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import house.greenhouse.bovinesandbuttercups.content.component.ItemCustomFlower;
import house.greenhouse.bovinesandbuttercups.content.component.ItemNectar;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.mixin.EntityAccessor;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncMoobloomSnowLayerClientboundPacket;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypeTypes;
import house.greenhouse.bovinesandbuttercups.api.BovinesCowTypes;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParamSets;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParams;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import house.greenhouse.bovinesandbuttercups.util.SnowLayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
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
    private static final EntityDataAccessor<Boolean> HAS_SNOW = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SNOW_LAYER_PERSISTENT = SynchedEntityData.defineId(Moobloom.class, EntityDataSerializers.BOOLEAN);
    @Nullable
    public Bee bee;
    private boolean hasRefreshedDimensionsForLaying;
    @Nullable
    private BlockPos previousPos = null;
    @Nullable private UUID lastLightningBoltUUID;
    private final Map<Holder<CowType<?>>, List<Vec3>> particlePositions = new HashMap<>();

    public final AnimationState layDownAnimationState = new AnimationState();
    public final AnimationState getUpAnimationState = new AnimationState();

    public Moobloom(EntityType<? extends Moobloom> entityType, Level level) {
        super(entityType, level);
        bee = null;
        if (getCowType() == null) {
            setCowType((Holder)level().registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().getHolder(BovinesCowTypes.MoobloomKeys.MISSING_MOOBLOOM).orElseThrow());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLOWER_SPREAD_ATTEMPTS, 0);
        builder.define(POLLINATED_RESET_TICKS, 0);
        builder.define(STANDING_STILL_FOR_BEE_TICKS, 0);
        builder.define(ALLOW_SHEARING, true);
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
        tag.putInt("pollinated_reset_ticks", getPollinatedResetTicks());
        tag.putBoolean("allow_shearing", shouldAllowShearing());
        tag.putBoolean("has_snow", hasSnow());
        if (isSnowLayerPersistent())
            tag.putBoolean("snow_layer_persistent", true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        backwardsCompat(tag);
        if (tag.contains("flower_spread_attempts", Tag.TAG_INT))
            setFlowerSpreadAttempts(tag.getInt("flower_spread_attempts"));
        if (tag.contains("pollinated_reset_ticks", Tag.TAG_INT))
            setPollinatedResetTicks(tag.getInt("pollinated_reset_ticks"));
        if (tag.contains("allow_shearing", Tag.TAG_BYTE))
            setAllowShearing(tag.getBoolean("allow_shearing"));
        if (tag.contains("has_snow", Tag.TAG_BYTE))
            setSnow(tag.getBoolean("has_snow"));
        if (tag.contains("snow_layer_persistent", Tag.TAG_BYTE))
            setPersistentSnowLayer(tag.getBoolean("snow_layer_persistent"));
    }

    public void backwardsCompat(CompoundTag tag) {
        if (tag.contains("Type", Tag.TAG_STRING)) {
            Optional<Holder.Reference<CowType<?>>> cowType = level().registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().getHolder(ResourceLocation.parse(tag.getString("Type")));
            if (cowType.isEmpty()) {
                BovinesAndButtercups.LOG.error("Could not deserialize legacy cow type tag \"{}\" into a cow type holder.", tag.getString("Type"));
                return;
            }
            if (!cowType.get().isBound() || cowType.get().value().type() != BovinesCowTypeTypes.MOOBLOOM_TYPE)  {
                BovinesAndButtercups.LOG.error("Cow Type \"{}\" is not bound or is not a moobloom.", cowType);
                return;
            }
            if (tag.contains("PreviousType", Tag.TAG_STRING)) {
                Optional<Holder.Reference<CowType<?>>> previousCowType = level().registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().getHolder(ResourceLocation.parse(tag.getString("Type")));
                if (previousCowType.isEmpty()) {
                    BovinesAndButtercups.LOG.error("Could not deserialize legacy cow type tag \"{}\" into a cow type holder.", tag.getString("Type"));
                    return;
                }
                if (!previousCowType.get().isBound() || previousCowType.get().value().type() != BovinesCowTypeTypes.MOOBLOOM_TYPE) {
                    BovinesAndButtercups.LOG.error("Previous Cow Type \"{}\" is not bound or is not a moobloom.", cowType);
                    return;
                }
                BovinesAndButtercups.getHelper().setCowTypeAttachment(this, new CowTypeAttachment(cowType.get(), previousCowType.map(cowTypeReference -> cowTypeReference)));
                CowTypeAttachment.sync(this);
            } else {
                setCowType((Holder) cowType.get());
                CowTypeAttachment.sync(this);
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

    public static boolean canMoobloomSpawn(EntityType<? extends Moobloom> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(level, pos) && getTotalSpawnWeight(level, pos) > 0;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt bolt) {
        UUID uuid = bolt.getUUID();
        if (!uuid.equals(lastLightningBoltUUID)) {
            if (getPreviousCowType() == null) {
                if (getCowType().value().configuration().settings().thunderConverts().isEmpty()) {
                    super.thunderHit(level, bolt);
                    return;
                }

                List<WeightedEntry.Wrapper<Holder<CowType<MoobloomConfiguration>>>> compatibleList = getCowType().value().configuration().settings().filterThunderConverts(BovinesCowTypeTypes.MOOBLOOM_TYPE);

                if (compatibleList.isEmpty()) {
                    super.thunderHit(level, bolt);
                    return;
                } else if (compatibleList.size() == 1) {
                    setCurrentWithPreviousCowType(compatibleList.getFirst().data());
                    CowTypeAttachment.sync(this);
                } else {
                    int totalWeight = level.getRandom().nextInt(compatibleList.stream().map(holderWrapper -> holderWrapper.weight().asInt()).reduce(Integer::sum).orElse(0));
                    for (WeightedEntry.Wrapper<Holder<CowType<MoobloomConfiguration>>> cct : compatibleList) {
                        totalWeight -= cct.weight().asInt();
                        if (totalWeight < 0) {
                            setCurrentWithPreviousCowType(cct.data());
                            CowTypeAttachment.sync(this);
                            break;
                        }
                    }
                }
            } else {
                setCowType(getPreviousCowType());
                CowTypeAttachment.sync(this);
            }
            lastLightningBoltUUID = uuid;
            playSound(BovinesSoundEvents.MOOBLOOM_CONVERT, 2.0F, 1.0F);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        }
        if (bee != null && !level().isClientSide()) {
            setStandingStillForBeeTicks(0);
            bee = null;
        }
        return super.hurt(source, amount);
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

            if (getUpAnimationState.isStarted() && getUpAnimationState.getAccumulatedTime() >= 1000)
                getUpAnimationState.stop();
        } else {
            if ((previousPos == null || blockPosition().distSqr(previousPos) > 4) && getFlowerSpreadAttempts() > 0) {
                if (spreadFlowers())
                    setFlowerSpreadAttempts(getFlowerSpreadAttempts() - 1);
                previousPos = blockPosition();
                if (getFlowerSpreadAttempts() <= 0)
                    previousPos = null;
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
        }

        if (getPollinatedResetTicks() > 0)
            setPollinatedResetTicks(getPollinatedResetTicks() - 1);

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
            return biome.getPrecipitationAt(pos) == Biome.Precipitation.SNOW;
        }
    }

    public boolean spreadFlowers() {
        if (level().isClientSide) return true;

        BlockState state = null;
        if (getCowType().value().configuration().flower().blockState().isPresent())
            state = getCowType().value().configuration().flower().blockState().get().getBlock().defaultBlockState();
        else if (getCowType().value().configuration().flower().customType().isPresent())
            state = BovinesBlocks.CUSTOM_FLOWER.defaultBlockState();

        if (state == null) {
            BovinesAndButtercups.LOG.warn("Moobloom with type '{}' tried to spread flowers without a valid flower type.", getCowType().getRegisteredName());
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
        if (level().isClientSide) return;
        ((ServerLevel) level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5D, pos.getY() + 0.3D, pos.getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
        if (state.getBlock() == BovinesBlocks.CUSTOM_FLOWER && getCowType().value().configuration().flower().customType().isPresent()) {
            level().setBlock(pos, state, Block.UPDATE_ALL);
            BlockEntity blockEntity = level().getBlockEntity(pos);
            if (blockEntity instanceof CustomFlowerBlockEntity customFlowerBlockEntity) {
                customFlowerBlockEntity.setFlowerType(new ItemCustomFlower(getCowType().value().configuration().flower().customType().get()));
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
            if (stack.is(Items.BONE_MEAL) && getFlowerSpreadAttempts() < 8) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (!level().isClientSide) {
                    ((ServerLevel) level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, position().x(), position().y() + 1.6D, position().z(), 8, 0.5, 0.1, 0.4, 0.0);
                    setFlowerSpreadAttempts(getFlowerSpreadAttempts() + 8);
                }
                playSound(BovinesSoundEvents.MOOBLOOM_EAT, 1.0f, (random.nextFloat() * 0.4F) + 0.8F);
                return InteractionResult.sidedSuccess(level().isClientSide);
            } else if (stack.is(Items.BOWL)) {
                ItemStack stack2;
                stack2 = new ItemStack(BovinesItems.NECTAR_BOWL);
                if (getCowType().value().configuration().nectar().isPresent()) {
                    stack2.set(BovinesDataComponents.NECTAR, new ItemNectar(getCowType().value().configuration().nectar().get()));
                } else {
                    return InteractionResult.PASS;
                }

                ItemStack stack3 = ItemUtils.createFilledResult(stack, player, stack2, false);
                player.setItemInHand(hand, stack3);
                playSound(BovinesSoundEvents.MOOBLOOM_MILK, 1.0f, 1.0f);
                return InteractionResult.sidedSuccess(level().isClientSide());
            }
        }
        InteractionResult result = SnowLayerUtil.removeSnowIfShovel(this, player, hand, stack);
        if (result != InteractionResult.PASS)
            return result;
        return super.mobInteract(player, hand);
    }

    public Pair<Holder<CowType<MoobloomConfiguration>>, Optional<Holder<CowType<MoobloomConfiguration>>>> chooseBabyType(ServerLevel level, Moobloom otherParent, Moobloom child) {
        List<Holder<CowType<MoobloomConfiguration>>> eligibleCowTypes = new ArrayList<>();

        for (Holder.Reference<CowType<?>> cowType : level.registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().holders().filter(type -> type.isBound() && type.value().type() == BovinesCowTypeTypes.MOOBLOOM_TYPE && ((MoobloomConfiguration)type.value().configuration()).offspringConditions() != OffspringConditions.EMPTY).toList()) {
            Holder.Reference<CowType<MoobloomConfiguration>> moobloomType = (Holder.Reference) cowType;
            var conditions = moobloomType.value().configuration().offspringConditions();

            LootParams.Builder params = new LootParams.Builder(level);
            params.withParameter(LootContextParams.THIS_ENTITY, this);
            params.withParameter(BovinesLootContextParams.PARTNER, otherParent);
            params.withParameter(BovinesLootContextParams.CHILD, child);
            params.withParameter(LootContextParams.ORIGIN, position());
            params.withParameter(BovinesLootContextParams.BREEDING_TYPE, cowType);

            LootContext thisContext = new LootContext.Builder(params.create(BovinesLootContextParamSets.BREEDING)).create(Optional.empty());

            params.withParameter(LootContextParams.THIS_ENTITY, otherParent);
            params.withParameter(BovinesLootContextParams.PARTNER, this);
            LootContext otherContext = new LootContext.Builder(params.create(BovinesLootContextParamSets.BREEDING)).create(Optional.empty());

            if (conditions.thisConditions().stream().allMatch(condition -> condition.test(thisContext))
            && conditions.otherConditions().stream().allMatch(condition -> condition.test(otherContext)))
                eligibleCowTypes.add(moobloomType);
        }

        if (!eligibleCowTypes.isEmpty()) {
            int random = getRandom().nextInt(eligibleCowTypes.size());
            var randomType = eligibleCowTypes.get(random);

            child.createParticles(randomType, position());

            if (getLoveCause() != null)
                BreedCowWithTypeTrigger.INSTANCE.trigger(getLoveCause(), this, otherParent, child, true, (Holder) randomType);
            return randomType.value().configuration().offspringConditions().inheritance().handleInheritance(randomType, BovinesAndButtercups.getHelper().getCowTypeAttachment(this), BovinesAndButtercups.getHelper().getCowTypeAttachment(otherParent));
        }

        child.particlePositions.clear();

        if (!otherParent.getCowType().equals(getCowType()) && getRandom().nextBoolean()) {
            if (getLoveCause() != null)
                BreedCowWithTypeTrigger.INSTANCE.trigger(getLoveCause(), this, otherParent, child, false, (Holder<CowType<?>>)(Holder<?>)otherParent.getCowType());
            return Pair.of(otherParent.getCowType(), Optional.ofNullable(otherParent.getPreviousCowType()));
        }

        if (getLoveCause() != null)
            BreedCowWithTypeTrigger.INSTANCE.trigger(getLoveCause(), this, otherParent, child, false, (Holder<CowType<?>>)(Holder<?>)getCowType());
        return Pair.of(getCowType(), Optional.ofNullable(getPreviousCowType()));
    }

    public void addParticlePosition(Holder<CowType<?>> type, Vec3 pos) {
        particlePositions.computeIfAbsent(type, holder -> new ArrayList<>()).add(pos);
    }

    public void createParticles(Holder<CowType<MoobloomConfiguration>> type, Vec3 parentPos) {
        if (!type.isBound() || type.value().configuration().settings().particle().isEmpty())
            return;

        if (particlePositions.isEmpty() && !level().isClientSide())
            ((ServerLevel)level()).sendParticles(type.value().configuration().settings().particle().get(), getX(), getY(0.5), getZ(), 6, 0.05, 0.05, 0.05, 0.01);

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
        Moobloom moobloom = BovinesEntityTypes.MOOBLOOM.create(level);
        var pair = chooseBabyType(level, (Moobloom)otherParent, moobloom);
        CowTypeAttachment.setCowType(moobloom, pair.getFirst(), pair.getSecond());
        return moobloom;
    }

    public Holder<CowType<MoobloomConfiguration>> getCowType() {
        return CowTypeAttachment.getCowTypeHolderFromEntity(this, BovinesCowTypeTypes.MOOBLOOM_TYPE);
    }
    
    public Holder<CowType<MoobloomConfiguration>> getPreviousCowType() {
        return CowTypeAttachment.getPreviousCowTypeHolderFromEntity(this, BovinesCowTypeTypes.MOOBLOOM_TYPE);
    }

    public void setCowType(Holder<CowType<MoobloomConfiguration>> value) {
        CowTypeAttachment.setCowType(this, value);
    }

    public void setCowType(Holder<CowType<MoobloomConfiguration>> value, Holder<CowType<MoobloomConfiguration>> previous) {
        CowTypeAttachment.setCowType(this, value, previous);
    }

    public void setCurrentWithPreviousCowType(Holder<CowType<MoobloomConfiguration>> value) {
        CowTypeAttachment.setCowType(this, value, getCowType());
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

        for (CowType<?> cowType : level.registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().stream().filter(cowType -> cowType.configuration() instanceof MoobloomConfiguration).toList()) {
            if (!(cowType.configuration() instanceof MoobloomConfiguration configuration))
                continue;

            Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
            if (biome.isPresent())
                totalWeight += biome.get().weight().asInt();
        }
        return totalWeight;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        if (spawnType != MobSpawnType.STRUCTURE) {
            if (data == null) {
                data = new MoobloomGroupData();
            }
            setCowType(((MoobloomGroupData)data).getSpawnType(blockPosition(), level, level.getRandom()));
        }
        CowTypeAttachment.sync(this);
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

        public Holder<CowType<MoobloomConfiguration>> getSpawnType(BlockPos pos, ServerLevelAccessor level, RandomSource random) {
            if (getTotalSpawnWeight(level, pos) > 0)
                return getMoobloomSpawnTypeDependingOnBiome(level, pos, random);
            else
                return getMostCommonMoobloomSpawnType(level, random);
        }

        public Holder<CowType<MoobloomConfiguration>> getMostCommonMoobloomSpawnType(ServerLevelAccessor level, RandomSource random) {
            int largestWeight = 0;
            Holder<CowType<?>> finalCowType = level.registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().getHolder(BovinesCowTypes.MoobloomKeys.MISSING_MOOBLOOM).get();

            for (Holder<CowType<?>> cowType : level.registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().holders().filter(cowType -> cowType.isBound() && cowType.value().configuration() instanceof MoobloomConfiguration && cowType.value().configuration() != MoobloomConfiguration.DEFAULT).toList()) {
                if (!(cowType.value().configuration() instanceof MoobloomConfiguration configuration)) continue;

                int max = configuration.settings().biomes().unwrap().stream().map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
                if (max > largestWeight) {
                    finalCowType = cowType;
                    largestWeight = max;
                }
            }

            return (Holder)finalCowType;
        }

        public Holder<CowType<MoobloomConfiguration>> getMoobloomSpawnTypeDependingOnBiome(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
            List<Holder<CowType<MoobloomConfiguration>>> moobloomList = new ArrayList<>();
            int totalWeight = 0;

            for (Holder.Reference<CowType<?>> cowType : level.registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().holders().filter(cowType -> cowType.isBound() && cowType.value().configuration() instanceof MoobloomConfiguration && cowType.value().configuration() != MoobloomConfiguration.DEFAULT).toList()) {
                if (!(cowType.value().configuration() instanceof MoobloomConfiguration configuration)) continue;

                Optional<WeightedEntry.Wrapper<HolderSet<Biome>>> biome = configuration.settings().biomes().unwrap().stream().filter(holderSetWrapper -> holderSetWrapper.data().contains(level.getBiome(pos))).findFirst();
                if (biome.isPresent()) {
                    moobloomList.add((Holder) cowType);
                    totalWeight += biome.get().weight().asInt();
                }
            }

            if (moobloomList.size() == 1) {
                return moobloomList.getFirst();
            } else if (!moobloomList.isEmpty()) {
                int r = Mth.nextInt(random, 0, totalWeight - 1);
                for (Holder<CowType<MoobloomConfiguration>> cowType : moobloomList) {
                    int max = cowType.value().configuration().settings().biomes().unwrap().stream().filter(wrapper -> wrapper.data().contains(level.getBiome(pos))).map(wrapper -> wrapper.weight().asInt()).max(Comparator.comparingInt(value -> value)).orElse(0);
                    r -= max;
                    if (r < 0.0)
                        return cowType;
                }
            }
            return (Holder)level.registryAccess().registry(BovinesRegistryKeys.COW_TYPE).orElseThrow().getHolder(BovinesCowTypes.MoobloomKeys.MISSING_MOOBLOOM).get();
        }
    }
}
