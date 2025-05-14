import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("setup")
    id("com.gradleup.shadow")
    `maven-publish`
    id("org.jetbrains.dokka") version "2.0.0"
}

dependencies {
    compileOnly(libs.spigot)

    api(project(":core"))
    api(project(":common"))
    api(project(":nms:v1_21_5"))

    dokkaPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:2.0.0")
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
    tasks.withType<DokkaTask>()

    tasks.register<Jar>("sourceJar") {
        archiveClassifier = "sources"
        from(sourceSets.main.get().allSource)
    }
}

val packageJavadoc by tasks.registering(Jar::class) {
    group = "akari"
    archiveClassifier = "javadoc"

    dependsOn(tasks.dokkaJavadocCollector)
    from(tasks.dokkaJavadocCollector.flatMap { it.outputDirectory })
}

val packageSources by tasks.registering(Jar::class) {
    group = "akari"
    archiveClassifier = "sources"
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(subprojects.filter { it.subprojects.isEmpty() }.map { it.sourceSets.main.get().allSource })
}

publishing {
    publications {
        create<MavenPublication>("kotlin") {
            artifactId = rootProject.name
            from(components["shadow"])
            artifact(packageJavadoc)
            artifact(packageSources)

            pom {
                name = "Lynx"
                description = "A general purpose API for Java and Kotlin."
                url = "https://www.github.com/UndefinedCreations/Lynx"
                licenses {
                    license {
                        name = "MIT"
                        url = "https://mit-license.org/"
                        distribution = "https://mit-license.org/"
                    }
                }
                developers {
                    developer {
                        id = "lars"
                        name = "Larskrs"
                        url = "https://github.com/larskrs/"
                    }
                    developer {
                        id = "redmagic"
                        name = "TheRedMagic"
                        url = "https://github.com/TheRedMagic/"
                    }
                    developer {
                        id = "lutto"
                        name = "StillLutto"
                        url = "https://github.com/StillLutto/"
                    }
                }
                scm {
                    url = "https://github.com/UndefinedCreations/Lynx/"
                    connection = "scm:git:git://github.com/UndefinedCreations/Lynx.git"
                    developerConnection = "scm:git:ssh://git@github.com/UndefinedCreations/Lynx.git"
                }
            }
        }
    }
    repositories {
        maven {
            name = "undefined-repo"
            url = uri("https://repo.undefinedcreations.com/releases")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME") ?: property("mavenUser").toString()
                password = System.getenv("MAVEN_SECRET") ?: property("mavenPassword").toString()
            }
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    shadowJar {
        archiveClassifier = ""
    }
}
