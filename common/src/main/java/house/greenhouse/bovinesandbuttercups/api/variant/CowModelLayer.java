package house.greenhouse.bovinesandbuttercups.api.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.bovinesandbuttercups.api.variant.modifier.TextureModifierFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Objects;

public record CowModelLayer(ResourceLocation textureLocation, List<TextureModifierFactory<?>> textureModifiers) {
    public static final Codec<CowModelLayer> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("texture_location").forGetter(CowModelLayer::textureLocation),
            TextureModifierFactory.CODEC.listOf().optionalFieldOf("texture_modifiers", List.of()).forGetter(CowModelLayer::textureModifiers)
    ).apply(inst, CowModelLayer::new));

    public void tickTextureModifiers(Entity entity) {
        textureModifiers.forEach(textureModifierFactory -> textureModifierFactory.tick(entity));
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof CowModelLayer other))
            return false;

        return other.textureLocation.equals(textureLocation) && other.textureModifiers.equals(textureModifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textureLocation, textureModifiers);
    }
}
