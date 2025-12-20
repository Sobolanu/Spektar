plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    // Firebase:
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
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
    val composeUiVersion = "1.7.8"
    val viewModelVersion = "2.10.0"
    
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Coil:
    implementation("io.coil-kt:coil-compose:2.7.0")
    // Material3 and Icons:
    implementation("androidx.compose.material:material-icons-extended-android:${composeUiVersion}")

    // ViewModels:
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${viewModelVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${viewModelVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${viewModelVersion}")
    implementation("androidx.compose.runtime:runtime-livedata:${composeUiVersion}")

    // Firebase imports:
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
}