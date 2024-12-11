package house.greenhouse.bovinesandbuttercups.client.renderer.item.select;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.CustomFlowerType;
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

public record CustomFlowerSelectProperty() implements SelectItemModelProperty<ResourceKey<CustomFlowerType>> {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("custom_flower");
    public static final SelectItemModelProperty.Type<CustomFlowerSelectProperty, ResourceKey<CustomFlowerType>> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(new CustomFlowerSelectProperty()), ResourceKey.codec(BovinesRegistryKeys.CUSTOM_FLOWER_TYPE)
    );

    @Override
    public @Nullable ResourceKey<CustomFlowerType> get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
        var holder = stack.get(BovinesDataComponents.CUSTOM_FLOWER);
        if (holder == null)
            return null;
        var key = holder.holder().unwrapKey();
        return key.orElse(null);
    }

    @Override
    public Type<CustomFlowerSelectProperty, ResourceKey<CustomFlowerType>> type() {
        return TYPE;
    }
}
