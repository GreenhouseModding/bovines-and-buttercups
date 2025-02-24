package house.greenhouse.bovinesandbuttercups.client.api;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.content.data.modifier.ConditionedTextureModifierFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class RenderStateObject {
    private static final Map<RenderStateObject.Type<Object>, Function<Entity, Object>> GLOBAL_REGISTRY = new HashMap<>();

    public static final Type<List<ResourceLocation>> ACTIVE_CONDITIONS = new Type<>(BovinesAndButtercups.asResource("active_conditions"), (Class<List<ResourceLocation>>)(Object)List.class);
    public static final Type<BlockPos> BLOCK_POS = new Type<>(BovinesAndButtercups.asResource("block_pos"), BlockPos.class);

    public static void registerAll() {
        registerGlobalRenderStateObject(ACTIVE_CONDITIONS, entity -> {
            if (entity instanceof LivingEntity living) {
                var attachment = BovinesAndButtercups.getHelper().getCowVariantAttachment(living);
                if (attachment == null)
                    return List.of();
                return attachment.cowVariant().value().configuration().settings().layers().stream().flatMap(cowModelLayer -> cowModelLayer.textureModifiers().stream().map(textureModifierFactory -> {
                    if (textureModifierFactory instanceof ConditionedTextureModifierFactory conditioned && conditioned.getConditionValue(entity))
                        return conditioned.getConditionId();
                    return null;
                }).filter(Objects::nonNull)).toList();
            }
            return List.of();
        });
        registerGlobalRenderStateObject(BLOCK_POS, Entity::blockPosition);
    }

    public static <T> void registerGlobalRenderStateObject(RenderStateObject.Type<T> type, Function<Entity, T> function) {
        if (GLOBAL_REGISTRY.containsKey(type)) {
            throw new UnsupportedOperationException("Cannot register variant '" + type.id() + "' twice.");
        }
        GLOBAL_REGISTRY.put((Type<Object>) type, (Function<Entity, Object>) function);
    }

    public static void setupGlobalObjects(Map<RenderStateObject.Type<Object>, Object> toModify, Entity entity) {
        for (var registryValue : GLOBAL_REGISTRY.entrySet()) {
            toModify.put(registryValue.getKey(), registryValue.getValue().apply(entity));
        }
    }

    public record Type<T>(ResourceLocation id, Class<T> type) {}
}
