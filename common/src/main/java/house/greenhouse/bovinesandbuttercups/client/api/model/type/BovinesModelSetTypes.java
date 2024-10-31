package house.greenhouse.bovinesandbuttercups.client.api.model.type;

import house.greenhouse.bovinesandbuttercups.BovinesAndButtercups;
import house.greenhouse.bovinesandbuttercups.client.api.model.BovinesModelSetRegistry;
import house.greenhouse.bovinesandbuttercups.content.block.BovinesBlocks;

public class BovinesModelSetTypes {
    public static final BovinesModelSetType GENERIC = BovinesModelSetRegistry.registerType(BovinesAndButtercups.asResource("generic"), GenericBovinesModelSetType.INSTANCE);
    public static final BovinesModelSetType ITEM = BovinesModelSetRegistry.registerType(BovinesAndButtercups.asResource("item"), InventoryBovinesModelSetType.INSTANCE);

    public static final BovinesModelSetType FLOWER = BovinesModelSetRegistry.registerType(BovinesAndButtercups.asResource("flower"), new StateDefinitionBovinesModelSetType(BovinesBlocks.CUSTOM_FLOWER.getStateDefinition()));
    public static final BovinesModelSetType MUSHROOM = BovinesModelSetRegistry.registerType(BovinesAndButtercups.asResource("mushroom"), new StateDefinitionBovinesModelSetType(BovinesBlocks.CUSTOM_MUSHROOM.getStateDefinition()));
    public static final BovinesModelSetType POTTED_FLOWER = BovinesModelSetRegistry.registerType(BovinesAndButtercups.asResource("potted_flower"), new StateDefinitionBovinesModelSetType(BovinesBlocks.POTTED_CUSTOM_FLOWER.getStateDefinition()));
    public static final BovinesModelSetType POTTED_MUSHROOM = BovinesModelSetRegistry.registerType(BovinesAndButtercups.asResource("potted_mushroom"), new StateDefinitionBovinesModelSetType(BovinesBlocks.POTTED_CUSTOM_MUSHROOM.getStateDefinition()));
    public static final BovinesModelSetType MUSHROOM_BLOCK = BovinesModelSetRegistry.registerType(BovinesAndButtercups.asResource("mushroom_block"), new StateDefinitionBovinesModelSetType(BovinesBlocks.CUSTOM_MUSHROOM_BLOCK.getStateDefinition()));
    // public static final BovinesModelSetType CUPCAKE = BovinesModelStateTypeRegistry.register(BovinesAndButtercups.asResource("placeable_edible"), BovinesBlocks.CUSTOM_MUSHROOM_BLOCK.getStateDefinition()); // TODO

    public static void init() {

    }
}
