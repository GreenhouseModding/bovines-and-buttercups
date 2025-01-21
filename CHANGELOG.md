## Changes
- Added Camellia Mooblooms, a new magenta Moobloom bred whilst near Cherry Grove blocks.
  - Pink Petals and Cherry Wood has been removed from the Pink Daisy breeding table to account for this change.
- Added Sombercup Mooblooms, a new alien Moobloom converted when activating a Sculk Catalyst with a Moobloom nearby.
- Bovines and Buttercups advancements are no longer hidden.
- The Full Bloom advancement now grants 100 experience upon completion.
- The Full Bloom advancement no longer requires a Buttercup, Chargelily or Pink Daisy Moobloom.
- Updated multiple moobloom models to better match the cow they're referencing.
  - This affects...
    - Freesia
    - Lingholm
    - Snowdrop
- Added modded DFU for contents of this mod, this will be used in the update to 1.21.4.
- Updated Pink Daisy Nectar Bowl texture to distinguish it from the new Camellia Nectar Bowl.
- Lowered the search area for Mooblooms on bees.

## Bugfixes
- Fixed potential ConcurrentModificationException when applying nectar.
- Fixed certain ranch structures having poorly placed fence gates. This affects:
  - Lingholm Ranches
  - Freesia Ranches
- Fixed NeoForge structure based Mooblooms spawning in as the missing moobloom type.

## Datapacking Changes
- Renamed `cow_type` directory to `cow_variant`.
- Renamed `cow_type_type` registry to `cow_type`.
- There's been a few renames across the board to accommodate these changes.
  - `bovinesandbuttercups:breed_cow_with_type` advancement trigger -> `bovinesandbuttercups:breed_cow_with_variant`
    - `cow_type_type` -> `type`
    - `cow_types` -> `variants`
  - `bovinesandbuttercups:cow` subpredicate.
    - `cow_type` -> `variant`