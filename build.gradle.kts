// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Root project build.gradle.kts
// Root project build.gradle.kts
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    // kotlinCompose is NOT applied here.
    alias(libs.plugins.hilt) apply false          // For version management and potential buildscript use
    alias(libs.plugins.ksp) apply false           // For version management
    alias(libs.plugins.ktlint) apply false        // For project-wide application or version management
    // Plugins like kotlinSerialization, room, firebasePerf will be applied in the app module.
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}