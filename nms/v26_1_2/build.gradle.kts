plugins {
    id("setup")
    id("io.papermc.paperweight.userdev")
}

dependencies {
    paperweight.paperDevBundle("26.1.2.build.+")
    compileOnly(project(":common"))
}