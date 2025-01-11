plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("de.mannodermaus.android-junit5")
    id("jacoco")
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
    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    // AndroidX and Material Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // UI Components
    implementation(libs.cardview)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.lifecycle.viewmodel.android)

    // Unit Testing Dependencies
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    androidTestImplementation(libs.junit.jupiter) // JUnit 5 API for tests
    testRuntimeOnly(libs.junit.jupiter.engine) // JUnit 5 engine for running tests
    testImplementation(libs.junit.vintage.engine) // Optional, allows running JUnit 4 tests on JUnit 5 platform
    testImplementation(libs.robolectric)

    // Android Instrumentation Testing Dependencies
    androidTestImplementation(libs.ext.junit) // Android-specific JUnit 4
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.archCoreTesting)
    implementation(libs.threetenabp)// Espresso for UI testing
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.gson)
    implementation(libs.glide)
    annotationProcessor (libs.compiler)
    testImplementation(libs.robolectric.v4103)

}