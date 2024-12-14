package house.greenhouse.bovinesandbuttercups.registry;

import com.mojang.serialization.MapCodec;
import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.api.CowTypeType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.model.CowModelType;
import house.greenhouse.bovinesandbuttercups.api.cowtype.modifier.TextureModifierFactory;
import net.minecraft.core.Registry;

public class BovinesRegistries {
    public static final Registry<CowTypeType<?>> COW_TYPE_TYPE = BovinesAndButtercups.getHelper().createRegistry(BovinesRegistryKeys.COW_TYPE_TYPE);
    public static final Registry<CowModelType> MODEL_TYPE = BovinesAndButtercups.getHelper().createRegistry(BovinesRegistryKeys.MODEL_TYPE);
    public static final Registry<MapCodec<? extends TextureModifierFactory<?>>> TEXTURE_MODIFIER = BovinesAndButtercups.getHelper().createRegistry(BovinesRegistryKeys.TEXTURE_MODIFIER);

    public static void init() {
    }

}
