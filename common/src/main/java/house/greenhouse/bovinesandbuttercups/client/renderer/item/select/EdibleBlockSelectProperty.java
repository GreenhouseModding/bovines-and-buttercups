package house.greenhouse.bovinesandbuttercups.client.renderer.item.select;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.block.EdibleBlockType;
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

public record EdibleBlockSelectProperty() implements SelectItemModelProperty<ResourceKey<EdibleBlockType>> {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("edible");
    public static final Type<EdibleBlockSelectProperty, ResourceKey<EdibleBlockType>> TYPE = Type.create(
            MapCodec.unit(new EdibleBlockSelectProperty()), ResourceKey.codec(BovinesRegistryKeys.EDIBLE_BLOCK_TYPE)
    );

    @Override
    public @Nullable ResourceKey<EdibleBlockType> get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
        var holder = stack.get(BovinesDataComponents.EDIBLE_TYPE);
        if (holder == null)
            return null;
        var key = holder.holder().unwrapKey();
        return key.orElse(null);
    }

    @Override
    public Type<EdibleBlockSelectProperty, ResourceKey<EdibleBlockType>> type() {
        return TYPE;
    }
}
