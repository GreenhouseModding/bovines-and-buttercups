package house.greenhouse.bovinesandbuttercups.content.recipe.ingredient;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.function.Function;
import java.util.stream.Stream;

public record RemainderIngredientImpl(Ingredient base, ItemStack remainder) implements RemainderIngredient, ICustomIngredient {
    private static final StreamCodec<RegistryFriendlyByteBuf, RemainderIngredientImpl> STREAM_CODEC = RemainderIngredient.createStreamCodec(RemainderIngredientImpl::new).map(remainderIngredient -> (RemainderIngredientImpl) remainderIngredient, Function.identity());;
    public static final IngredientType<RemainderIngredientImpl> TYPE = new IngredientType<>(RemainderIngredient.createCodec(RemainderIngredientImpl::new).xmap(remainderIngredient -> (RemainderIngredientImpl)remainderIngredient, Function.identity()), STREAM_CODEC);

    @Override
    public boolean test(ItemStack stack) {
        return RemainderIngredient.super.test(stack);
    }

    @Override
    public Stream<Holder<Item>> items() {
        return RemainderIngredient.super.items();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return TYPE;
    }
}
