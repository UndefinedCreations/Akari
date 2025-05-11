import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("setup")
    id("com.undefinedcreations.nova") version "0.0.5"
    id("com.gradleup.shadow")
}

repositories {
    maven {
        name = "undefined-repo"
        url = uri("https://repo.undefinedcreations.com/releases")
    }
}

dependencies {
    compileOnly(libs.spigot)

    implementation("com.undefined:stellar:1.0.0")
    implementation("com.undefined:lynx:0.0.12:core")
    implementation("com.undefined:lynx:0.0.12:logger")
    implementation("com.undefined:lynx:0.0.12:scheduler")

    implementation(project(":common"))
    implementation(project(":api"))
    implementation(project(":nms:v1_21_5"))

    implementation("net.kyori:adventure-api:4.17.0")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.20")
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }
    compileJava {
        options.release = 21
    }
    runServer {
        minecraftVersion("1.21.5")
        acceptMojangEula()
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}