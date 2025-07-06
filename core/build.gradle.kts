plugins {
    id("setup")
    id("com.gradleup.shadow")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(project(":common"))
    compileOnly(project(":nms:v1_21_5"))
    compileOnly(project(":nms:v1_21_7"))
}