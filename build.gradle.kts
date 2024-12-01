// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}
// Top-level build.gradle.kts
buildscript {
    dependencies {
        // Google services classpath
        classpath(libs.google.services)
        classpath(libs.android.junit5)
    }
    repositories{
        google()
        mavenCentral()
    }
}


