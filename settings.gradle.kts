pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/org/jetbrains/kotlin/kotlin-compose-compiler-plugin/2.0.0-RC2-200/") }
    }
}

rootProject.name = "LocationLog"
include(":app")
 