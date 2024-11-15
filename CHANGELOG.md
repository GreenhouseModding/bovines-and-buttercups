## 2.0.0 - Sweet and Savory
2.0.0 is a total rewrite of the mod, both content-wise and datapacking-wise.

Thank you to [GaeaKat](https://github.com/GaeaKat), [TathanDev](https://github.com/TathanDev) and [ferriarnus](https://github.com/ferriarnus) for helping with this update by submitting PRs!

Additionally, a huge thank you to [Omniv](https://bsky.app/profile/omniv.bsky.social) for contributing many textures for this update.

## Main Features
- Added a new Moobloom, the Lingholm Moobloom, a cyan flower themed after non-snowy Taiga biomes.
- Added a new cosmetic item, the Flower Crown. This is an item you may wear on your head and can craft from any combination of Moobloom flowers.
  - New Flower Crown Materials can be made through the `bovinesandbuttercups:flower_crown_material` datapack registry.
- Re-textured every flower and moobloom within the mod to give them a more modern appearance, as well as making them referential to non holstein cattle.
- Reworked bee interaction! Mooblooms when pollinated will now provide the hive the ability to create Rich Honey or provide more Honeycomb instead of what it was before.
- Added Rich Honey Bottles, a new Honey Bottle that gives more saturation when compared to the Honey Bottle and a little duration of Absorption.
- Added Rich Honey Blocks, a new Honey Block that is counted separately by pistons, and is useful for storing Rich Honey Bottles.
- Reworked interaction when feeding a Moobloom Bone Meal. They will now create a varied flower trail of their flower as they walk instead of an instant area of flowers.
  - This change was made to be more referential to Minecraft Earth as a source material.
- Added a Chargelily Ranch to guide players that you may wish to strike a Moobloom with lightning.
  - Spawns within non-snowy stony mountainous biomes.
- Modified certain vanilla advancements if there are no datapack changes to these respective advancements.
  - Balanced Diet. Added consuming Rich Honey Bottles to the requirements.
  - Sticky Situation. Allowed Rich Honey Blocks to trigger the advancement.
  - Two by Two. Added breeding Mooblooms to the requirements.
- Added Cupcakes and Mushroom Puff Pastries, new decorative blocks that can be crafted.
  - Cupcakes will grant a quarter of the duration of the lockdown from the nectar used to craft it when eaten.
  - Mushroom Puff Pastries additionally have a Suspicious variant that will grant a quarter of the suspicious stew used to craft it when eaten.
- Added new advancements.
  - Full Bloom - Breed every Moobloom that can be bred for.
  - Moo-tiful - Obtain a Flower Crown.

## Bugfixes
- Fixed data driven cow types always selecting the first type upon being struck by lightning when it should be randomised.
- Fixed an escape path within the Buttercup Ranch.
- Fixed Limelight Ranches only not being found by /locate and similar when they generate in water.

## Miscellaneous Changes
- Removed REI compat.
  - It was way too much of a hassle to set up REI compatibility with the Flower Crown item.
  - If you wish to PR support, be my guest, but I give up.
- Renamed Milky Way advancement to Dear Dairy and updated its description.
- Added animations for Mooblooms laying down and getting up when being pollinated by a bee.
- Renamed Bowl of Nectar to Nectar Bowl for consistency with vanilla.
- Tweaked Ranch Loot Table to always include a Saddle, and to roll for seeds/crops twice.
- Swapped Buttercup and Pink Daisy Moobloom Ranch structures to have their respective Mooblooms instead of the opposite one.
  - This was done for slightly more cohesion with these structures.
  - You can still figure out that the breeding blocks are a thing, just look for the particles.
- Limelight Moobloom are now bred with Cave Vines (Glow Berry Vines) instead of Moss and Moss Carpets.
- Limelight Ranches will now spawn between y 20 and y 50 rather than y 0.
- Pink Daisy Moobloom now have Pink Petals and Cherry Wood as respective options for breeding.
- Nectar Bowls will now return bowls when used in crafting.
- Nectar Bowls no longer store the effects and the Moobloom it was obtained from, instead storing a nectar object containing the effects and model.
- Prefixed certain subtitle lang entries with mod id.
- Updated /effect lockdown command to instead be found at /bovinesandbuttercups lockdown give/clear.
- Updated lockdown command related lang.

## Language Changes
- Added French language support ([#2](https://github.com/GreenhouseModding/bovines-and-buttercups/pull/2) - TathanDev).
  - This translation may be slightly out of date for specific bits of content as it was made before this update was finished.
- Added Dutch language support ([#3](https://github.com/GreenhouseModding/bovines-and-buttercups/pull/3) - ferriarnus).
  - This translation may be slightly out of date for specific bits of content as it was made before this update was finished.

## Data Packing Changes
- Bovines and Buttercups now utilises datapack/dynamic registries.
  - A side effect of this means that you can no longer reload the registry through `/reload`.
  - This change was made to better implement Bovines with vanilla systems.
- Configured Cow Types have been renamed to Cow Types, and now go inside the `data/<namespace>/bovinesandbuttercups/cow_type` directory.
- Nectar is now its own separate datapack registry, being found inside the `data/<namespace>/bovinesandbuttercups/nectar` directory.
- The Buttercup and Pink Daisy spawning biome tag is now `bovinesandbuttercups:has_moobloom/flower_forest`, instead of `bovinesandbuttercups:has_mooblooms`.
- Swapped Ranch Structure and spawning biome tags to use `c` tags where appropriate.
- `back_grass` field has been changed to `layers`. Layers are a more powerful system that allows you to overlay textures on top of Mooblooms.
  - Contains two fields. `texture_location` and `texture_modifications`.
  - Texture Modifications are a new registry that allows you to modify textures in certain ways.
    - By default it contains:
      - `bovinesandbuttercups:conditioned`, allows you to make a layer appear under a predicate.
      - `bovinesandbuttercups:emmissive`, allows you to make a texture emmissive.
      - `bovinesandbuttercups:grass_tint`, allows you to turn a texture's colour to the current biome's grass tint.
- Renamed `breeding_conditions` field to `offspring_conditions`.
- `offspring_conditions` has been rewritten from the ground up to utilise vanilla's predicate system.
  - [More information on this topic can be found here](https://minecraft.wiki/w/Predicate).
  - `offspring_conditions` may be a list of predicates that will apply to either parent; or you may specify `this_conditions` and `other_conditions` to specify conditions that must both be met by both parents with this applying to one and other applying to the other.
    - If no conditions are specified for either field, it is automatically considered true.
  - This change was made to better implement Bovines with vanilla systems.
- Removed `vanilla_spawning_hack` field. Red Mushroom Mooshrooms are now datapacked to be a natural spawn in the Mushroom Fields.
- Added `vanilla_type` field to Mooshroom Types, this will map a vanilla Mooshroom type to a Bovines and Buttercups Mooshroom type.
- Removed `dye_craft_result` field from Custom Flowers. This has been replaced with using your loader's `components` custom ingredient type and a regular recipe.
  - This change has also been applied to Suspicious Stew recipes, in preparation for future Minecraft versions.
    - For the above, you may also use the result field's `components` field.
- Custom Mushroom Types now use template pools for the `huge_structures` field.
- Removed `bovinesandbuttercups:mutation` trigger and replaced it with `bovinesandbuttercups:breed_cow_with_type`
  - Added `player` field, a predicate for the player breeding the animals.
  - Added `different_from_parents`, which determines whether breeding conditions were in play to create a different cow type.
- Modified the `bovinesandbuttercups` subfolder's format in resource packs.
- Added `bovinesandbuttercups:remainder` ingredient type. Which will return a `remainder` stack from a `base` ingredient.

#### Example Custom Recipes
For crossplatforming these, use both unique fields within the same ingredient.
<details>
<summary>Fabric</summary>

```json
{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "group": "white_dye",
  "ingredients": [
    {
      "fabric:type": "fabric:components",
      "base": {
        "item": "bovinesandbuttercups:custom_flower"
      },
      "components": {
        "bovinesandbuttercups:custom_flower": "example:cool_flower"
      }
    }
  ],
  "result": {
    "id": "minecraft:white_dye",
    "count": 1
  }
}
```
```json
{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "group": "white_dye",
  "ingredients": [
    {
      "item": "minecraft:bowl"
    },
    {
      "item": "minecraft:red_mushroom"
    },
    {
      "item": "minecraft:brown_mushroom"
    },
    {
      "fabric:type": "fabric:components",
      "base": {
        "item": "bovinesandbuttercups:custom_flower"
      },
      "components": {
        "bovinesandbuttercups:custom_flower": "example:cool_flower"
      }
    }
  ],
  "result": {
    "id": "minecraft:suspicious_stew",
    "count": 1,
    "components": {
      "minecraft:suspicious_stew_effects": [
        {
          "id": "minecraft:regeneration",
          "duration": 20
        }
      ]
    }
  }
}
```
</details>
<details>
<summary>NeoForge</summary>

```json
{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "group": "white_dye",
  "ingredients": [
    {
      "type": "neoforge:components",
      "items": [
        "bovinesandbuttercups:custom_flower"
      ],
      "components": {
        "bovinesandbuttercups:custom_flower": "example:cool_flower"
      }
    }
  ],
  "result": {
    "id": "minecraft:white_dye",
    "count": 1
  }
}
```
```json
{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "ingredients": [
    {
      "item": "minecraft:bowl"
    },
    {
      "item": "minecraft:red_mushroom"
    },
    {
      "item": "minecraft:brown_mushroom"
    },
    {
      "type": "neoforge:components",
      "items": [
        "bovinesandbuttercups:custom_flower"
      ],
      "components": {
        "bovinesandbuttercups:custom_flower": "example:cool_flower"
      }
    }
  ],
  "result": {
    "id": "minecraft:suspicious_stew",
    "count": 1,
    "components": {
      "minecraft:suspicious_stew_effects": [
        {
          "id": "minecraft:regeneration",
          "duration": 20
        }
      ]
    }
  }
}
```
</details>