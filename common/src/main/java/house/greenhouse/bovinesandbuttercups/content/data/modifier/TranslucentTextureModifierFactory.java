package house.greenhouse.bovinesandbuttercups.content.data.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import house.greenhouse.bovinesandbuttercups.client.renderer.modifier.EmissiveTextureModifier;
import house.greenhouse.bovinesandbuttercups.client.renderer.modifier.TranslucentTextureModifier;
import house.greenhouse.bovinesandbuttercups.content.loot.BovinesLootContextParamSets;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncConditionedTextureModifier;
import house.greenhouse.bovinesandbuttercups.network.clientbound.SyncCowTypeClientboundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;

public class TranslucentTextureModifierFactory extends TextureModifierFactory<TranslucentTextureModifier> {
    public static final MapCodec<TranslucentTextureModifierFactory> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("speed", 0.02F).forGetter(TranslucentTextureModifierFactory::speed),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("min", 0.0F).forGetter(TranslucentTextureModifierFactory::min),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("max", 1.0F).forGetter(TranslucentTextureModifierFactory::max)
    ).apply(inst, TranslucentTextureModifierFactory::new));

    private final float speed;
    private final float min;
    private final float max;

    public TranslucentTextureModifierFactory() {
        this.speed = 0.02F;
        this.min = 0.0F;
        this.max = 1.0F;
    }

    public TranslucentTextureModifierFactory(float speed,
                                             float min, float max) {
        this.speed = speed;
        this.min = min;
        this.max = max;
    }

    public float speed() {
        return speed;
    }

    public float min() {
        return min;
    }

    public float max() {
        return max;
    }

    @Override
    protected TranslucentTextureModifier createProvider() {
        return new TranslucentTextureModifier(speed, min, max);
    }

    @Override
    public MapCodec<? extends TextureModifierFactory<?>> codec() {
        return CODEC;
    }

}
