package house.greenhouse.bovinesandbuttercups.client.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.client.api.model.type.BovinesModelSetType;
import net.minecraft.Util;
import net.minecraft.client.resources.model.MissingBlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Function;

// TODO: Remove as soon as data driven blocks are introduced.
public class BovinesModelSetUtil {
    private static final Map<ResourceLocation, ResourceLocation> MODEL_TO_TYPE_MAP = new ConcurrentHashMap<>();

    public static CompletableFuture<List<ResourceLocation>> getModels(ResourceManager manager, Executor executor) {
        BovinesModelSetRegistry.clear();
        return CompletableFuture.supplyAsync(() -> manager.listResources("bovinesandbuttercups", fileName -> fileName.getPath().endsWith(".json")), executor).thenCompose(blocks -> {
            ArrayList<CompletableFuture<List<ResourceLocation>>> models = new ArrayList<>();

            for (Map.Entry<ResourceLocation, Resource> resourceEntry : blocks.entrySet()) {
                models.add(CompletableFuture.supplyAsync(() -> {
                    ResourceLocation resourceId = resourceEntry.getKey().withPath(s -> s.substring(21, s.length() - 5));

                    try {
                        Reader reader = resourceEntry.getValue().openAsReader();
                        JsonElement json = JsonParser.parseReader(reader);
                        reader.close();
                        if (json instanceof JsonObject jsonObject) {
                            if (!jsonObject.has("type")) {
                                BovinesAndButtercups.LOG.error("Could not find 'variant' field in Bovines and Buttercups model set json: {}.", resourceId);
                                return List.of();
                            }

                            if (!jsonObject.get("type").isJsonPrimitive() || !jsonObject.getAsJsonPrimitive("type").isString()) {
                                BovinesAndButtercups.LOG.error("'variant' field \"{}\" is not a string in Bovines and Buttercups model set json: {}.", jsonObject.get("type"), resourceId);
                                return List.of();
                            }

                            ResourceLocation typeLocation = ResourceLocation.tryParse(jsonObject.getAsJsonPrimitive("type").getAsString());

                            if (typeLocation == null) {
                                BovinesAndButtercups.LOG.error("'variant' field \"{}\" is not a resource location in Bovines and Buttercups model set json: {}.", jsonObject.get("type"), resourceId);
                                return List.of();
                            }

                            BovinesModelSetType modelsType = BovinesModelSetRegistry.getType(typeLocation);
                            if (modelsType == null) {
                                BovinesAndButtercups.LOG.error("Bovines Model Set Type with id \"{}\" does not exist, was referenced in bovines model set file at \"{}\"", typeLocation, resourceId);
                                return List.of();
                            }

                            var modelSet = modelsType.createReference(resourceId, jsonObject);
                            BovinesModelSetRegistry.register(resourceId, modelSet);
                            modelSet.resolvedModelPaths().forEach(path ->
                                    MODEL_TO_TYPE_MAP.put(path, typeLocation));
                            return modelSet.resolvedModelPaths();
                        }
                    } catch (Exception ex) {
                        BovinesAndButtercups.LOG.error("Unexpected error in Bovines and Buttercups model set \"{}\". {}", resourceEntry.getKey(), ex);
                        return List.of();
                    }
                    BovinesAndButtercups.LOG.error("Unexpected error in Bovines and Buttercups model set registry: {}.", resourceEntry.getKey());
                    return List.of();
                }, executor));
            }
            return Util.sequenceFailFast(models).thenApply(m -> m.stream().flatMap(Collection::stream).filter(Objects::nonNull).toList());
        });
    }

    public static UnbakedModel getUnbakedModel(ResourceLocation modelId) {
        if (modelId == null)
            return null;

        if (MODEL_TO_TYPE_MAP.containsKey(modelId)) {
            ResourceLocation typeKey = MODEL_TO_TYPE_MAP.get(modelId);
            MODEL_TO_TYPE_MAP.remove(modelId);

            BovinesModelSetType modelsType = BovinesModelSetRegistry.getType(typeKey);
            if (modelsType == null)
                return MissingBlockModel.missingModel();

            return modelsType.createUnbaked(modelId);
        }
        return null;
    }

    public static final RegistryOps.RegistryInfoLookup STARTUP_LOOKUP = new RegistryOps.RegistryInfoLookup() {
        @Override
        public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
            if (!BuiltInRegistries.REGISTRY.containsKey((ResourceKey) registryKey))
                throw new UnsupportedOperationException("Registry \"" + registryKey.location() + "\" is not supoprted within edible bovines model sets.");
            return Optional.of(new RegistryOps.RegistryInfo<>(new HolderOwner<>() {
                @Override
                public boolean canSerializeIn(HolderOwner<T> owner) {
                    return true;
                }
            }, new HolderGetter<>() {
                @Override
                public Optional<Holder.Reference<T>> get(ResourceKey<T> resourceKey) {
                    if (!BuiltInRegistries.REGISTRY.containsKey((ResourceKey) registryKey))
                        throw new UnsupportedOperationException("Registry \"" + registryKey.location() + "\" is not supoprted within edible bovines model sets.");
                    return ((Registry)BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryKey).value()).get(resourceKey);
                }

                @Override
                public Optional<HolderSet.Named<T>> get(TagKey<T> tagKey) {
                    throw new UnsupportedOperationException("Tags are not supported within edible bovines model sets.");
                }
            }, Lifecycle.stable()));
        }
    };

    public static final Codec<ItemStack> ITEM_OR_STRICT_STACK = Codec.either(BuiltInRegistries.ITEM.byNameCodec(), ItemStack.STRICT_SINGLE_ITEM_CODEC).xmap(e -> e.map(Item::getDefaultInstance, Function.identity()), stack -> {
        if (ItemStack.isSameItemSameComponents(stack, stack.getItem().getDefaultInstance()))
            return Either.left(stack.getItem());
        return Either.right(stack);
    });
}
