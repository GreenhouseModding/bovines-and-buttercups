//package house.greenhouse.bovinesandbuttercups.integration.jei;
//
//import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
//import house.greenhouse.bovinesandbuttercups.content.recipe.FlowerCrownRecipe;
//import house.greenhouse.bovinesandbuttercups.content.recipe.SuspiciousEdibleRecipe;
//import house.greenhouse.bovinesandbuttercups.integration.jei.recipe.FlowerCrownJeiRecipe;
//import house.greenhouse.bovinesandbuttercups.integration.jei.recipe.SuspiciousEdibleJeiRecipe;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.JeiPlugin;
//import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
//import net.minecraft.resources.ResourceLocation;
//
//@JeiPlugin
//public class BovinesJeiPlugin implements IModPlugin {
//    @Override
//    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
//        registration.getCraftingCategory().addExtension(FlowerCrownRecipe.class, new FlowerCrownJeiRecipe());
//        registration.getCraftingCategory().addExtension(SuspiciousEdibleRecipe.class, new SuspiciousEdibleJeiRecipe());
//    }
//
//    @Override
//    public ResourceLocation getPluginUid() {
//        return BovinesAndButtercups.asResource("jei_plugin");
//    }
//}
