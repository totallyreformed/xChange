plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("de.mannodermaus.android-junit5")
}

android {
    namespace = "com.example.xchange"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.xchange"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    // AndroidX and Material Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Google Play Services Maps
    implementation(libs.play.services.maps)

    // Firebase Dependencies
    implementation(platform(libs.firebase.bom)) // Import Firebase BoM (Bill of Materials)
    implementation(platform(libs.firebase.bom.v2611))
    implementation(libs.firebase.database) // Realtime Database
    implementation(libs.google.firebase.analytics) // Firebase Analytics

    // UI Components
    implementation(libs.cardview)

    // Unit Testing Dependencies
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter) // JUnit 5 API for tests
    testRuntimeOnly(libs.junit.jupiter.engine) // JUnit 5 engine for running tests
    testImplementation(libs.junit.vintage.engine) // Optional, allows running JUnit 4 tests on JUnit 5 platform

    // Android Instrumentation Testing Dependencies
    androidTestImplementation(libs.ext.junit) // Android-specific JUnit 4
    androidTestImplementation(libs.espresso.core) // Espresso for UI testing
}

