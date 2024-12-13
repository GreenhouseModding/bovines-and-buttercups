## Changes
- Ported to 1.21.4.
- Lowered the search area for Mooblooms on bees.

## Updating Old Worlds
- To update old worlds, you will need to install Bovines and Buttercups 2.1.0+1.21.1 on your server, and have every player with a nectar join.
  - Not doing this will remove any nectars from the player's inventory.
  - This only supports the built-in nectars, any custom nectars will be converted into buttercup nectar.

## Datapacking Changes
- Removed `bovinesandbuttercups:nectar` registry.
  - This registry's functionality has been replaced with the `minecraft:consumable` data component.
  - Each nectar is now an individual item, you may edit an item's components to create your own nectars.
- Added `item_model` fields to custom flower, custom mushroom and edible block data types.
  - This will define an `items` file to use as the component for these items.