plugins {
    id("setup")
    id("com.undefinedcreations.echo")
}

dependencies {
    echo("1.21.8", printDebug = true)
    compileOnly(project(":common"))
}

tasks {
    remap {
        minecraftVersion("1.21.8")
    }
}