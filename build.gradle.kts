import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.mia.copyjar)
    id("xyz.jpenilla.run-paper") version "2.2.0" // Adds runServer and runMojangMappedServer tasks for testing
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id ("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("net.minecrell.plugin-yml.paper") version "0.6.0" // Generates plugin.yml
    id("io.papermc.paperweight.userdev") version "1.5.11" // NMS
}

val pluginVersion: String by project
group = "com.boy0000"
version = pluginVersion

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.mineinabyss.com/releases")
    maven("https://repo.dmulloy2.net/repository/public/")
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT") //NMS
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.kaml)

    implementation(libs.idofront.di)
    implementation(libs.idofront.commands)
    implementation(libs.idofront.config)
    implementation(libs.idofront.text.components)
    implementation(libs.idofront.logging)
    implementation(libs.idofront.serializers)
    implementation(libs.idofront.util)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.14.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.14.0")
}

tasks {

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        filteringCharset = Charsets.UTF_8.name()
    }

    runServer {
        minecraftVersion("1.20.4")
    }

    build {
        dependsOn(shadowJar)
    }

    /*
    reobfJar {
      // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
      // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
      outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
    }
     */
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


paper {
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = "com.boy0000.xray.XrayPlugin"
    version = "${project.version}"
    apiVersion = "1.20"
    authors = listOf("Author")
    //commands.create("xray")
}
