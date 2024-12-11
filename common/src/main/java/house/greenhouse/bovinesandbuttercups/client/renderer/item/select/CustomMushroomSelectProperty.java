package house.greenhouse.bovinesandbuttercups.client.renderer.item.select;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.CustomMushroomType;
import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistryKeys;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record CustomMushroomSelectProperty() implements SelectItemModelProperty<ResourceKey<CustomMushroomType>> {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("custom_mushroom");
    public static final Type<CustomMushroomSelectProperty, ResourceKey<CustomMushroomType>> TYPE = Type.create(
            MapCodec.unit(new CustomMushroomSelectProperty()), ResourceKey.codec(BovinesRegistryKeys.CUSTOM_MUSHROOM_TYPE)
    );

    @Override
    public @Nullable ResourceKey<CustomMushroomType> get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
        var holder = stack.get(BovinesDataComponents.CUSTOM_MUSHROOM);
        if (holder == null)
            return null;
        var key = holder.holder().unwrapKey();
        return key.orElse(null);
    }

    @Override
    public Type<CustomMushroomSelectProperty, ResourceKey<CustomMushroomType>> type() {
        return TYPE;
    }
}
