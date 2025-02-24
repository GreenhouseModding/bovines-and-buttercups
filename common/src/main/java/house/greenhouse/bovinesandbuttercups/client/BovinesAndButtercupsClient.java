package house.greenhouse.bovinesandbuttercups.client;

import house.greenhouse.bovinesandbuttercups.api.BaseCowConfiguration;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.client.api.RenderStateObject;
import house.greenhouse.bovinesandbuttercups.client.platform.BovinesClientHelper;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.FlowerCrownItemRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.item.select.BovinesSelectProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;

public class BovinesAndButtercupsClient {
    private static final HashSet<ResourceLocation> LOADED_COW_TEXTURES = new HashSet<>();
    private static final HashSet<ResourceLocation> FAILED_COW_TEXTURES = new HashSet<>();
    private static BovinesClientHelper clientHelper;

    public static void init(BovinesClientHelper helper) {
        clientHelper = helper;
        RenderStateObject.registerAll();
        BovinesSelectProperties.registerAll();
    }

    public static void registerItemRenderers() {
        SpecialModelRenderers.ID_MAPPER.put(FlowerCrownItemRenderer.Unbaked.ID, FlowerCrownItemRenderer.Unbaked.MAP_CODEC);
    }

    public static void clearCowTextureCache() {
        LOADED_COW_TEXTURES.clear();
        FAILED_COW_TEXTURES.clear();
    }

    public static ResourceLocation getCachedTextures(Holder<CowVariant<?>> cowVariant, ResourceLocation original) {
        if (cowVariant.value().configuration().settings() == null || cowVariant.value().type().defaultConfig().settings() == null)
            return original;
        ResourceLocation remappedLocation = getTextureFromCowType(cowVariant.value().configuration(), cowVariant.value().type().fallbackTexturePath(), cowVariant.unwrapKey().orElse(cowVariant.value().type().defaultKey()).location());

        if (LOADED_COW_TEXTURES.contains(remappedLocation))
            return remappedLocation;
        if (FAILED_COW_TEXTURES.contains(remappedLocation)) {
            if (cowVariant.value().type().defaultConfig().settings() == null)
                return original;

            return getTextureFromCowType(cowVariant.value().type().defaultConfig(), cowVariant.value().type().fallbackTexturePath(), cowVariant.value().type().defaultKey().location());
        }

        if (!(Minecraft.getInstance().getTextureManager().getTexture(remappedLocation) instanceof ReloadableTexture reloadableTexture) || !reloadableTexture.resourceId().equals(MissingTextureAtlasSprite.getLocation())) {
            LOADED_COW_TEXTURES.add(remappedLocation);
            return remappedLocation;
        }
        else
            FAILED_COW_TEXTURES.add(remappedLocation);

        return getTextureFromCowType(cowVariant.value().type().defaultConfig(), cowVariant.value().type().fallbackTexturePath(), cowVariant.value().type().defaultKey().location());
    }

    private static ResourceLocation getTextureFromCowType(BaseCowConfiguration configuration, String fallbackTexturePath, ResourceLocation originalLocation) {
        return configuration.settings().cowTexture().map(texture -> texture.withPath(s -> "textures/entity/" + s + ".png")).orElseGet(() -> originalLocation.withPath(str -> "textures/entity/" + fallbackTexturePath.replace("%s", str) + ".png"));
    }

    public static BovinesClientHelper getHelper() {
        return clientHelper;
    }
}
