plugins {
    kotlin("jvm") version "1.9.22"
    id("xyz.jpenilla.run-paper") version "2.2.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    maven("https://repo.undefinedcreation.com/stellar")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT")
    implementation(project(":common"))
    implementation(project(":v1_21_3:", "reobf"))
    implementation(project(":api"))
    implementation("com.undefined:stellar:0.0.1:mapped")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }


    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }

    runServer {
        minecraftVersion("1.21.3")
        jvmArgs("-Xmx2G")
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}