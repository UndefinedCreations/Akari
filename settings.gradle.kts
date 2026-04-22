rootProject.name = "akari"
include(
    ":common",
    ":core",
    ":server",
    ":nms:v1_21_5",
    ":nms:v1_21_7",
    ":nms:v1_21_8",
    ":nms:v26_1_2",
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}