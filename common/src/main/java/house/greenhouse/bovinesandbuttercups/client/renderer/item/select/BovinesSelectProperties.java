package house.greenhouse.bovinesandbuttercups.client.renderer.item.select;

import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;

public class BovinesSelectProperties {
    public static void registerAll() {
        SelectItemModelProperties.ID_MAPPER.put(CustomFlowerSelectProperty.ID, CustomFlowerSelectProperty.TYPE);
        SelectItemModelProperties.ID_MAPPER.put(CustomMushroomSelectProperty.ID, CustomMushroomSelectProperty.TYPE);
        SelectItemModelProperties.ID_MAPPER.put(EdibleBlockSelectProperty.ID, EdibleBlockSelectProperty.TYPE);
    }
}