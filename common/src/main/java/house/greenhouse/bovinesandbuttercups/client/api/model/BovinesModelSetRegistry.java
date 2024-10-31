package house.greenhouse.bovinesandbuttercups.client.api.model;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.BovinesModelSetType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BovinesModelSetRegistry {
    private static final Map<ResourceLocation, BovinesModelSetType> TYPE_REGISTRY = new HashMap<>();
    private static final Map<ResourceLocation, BovinesModelSet> REGISTRY = new HashMap<>();

    private static final Set<ResourceLocation> WARNED_EXISTENT_TYPE_KEYS = new HashSet<>();
    private static final Set<ResourceLocation> WARNED_NON_EXISTENT_KEYS = new HashSet<>();

    public static void clear() {
        REGISTRY.clear();
        WARNED_EXISTENT_TYPE_KEYS.clear();
        WARNED_NON_EXISTENT_KEYS.clear();
    }

    @Nullable
    public static BovinesModelSetType getType(ResourceLocation key) {
        return TYPE_REGISTRY.get(key);
    }

    public static BovinesModelSetType registerType(ResourceLocation key, BovinesModelSetType type) {
        if (TYPE_REGISTRY.containsKey(key)) {
            if (!WARNED_EXISTENT_TYPE_KEYS.contains(key))
                BovinesAndButtercups.LOG.error("Attempted to register a bovines model set type which already contains a key: {}.", key);
            WARNED_EXISTENT_TYPE_KEYS.add(key);
            return type;
        }

        TYPE_REGISTRY.put(key, type);
        return type;
    }

    @Nullable
    public static BovinesModelSet get(ResourceLocation key) {
        if (!REGISTRY.containsKey(key)) {
            if (!WARNED_NON_EXISTENT_KEYS.contains(key))
                BovinesAndButtercups.LOG.warn("Bovines Model Set \"{}\" does not exist.", key);
            WARNED_NON_EXISTENT_KEYS.add(key);
            return null;
        }
        return REGISTRY.get(key);
    }

    public static void register(ResourceLocation key, BovinesModelSet reference) {
        REGISTRY.put(key, reference);
    }
}
