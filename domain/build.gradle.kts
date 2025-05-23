// domain/build.gradle.kts
plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    // For @javax.inject.Inject annotation if Hilt is used for use case constructor injection
    // This typically comes from dagger or hilt-core.
    // Given libs.versions.toml, hilt-android provides javax.inject.
    implementation(libs.hilt.android)
}
