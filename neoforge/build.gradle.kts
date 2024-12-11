import house.greenhouse.bovinesandbuttercups.gradle.Properties
import house.greenhouse.bovinesandbuttercups.gradle.Versions
import org.apache.tools.ant.filters.LineContains
import org.gradle.jvm.tasks.Jar

plugins {
    id("conventions.loader")
    id("net.neoforged.moddev")
    id("me.modmuss50.mod-publish-plugin")
}

neoForge {
    version = Versions.NEOFORGE
    parchment {
        minecraftVersion = Versions.PARCHMENT_MINECRAFT
        mappingsVersion = Versions.PARCHMENT
    }
    addModdingDependenciesTo(sourceSets["test"])

    val at = project(":common").file("src/main/resources/${Properties.MOD_ID}.cfg")
    if (at.exists())
        setAccessTransformers(at)
    validateAccessTransformers = true

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("forge.logging.console.level", "debug")
            systemProperty("neoforge.enabledGameTestNamespaces", Properties.MOD_ID)
        }
        create("client") {
            client()
            gameDirectory.set(file("runs/client"))
            sourceSet = sourceSets["test"]
            jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
        }
        create("server") {
            server()
            gameDirectory.set(file("runs/server"))
            programArgument("--nogui")
            sourceSet = sourceSets["test"]
            jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
        }
    }

    mods {
        register(Properties.MOD_ID) {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["test"])
        }
    }
}

repositories {
    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven("https://maven.wispforest.io/releases")
    maven("https://maven.su5ed.dev/releases")
    maven("https://maven.fabricmc.net")
    maven("https://maven.shedaniel.me/")
    maven {
        name = "OctoStudios"
        url = uri("https://maven.octo-studios.com/releases")
    }
}

dependencies {
////TODO: Re-enable when updated.
//    // Recipe Viewer Mods
//    compileOnly("mezz.jei:jei-${Versions.MINECRAFT}-neoforge-api:${Versions.JEI}")
//    // runtimeOnly("mezz.jei:jei-${Versions.MINECRAFT}-neoforge:${Versions.JEI}")
//    compileOnly("dev.emi:emi-neoforge:${Versions.EMI}:api")
//    runtimeOnly("dev.emi:emi-neoforge:${Versions.EMI}")
//    // Equipment Mods
//    compileOnly("io.wispforest:accessories-neoforge:${Versions.ACCESSORIES}")
//    // runtimeOnly("io.wispforest:accessories-neoforge:${Versions.ACCESSORIES}")
//    compileOnly("top.theillusivec4.curios:curios-neoforge:${Versions.CURIOS}")
//    // runtimeOnly("top.theillusivec4.curios:curios-neoforge:${Versions.CURIOS}")
}

tasks {
    named<ProcessResources>("processResources").configure {
        filesMatching("*.mixins.json") {
            filter<LineContains>("negate" to true, "contains" to setOf("refmap"))
        }
    }
}

publishMods {
    file.set(tasks.named<Jar>("jar").get().archiveFile)
    modLoaders.add("neoforge")
    changelog = rootProject.file("CHANGELOG.md").readText()
    version = "${Versions.MOD}+${Versions.MINECRAFT}-neoforge"
    displayName = "v${Versions.MOD} (NeoForge ${Versions.MINECRAFT})"
    type = STABLE

    curseforge {
        projectId = Properties.CURSEFORGE_PROJECT_ID
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")

        minecraftVersions.add(Versions.MINECRAFT)
        javaVersions.add(JavaVersion.VERSION_21)

        clientRequired = true
        serverRequired = true

        optional("jei")
        optional("emi")
        optional("accessories")
        optional("curios-continuation")
    }

    modrinth {
        projectId = Properties.MODRINTH_PROJECT_ID
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")

        minecraftVersions.add(Versions.MINECRAFT)

        optional("jei")
        optional("emi")
        optional("accessories")
        optional("curios-continuation")
    }

    github {
        accessToken = providers.environmentVariable("GITHUB_TOKEN")
        parent(project(":common").tasks.named("publishGithub"))
    }
}