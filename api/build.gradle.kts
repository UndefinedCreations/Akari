plugins {
    id("setup")
    id("com.gradleup.shadow")
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(project(":common"))
    compileOnly(project(":nms:v1_21_5"))

    implementation("com.undefined:stellar:1.0.0")
    implementation("com.undefined:lynx:0.0.12:core")
    implementation("com.undefined:lynx:0.0.12:logger")
    implementation("com.undefined:lynx:0.0.12:scheduler")
}