# Bovines and Buttercups
This is the repository for Bovines and Buttercups. 

To access the old pre 1.21/2.0.0 repository, you may go [here.](https://github.com/MerchantPug/bovines-and-buttercups-archive)

## Downloads
You may download the mod through any of these sources.
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bovines-and-buttercups)
- [Modrinth](https://modrinth.com/mod/bovines-and-buttercups)
- [GitHub Releases](https://github.com/GreenhouseModding/bovines-and-buttercups/releases)

## Wiki
There is currently no Wiki for Bovines and Buttercups. One is planned for the future.

Your best bet is to get the information you need off of the mod page, or if it's to do with data packs, the README for the [GreenhouseModding/bovines-and-buttercups-example-pack](https://github.com/GreenhouseModding/bovines-and-buttercups-example-pack) repository.

## Development
The best way to get Bovines and Buttercups in your developer environment is through the [Greenhouse Maven](https://repo.greenhouse.house/#/releases/house/greenhouse/bovinesandbuttercups)

### Kotlin DSL
<details>

```groovy
repositories {
    maven("https://repo.greenhouse.house/releases/") {
        name = "Greenhouse Maven"
    }
}

dependencies {
    // Depend on the Common project, for VanillaGradle and ModDevGradle.
    compileOnly("house.greenhouse:bovines-and-buttercups:${bovines_version}+${minecraft_version}-common")

    // Depend on the Fabric project, for Loom.
    modImplementation("house.greenhouse:bovines-and-buttercups:${bovines_version}+${minecraft_version}-fabric")
    
    // Depend on the NeoForge project, for ModDevGradle or NeoGradle.
    implementation("house.greenhouse:bovines-and-buttercups:${bovines_version}+${minecraft_version}-neoforge")
}
```

</details>

### Groovy DSL
<details>

```groovy
repositories {
    maven {
        name = "Greenhouse Maven"
        url = "https://repo.greenhouse.house/releases/"
    }
}

dependencies {
    // Depend on the Common project, for VanillaGradle and ModDevGradle.
    compileOnly("house.greenhouse:bovines-and-buttercups:${bovines_version}+${minecraft_version}-common")

    // Depend on the Fabric project, for Loom.
    modImplementation("house.greenhouse:bovines-and-buttercups:${bovines_version}+${minecraft_version}-fabric")
    
    // Depend on the NeoForge project, for ModDevGradle or NeoGradle.
    implementation("house.greenhouse:bovines-and-buttercups:${bovines_version}+${minecraft_version}-neoforge")
}
```

</details>
