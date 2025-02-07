# 2.2.0 - The Texturous (and Partial Backport) Update
### Major Changes
- Updated the model of the Freesia and Bird of Paradise Mooblooms to match the upcoming Warm cow variant's model.
- Updated the model of the Lingholm and Snowdrop Mooblooms to match the upcoming Cold cow variant's model.
- Updated the model of the Limelight and Nightshade Mooblooms to more closely resemble a Zebu.
- Refreshed the textures of every Moobloom added by the mod to be closer to the upcoming 1.21.5 cow variant textures.

### Minor Changes
- Trees are no longer blocked from generating where Ranches generate. This will hopefully make the surroundings of Ranch structures less bare.
- Moobloom's variant may now be set with the `variant` NBT tag. This tag follows the same rules as the Fabric/NeoForge attacHment.

### Bugfixes
- Fixed Mooblooms not spawning in Buttercup or Pink Daisy ranches due to a missing template pool.

### Datapacking Changes
- Deprecated old model type ids, these will be removed when the mod updates to 1.21.5 or above.
  - A list of the model type ids can be found below;
  - `bovinesandbuttercups:default` -> `bovinesandbuttercups:temperate`
  - Not Applicable -> `bovinesandbuttercups:warm`
  - Not Applicable -> `bovinesandbuttercups:cold`
  - Not Applicable -> `bovinesandbuttercups:lush`
  - `bovinesandbuttercups:flat` -> `sbovinesandbuttercups:sculk`
  - `bovinesandbuttercups:buffalo` -> scheduled for removal but kept in for old packs (use `bovinesandbuttercups:warm` instead).
  - `bovinesandbuttercups:highland` -> scheduled for removal but kept in for old packs (use `bovinesandbuttercups:cold` instead).
  - `bovinesandbuttercups:ox` -> scheduled for removal but kept in for old packs (use `bovinesandbuttercups:cold` instead).