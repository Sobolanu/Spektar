plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    // Firebase:
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.spektar"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.spektar"
        minSdk = 26
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

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    val composeUi_version = "1.7.8"
    val viewModel_version = "2.10.0"
    val room_version = "2.8.4"
    val supabase_version = "3.3.0"
    val ktor_version = "3.3.3"
    val datastore_version = "1.2.0"

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
    implementation("androidx.compose.material:material-icons-extended-android:${composeUi_version}")

    // ViewModels:
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${viewModel_version}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${viewModel_version}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${viewModel_version}")
    implementation("androidx.compose.runtime:runtime-livedata:${composeUi_version}")

    // Supabase imports:
    implementation(platform("io.github.jan-tennert.supabase:bom:$supabase_version"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:$ktor_version")

    // ROOM database:
    implementation("androidx.room:room-ktx:${room_version}")
    ksp("androidx.room:room-compiler:${room_version}")

    implementation("androidx.datastore:datastore-preferences:${datastore_version}")
}