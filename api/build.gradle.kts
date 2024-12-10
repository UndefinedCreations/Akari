plugins {
    kotlin("jvm") version "1.9.22"
}

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":v1_21_3"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}

kotlin {
    jvmToolchain(21)
}