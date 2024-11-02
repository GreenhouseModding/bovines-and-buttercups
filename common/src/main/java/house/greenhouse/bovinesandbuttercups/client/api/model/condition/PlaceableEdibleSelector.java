package house.greenhouse.bovinesandbuttercups.client.api.model.condition;

import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Keyable;
import house.greenhouse.bovinesandbuttercups.client.util.BovinesModelSetUtil;
import house.greenhouse.bovinesandbuttercups.content.block.entity.PlaceableEdibleBlockEntity;
import house.greenhouse.bovinesandbuttercups.util.IntRange;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PlaceableEdibleSelector(PlaceableEdibleCondition condition, MultiVariant variant) {
    public PlaceableEdibleSelector(PlaceableEdibleCondition condition, MultiVariant variant) {
        if (condition == null) {
            throw new IllegalArgumentException("Missing condition for selector");
        } else if (variant == null) {
            throw new IllegalArgumentException("Missing variant for selector");
        } else {
            this.condition = condition;
            this.variant = variant;
        }
    }

    public boolean test(PlaceableEdibleBlockEntity blockEntity) {
        return condition.test(blockEntity);
    }

    public static class Deserializer implements JsonDeserializer<PlaceableEdibleSelector> {
        public PlaceableEdibleSelector deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonobject = json.getAsJsonObject();
            return new PlaceableEdibleSelector(this.getSelector(jsonobject), context.deserialize(jsonobject.get("apply"), MultiVariant.class));
        }

        private PlaceableEdibleCondition getSelector(JsonObject json) {
            return json.has("when") ? getCondition(json.get("when")) : PlaceableEdibleCondition.TRUE;
        }

        static PlaceableEdibleCondition getCondition(JsonElement json) {
            if (json.isJsonObject()) {
                JsonObject jo = json.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> set = jo.entrySet();
                if (set.isEmpty()) {
                    throw new JsonParseException("No elements found in selector");
                }
                if (jo.has("OR")) {
                    List<PlaceableEdibleCondition> list1 = Streams.stream(GsonHelper.getAsJsonArray(jo, "OR"))
                            .map(p_112038_ -> getCondition(p_112038_.getAsJsonObject()))
                            .collect(Collectors.toList());
                    return new OrCondition(list1);
                } else if (jo.has("AND")) {
                    List<PlaceableEdibleCondition> list = Streams.stream(GsonHelper.getAsJsonArray(jo, "AND"))
                            .map(p_112028_ -> getCondition(p_112028_.getAsJsonObject()))
                            .collect(Collectors.toList());
                    return new AndCondition(list);
                }
                return getValueCondition(jo);
            } else if (json.isJsonArray()) {
                JsonArray ja = json.getAsJsonArray();
                if (ja.isEmpty())
                    throw new JsonParseException("No elements found in selector");
                List<PlaceableEdibleCondition> list = Streams.stream(ja)
                        .map(p_112028_ -> getCondition(p_112028_.getAsJsonObject()))
                        .collect(Collectors.toList());
                return new AndCondition(list);
            }
            throw new JsonParseException("Selector must be either an array of conditions, or a condition object, prefixed with \"AND\" or \"OR\" if necessary");
        }

        static PlaceableEdibleCondition getValueCondition(JsonObject json) {
            var bites = Optional.<IntRange>empty();
            if (json.has("bites"))
                bites = Optional.of(IntRange.codec(1, 16).decode(JsonOps.INSTANCE, json.get("bites")).getOrThrow(JsonParseException::new).getFirst());
            var attachments = Map.<Either<TagKey<Item>, List<ResourceKey<Item>>>, ValueCondition.AttachmentValues>of();
            if (json.has("attachments"))
                attachments = Codec.simpleMap(Codec.either(TagKey.hashedCodec(Registries.ITEM), Codec.either(ResourceKey.codec(Registries.ITEM).listOf(), ResourceKey.codec(Registries.ITEM)).xmap(either -> either.map(resourceKeys -> resourceKeys, List::of), resourceKeys -> {
                    if (resourceKeys.size() == 1)
                        return Either.right(resourceKeys.getFirst());
                    return Either.left(resourceKeys);
                })), ValueCondition.AttachmentValues.CODEC, Keyable.forStrings(() -> Stream.of("items", "values"))).codec().decode(RegistryOps.create(JsonOps.INSTANCE, BovinesModelSetUtil.STARTUP_LOOKUP), json.getAsJsonObject("attachments")).getOrThrow(JsonParseException::new).getFirst();
            return new ValueCondition(bites, attachments);
        }
    }
}