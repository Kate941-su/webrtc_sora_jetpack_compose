// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    val kotlinVersion = "1.8.0"

    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
//        maven("https://kotlin.bintray.com/kotlinx")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        // START: FlutterFire Configuration
        classpath("com.google.gms:google-services:4.3.15")
        // END: FlutterFire Configuration
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}
