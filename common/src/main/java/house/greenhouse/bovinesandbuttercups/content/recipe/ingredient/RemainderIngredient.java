package house.greenhouse.bovinesandbuttercups.content.recipe.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.stream.Stream;

public interface RemainderIngredient {
    ResourceLocation ID = BovinesAndButtercups.asResource("remainder");
    static MapCodec<RemainderIngredient> createCodec(Constructor constructor, boolean allowEmpty) {
        Codec<Ingredient> ingredientCodec = !allowEmpty ? Ingredient.CODEC_NONEMPTY : Ingredient.CODEC;
        return RecordCodecBuilder.mapCodec(inst -> inst.group(
                ingredientCodec.fieldOf("base").forGetter(RemainderIngredient::base),
                ItemStack.STRICT_SINGLE_ITEM_CODEC.validate(
                        stack -> {
                            if (stack.is(Items.AIR)) {
                                return DataResult.error(() -> "Item must not be minecraft:air");
                            }
                            return DataResult.success(stack);
                        }
                ).fieldOf("remainder").forGetter(RemainderIngredient::remainder)
        ).apply(inst, constructor::construct));
    }
    static StreamCodec<RegistryFriendlyByteBuf, RemainderIngredient> createStreamCodec(Constructor constructor) {
        return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, RemainderIngredient::base,
                ItemStack.STREAM_CODEC, RemainderIngredient::remainder,
                constructor::construct
        );
    }

    Ingredient base();
    ItemStack remainder();

    default boolean test(ItemStack stack) {
        return base().test(stack);
    }

    default Stream<ItemStack> getItems() {
        return Arrays.stream(base().getItems());
    }

    @FunctionalInterface
    interface Constructor {
        RemainderIngredient construct(Ingredient base, ItemStack stack);
    }
}
