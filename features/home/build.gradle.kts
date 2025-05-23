// features/home/build.gradle.kts
plugins {
    id("com.android.library")
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp) // For Hilt processor
    // alias(libs.plugins.kotlinCompose) // Assuming this still causes issues, configure compose features directly
}

android {
    namespace = "com.ytone.longcare.features.home"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":domain")) // Dependency on the domain module
    // implementation(project(":data")) // Typically feature modules depend on domain, not directly on data

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // For viewModelScope
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose BOM and libraries
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Hilt for DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // Hilt KSP compiler
}
