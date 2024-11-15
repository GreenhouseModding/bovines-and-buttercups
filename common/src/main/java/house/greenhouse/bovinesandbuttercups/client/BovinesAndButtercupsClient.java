package house.greenhouse.bovinesandbuttercups.client;

import house.greenhouse.bovinesandbuttercups.access.SimpleTextureExceptionAccess;
import house.greenhouse.bovinesandbuttercups.api.CowType;
import house.greenhouse.bovinesandbuttercups.api.CowTypeConfiguration;
import house.greenhouse.bovinesandbuttercups.client.platform.BovinesClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;

public class BovinesAndButtercupsClient {
    private static final HashSet<ResourceLocation> LOADED_COW_TEXTURES = new HashSet<>();
    private static final HashSet<ResourceLocation> FAILED_COW_TEXTURES = new HashSet<>();
    private static BovinesClientHelper clientHelper;
    private static ModelBakery modelBakery;

    public static void init(BovinesClientHelper helper) {
        clientHelper = helper;
    }

    public static ModelBakery getModelBakery() {
        return modelBakery;
    }

    public static void setModelBakery(ModelBakery modelBakery) {
        BovinesAndButtercupsClient.modelBakery = modelBakery;
    }

    public static void clearCowTextureCache() {
        LOADED_COW_TEXTURES.clear();
        FAILED_COW_TEXTURES.clear();
    }

    public static ResourceLocation getCachedTextures(Holder<CowType<?>> cowType, ResourceLocation original) {
        if (cowType.value().configuration().settings() == null || cowType.value().type().defaultConfig().settings() == null)
            return original;
        ResourceLocation remappedLocation = getTextureFromCowType(cowType.value().configuration(), cowType.value().type().fallbackTexturePath(), cowType.unwrapKey().orElse(cowType.value().type().defaultKey()).location());

        if (LOADED_COW_TEXTURES.contains(remappedLocation))
            return remappedLocation;
        if (FAILED_COW_TEXTURES.contains(remappedLocation)) {
            if (cowType.value().type().defaultConfig().settings() == null)
                return original;

            return getTextureFromCowType(cowType.value().type().defaultConfig(), cowType.value().type().fallbackTexturePath(), cowType.value().type().defaultKey().location());
        }

        if (!((SimpleTextureExceptionAccess)Minecraft.getInstance().getTextureManager().getTexture(remappedLocation)).bovinesandbuttercups$causedException()) {
            LOADED_COW_TEXTURES.add(remappedLocation);
            return remappedLocation;
        }
        else
            FAILED_COW_TEXTURES.add(remappedLocation);

        return getTextureFromCowType(cowType.value().type().defaultConfig(), cowType.value().type().fallbackTexturePath(), cowType.value().type().defaultKey().location());
    }

    private static ResourceLocation getTextureFromCowType(CowTypeConfiguration configuration, String fallbackTexturePath, ResourceLocation originalLocation) {
        return configuration.settings().cowTexture().map(texture -> texture.withPath(s -> "textures/entity/" + s + ".png")).orElseGet(() -> originalLocation.withPath(str -> "textures/entity/" + fallbackTexturePath.replace("%s", str) + ".png"));
    }

    public static BovinesClientHelper getHelper() {
        return clientHelper;
    }
}
