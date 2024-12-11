package house.greenhouse.bovinesandbuttercups.content.recipe.ingredient;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record RemainderIngredientImpl(Ingredient base, ItemStack remainder) implements RemainderIngredient, CustomIngredient {
    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public boolean test(ItemStack stack) {
        return RemainderIngredient.super.test(stack);
    }

    @Override
    public Stream<Holder<Item>> getMatchingItems() {
        return RemainderIngredient.super.items();
    }

    @Override
    public boolean requiresTesting() {
        return base.requiresTesting();
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements CustomIngredientSerializer<RemainderIngredientImpl> {
        private static final StreamCodec<RegistryFriendlyByteBuf, RemainderIngredientImpl> STREAM_CODEC = RemainderIngredient.createStreamCodec(RemainderIngredientImpl::new).map(remainderIngredient -> (RemainderIngredientImpl) remainderIngredient, Function.identity());;

        protected Serializer() {
        }

        @Override
        public ResourceLocation getIdentifier() {
            return RemainderIngredient.ID;
        }

        @Override
        public MapCodec<RemainderIngredientImpl> getCodec() {
            return RemainderIngredient.createCodec(RemainderIngredientImpl::new).xmap(remainderIngredient -> (RemainderIngredientImpl) remainderIngredient, Function.identity());
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RemainderIngredientImpl> getPacketCodec() {
            return STREAM_CODEC;
        }
    }
}
