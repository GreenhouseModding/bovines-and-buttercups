package house.greenhouse.bovinesandbuttercups.api.cowtype.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.registry.BovinesRegistries;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public abstract class TextureModifierFactory<T extends TextureModifier> {

    public static final Codec<TextureModifierFactory<?>> CODEC = BovinesRegistries.TEXTURE_MODIFIER.byNameCodec().dispatch(TextureModifierFactory::codec, Function.identity());

    private T provider = null;

    /**
     * Gets or creates a {@link TextureModifier}.
     * Be careful to not call this on the server, as client only classes may
     * be referenced in the provider.
     * @return A TextureModifier.
     */
    public final T getOrCreateProvider() {
        if (provider == null)
            provider = createProvider();
        return provider;
    }

    /**
     * Initialises this texture modifier factory. Called on both the server and the client.
     * @param entity    The cow that this texture modifier has been attached to.
     */
    public void init(Entity entity) {}

    public void tick(Entity entity) {}

    protected abstract T createProvider();

    public abstract MapCodec<? extends TextureModifierFactory<?>> codec();
}
