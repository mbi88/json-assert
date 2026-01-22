plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Gradle Core API (for tasks, plugins, extensions)
    implementation(gradleApi())
}