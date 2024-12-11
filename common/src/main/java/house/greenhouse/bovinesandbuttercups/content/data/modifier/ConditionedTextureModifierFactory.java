package house.greenhouse.bovinesandbuttercups.content.data.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.NoOpTextureModifier;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.client.renderer.modifier.ConditionedTextureModifier;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncConditionedTextureModifier;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncCowTypeClientboundPacket;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParamSets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;

public class ConditionedTextureModifierFactory extends TextureModifierFactory<ConditionedTextureModifier> {
    public static final MapCodec<ConditionedTextureModifierFactory> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(f -> f.id),
            LootItemCondition.DIRECT_CODEC.validate(
                    lootCondition -> {
                        ProblemReporter.Collector problemreporter$collector = new ProblemReporter.Collector();
                        ValidationContext validationcontext = new ValidationContext(problemreporter$collector, BovinesLootContextParamSets.ENTITY);
                        lootCondition.validate(validationcontext);
                        return problemreporter$collector.getReport()
                                .map(err -> DataResult.<LootItemCondition>error(() -> "Validation error in texture modifier condition: " + err))
                                .orElseGet(() -> DataResult.success(lootCondition));
                    }
            ).listOf().fieldOf("condition").forGetter(f -> f.condition),
            Codec.intRange(1, 16).optionalFieldOf("tick_rate", 1).forGetter(f -> f.tickRate)
    ).apply(inst, ConditionedTextureModifierFactory::new));
    private static final WeakHashMap<UUID, Map<ResourceLocation, Boolean>> CONDITION_VALUES = new WeakHashMap<>();

    private final ResourceLocation id;
    private final List<LootItemCondition> condition;
    private final int tickRate;

    public ConditionedTextureModifierFactory(ResourceLocation id, List<LootItemCondition> condition, int tickRate) {
        this.id = id;
        this.condition = condition;
        this.tickRate = tickRate;
    }

    public ResourceLocation getConditionId() {
        return id;
    }

    public boolean getConditionValue(Entity entity) {
        return CONDITION_VALUES.getOrDefault(entity.getUUID(), new HashMap<>()).getOrDefault(id, false);
    }

    public void setConditionValue(Entity entity, boolean value) {
        CONDITION_VALUES.computeIfAbsent(entity.getUUID(), uuid -> new HashMap<>()).put(id, value);
    }

    @Override
    protected ConditionedTextureModifier createProvider() {
        return new ConditionedTextureModifier(id);
    }

    @Override
    public void init(Entity entity) {
        if (entity.level().isClientSide)
            return;
        LootParams.Builder params = new LootParams.Builder((ServerLevel) entity.level());
        params.withParameter(LootContextParams.THIS_ENTITY, entity);
        params.withParameter(LootContextParams.ORIGIN, entity.position());
        LootContext context = new LootContext.Builder(params.create(BovinesLootContextParamSets.ENTITY)).create(Optional.empty());
        boolean conditionValue = condition.stream().allMatch(condition1 -> condition1.test(context));
        setConditionValue(entity, conditionValue);
        if (entity instanceof LivingEntity living && BovinesAndButtercups.getHelper().getCowTypeAttachment(living) != null)
            // This guarantees that the cow type will be synced to the client before running this modifier's sync code.
            BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(entity, new SyncCowTypeClientboundPacket(entity.getId(), BovinesAndButtercups.getHelper().getCowTypeAttachment(living), false), new SyncConditionedTextureModifier(entity.getId(), getConditionId(), conditionValue));
        else
            BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(entity, new SyncConditionedTextureModifier(entity.getId(), getConditionId(), conditionValue));
    }

    @Override
    public void tick(Entity entity) {
        if (entity.level().isClientSide() || entity.tickCount % tickRate != 0)
            return;
        LootParams.Builder params = new LootParams.Builder((ServerLevel) entity.level());
        params.withParameter(LootContextParams.THIS_ENTITY, entity);
        params.withParameter(LootContextParams.ORIGIN, entity.position());
        LootContext context = new LootContext.Builder(params.create(BovinesLootContextParamSets.ENTITY)).create(Optional.empty());
        boolean conditionValue = condition.stream().allMatch(condition1 -> condition1.test(context));
        boolean oldConditionValue = getConditionValue(entity);
        if (conditionValue != oldConditionValue) {
            setConditionValue(entity, conditionValue);
            BovinesAndButtercups.getHelper().sendTrackingClientboundPacket(entity, new SyncConditionedTextureModifier(entity.getId(), getConditionId(), conditionValue));
        }
    }

    @Override
    public MapCodec<? extends TextureModifierFactory<?>> codec() {
        return CODEC;
    }

}
