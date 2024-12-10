import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


plugins {
    `java-library`
    java
    `maven-publish`
    kotlin("jvm") version "1.9.22"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

apply(plugin = "maven-publish")

val versionVar = "0.0.1"
val groupIdVar = "com.undefined"

group = "com.undefined"
version = "0.0.1"

allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = groupIdVar
    version = versionVar

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.io/repository/nms/")
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    }

}

dependencies {
    implementation(project(":common"))
    implementation(project(":v1_21_3:", "reobf"))
    implementation(project(":api"))
}

tasks {
    assemble {
        dependsOn("shadowJar")
    }

    jar.configure {
        dependsOn("shadowJar")
        archiveClassifier.set("dev")
    }

    withType<ShadowJar> {
        archiveClassifier.set("mapped")
        archiveFileName.set("${project.name}-${project.version}.jar")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}


kotlin {
    jvmToolchain(21)
}