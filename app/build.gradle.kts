plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    // Firebase:
    id("com.google.gms.google-services")

    // KSP:
    id("com.google.devtools.ksp")

    // Hilt:
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.spektar"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.spektar"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    val nav_version = "2.9.6"
    val compose_ui_version = "1.7.8"
    val view_model_version = "2.8.6"

    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Coil:
    implementation("io.coil-kt:coil-compose:2.4.0")
    // Material3 Icons:
    implementation("androidx.compose.material:material-icons-extended-android:${compose_ui_version}")

    // ViewModels:
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${view_model_version}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${view_model_version}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${view_model_version}")
    implementation("androidx.compose.runtime:runtime-livedata:${compose_ui_version}")

    // Firebase imports:
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")

    // Hilt:
    implementation("com.google.dagger:hilt-android:2.57.1")
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")
}