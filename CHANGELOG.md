# 2.2.0 - The Texterity (and Partial Backport) Update
### Major Changes
- Reflavoured Bird of Paradise to Alstroemeria.
  - Birds of Paradise are far more linked to Jungles than Savannas, and Tropical Blues already fulfil the Savanna niche. So the change happened to make this orange flower fulfil the Savanna niche far more.
  - There is backwards compatibility implemented for converting blocks, items, and items on vanilla block entities and entities.
  - Cross-mod backwards compatibility may be impossible, so please make sure to put any Bird of Paradise items away from modded (block) entities, and to allow players a free cheat card if they don't do this.
  - Language entries may be inaccurate after this change, but they were already so *shrug*.
- Updated the model of the Freesia and Alstroemeria Mooblooms to match the upcoming Warm cow variant's model.
- Updated the model of the Lingholm and Snowdrop Mooblooms to match the upcoming Cold cow variant's model.
- Updated the model of the Limelight and Nightshade Mooblooms to more closely resemble a Zebu.
- Upddated the textures of every Moobloom added by the mod to be closer to the upcoming 1.21.5 cow variant textures.
- Updated the textures of certain Moobloom linked blocks/items (Flowers, Cupcakes, Flower Crowns) to match the new variant textures' colors.
- Updated Lingholm flower texture.

### Minor Changes
- Trees are no longer blocked from generating where Ranches generate. This will hopefully make the surroundings of Ranch structures less bare.
- Mooblooms' variant may now be set with the `variant` NBT tag. This tag follows the same rules as the Fabric/NeoForge attacHment.
- Updated Nightshade text colors.

### Bugfixes
- Fixed Mooblooms not spawning in Buttercup or Pink Daisy ranches due to a missing template pool.
- Fixed special recipes causing an exception and returning items whilst crafting.
- Fixed fallback textures always displaying when no conditions are specified.
- Fixed an exception being thrown on startup, not allowing options.txt to be respected on NeoForge. ([#9](https://github.com/GreenhouseModding/bovines-and-buttercups/issues/9))
- Fixed Fabric platform helper initializing too early, causing conflicts with mods that implement non-loader Mixin Extras versions. ([#10](https://github.com/GreenhouseModding/bovines-and-buttercups/issues/10))

### Datapacking Changes
- Deprecated old model type ids, these will be removed when the mod updates to 1.21.5 or above.
  - `bovinesandbuttercups:default` -> `bovinesandbuttercups:temperate`
  - Not Applicable -> `bovinesandbuttercups:warm` (Used by Freesia and Alstroemeria)
  - Not Applicable -> `bovinesandbuttercups:cold` (Used by Lingholm and Snowdrop)
  - Not Applicable -> `bovinesandbuttercups:lush` (Used by Limelight and Nightshade)
  - `bovinesandbuttercups:flat` -> `sbovinesandbuttercups:sculk` (Used by Sombercup)
  - `bovinesandbuttercups:buffalo` -> scheduled for removal but kept in for old packs (use `bovinesandbuttercups:warm` instead).
  - `bovinesandbuttercups:highland` -> scheduled for removal but kept in for old packs (use `bovinesandbuttercups:cold` instead).
  - `bovinesandbuttercups:ox` -> scheduled for removal but kept in for old packs (use `bovinesandbuttercups:cold` instead).