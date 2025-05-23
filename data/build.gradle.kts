plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid) // Assuming libs.versions.toml is available
    alias(libs.plugins.ksp) // For Room if DAOs are here, or Hilt
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.ytone.longcare.data"
    compileSdk = libs.versions.compileSdk.get().toInt() // From libs.versions.toml

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt() // From libs.versions.toml
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.hilt)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.hilt.compiler)
    // Add other common data layer dependencies like retrofit, room-runtime, kotlinx-serialization-json
    implementation(libs.retrofit.core) // Corrected to libs.retrofit.core from previous TOML
    implementation(libs.retrofit.converter.kotlinx.serialization) // Corrected to libs.retrofit.converter.kotlinx.serialization
    implementation(libs.okhttp.core) // Corrected to libs.okhttp.core
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler) // If DAOs are defined in this module
}
