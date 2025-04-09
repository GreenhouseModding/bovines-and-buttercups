package house.greenhouse.bovinesandbuttercups.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemEdible;
import house.greenhouse.bovinesandbuttercups.content.item.BovinesItems;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

import java.util.List;

public class SuspiciousEdibleRecipe extends CustomRecipe {
    private final ShapedRecipePattern pattern;
    private final Holder<EdibleBlockType> edibleType;
    private final String group;
    private final boolean symmetrical;

    public SuspiciousEdibleRecipe(CraftingBookCategory category, ShapedRecipePattern pattern, Holder<EdibleBlockType> edibleType, String group) {
        super(category);
        this.pattern = pattern;
        this.edibleType = edibleType;
        this.group = group;
        this.symmetrical = Util.isSymmetrical(pattern.width(), pattern.height(), pattern.ingredients());
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (!canCraftInDimensions(input.width(), input.height()))
            return false;

        for (int i = 0; i < pattern.height(); i++) {
            for (int j = 0; j < pattern.width(); j++) {
                Ingredient ingredient;
                if (symmetrical) {
                    ingredient = pattern.ingredients().get(pattern.width() - j - 1 + i * pattern.width());
                } else {
                    ingredient = pattern.ingredients().get(j + i * pattern.width());
                }

                ItemStack stack = input.getItem(j, i);
                if ((!ingredient.test(stack) && (!stack.is(Items.SUSPICIOUS_STEW) || !ingredient.test(new ItemStack(Items.SUSPICIOUS_STEW))))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack suspiciousStew = input.items().stream().filter(stack -> stack.is(Items.SUSPICIOUS_STEW)).findFirst().orElse(new ItemStack(Items.SUSPICIOUS_STEW));
        ItemStack returnStack = new ItemStack(BovinesItems.PLACEABLE_EDIBLE);

        List<ItemEdible.MobEffectEntry> entries = suspiciousStew.getOrDefault(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY).effects().stream().map(entry ->
                new ItemEdible.MobEffectEntry(new MobEffectInstance(entry.effect(), Mth.ceil((float) entry.duration() / 4)), entry.duration(), ItemEdible.MobEffectEntry.ShowTooltip.CREATIVE_MENU_ONLY)).toList();

        ItemEdible.apply(returnStack, new ItemEdible(edibleType, entries));
        // TODO: Unhardcode result count.
        return returnStack.copyWithCount(4);
    }

    public ShapedRecipePattern getPattern() {
        return pattern;
    }

    public Holder<EdibleBlockType> getEdibleType() {
        return edibleType;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= pattern.width() * pattern.height();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BovinesRecipeSerializers.SUSPICIOUS_EDIBLE;
    }

    public static class Serializer implements RecipeSerializer<SuspiciousEdibleRecipe> {
        public static final MapCodec<SuspiciousEdibleRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC).forGetter(CraftingRecipe::category),
                ShapedRecipePattern.MAP_CODEC.fieldOf("pattern").forGetter(SuspiciousEdibleRecipe::getPattern),
                EdibleBlockType.CODEC.fieldOf("result").forGetter(SuspiciousEdibleRecipe::getEdibleType),
                Codec.STRING.fieldOf("group").forGetter(SuspiciousEdibleRecipe::getGroup)
        ).apply(inst, SuspiciousEdibleRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SuspiciousEdibleRecipe> STREAM_CODEC = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC,
                SuspiciousEdibleRecipe::category,
                ShapedRecipePattern.STREAM_CODEC,
                SuspiciousEdibleRecipe::getPattern,
                ByteBufCodecs.holderRegistry(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE),
                SuspiciousEdibleRecipe::getEdibleType,
                ByteBufCodecs.STRING_UTF8,
                SuspiciousEdibleRecipe::getGroup,
                SuspiciousEdibleRecipe::new
        );

        @Override
        public MapCodec<SuspiciousEdibleRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SuspiciousEdibleRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
