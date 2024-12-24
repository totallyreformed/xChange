plugins {
    alias(libs.plugins.android.application) apply false
}
// Top-level build.gradle.kts
buildscript {
    dependencies {
        classpath(libs.org.jacoco.core)
        classpath(libs.google.services)
        classpath(libs.android.junit5)
    }
    repositories{
        google()
        mavenCentral()
    }
}