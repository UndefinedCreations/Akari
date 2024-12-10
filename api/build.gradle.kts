plugins {
    kotlin("jvm") version "1.9.22"
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16-R0.1-SNAPSHOT")
    compileOnly(project(":common"))
    compileOnly(project(":api"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}

kotlin {
    jvmToolchain(21)
}