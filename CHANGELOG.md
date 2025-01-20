## Changes
- Ported to 1.21.4.
- Lowered the search area for Mooblooms on bees.
- Bovines and Buttercups advancements are no longer hidden.
- Removed Buttercup, Chargelily and Pink Daisy from Full Bloom advancement's requirements.

## Updating Old Worlds
- If you are coming from 1.21.1
  - When updating from old worlds, Nectars will be changed to a new item ID based object, any custom nectar bowls will be converted into Buttercup Nectar Bowls.
  - There may be better options for Custom Nectar DFU in the future.
  
## Datapacking Changes
- Removed the `bovinesandbuttercups:nectar` registry.
  - This registry's functionality has been replaced with the `minecraft:consumable` data component.
  - Each nectar is now an individual item, you may edit an item's components to create your own nectars.
- Added `item_model` fields to custom flower, custom mushroom and edible block data types.
  - This will define an `items` file to use as the component for these items.