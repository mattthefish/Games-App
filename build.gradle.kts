// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central repository
    }
    dependencies {

        classpath("com.google.gms:google-services:4.3.13")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.1")

        classpath("org.jacoco:org.jacoco.core:0.8.7")
    }
}

plugins {
    id("com.android.application") version "8.5.1" apply false
    id("com.android.library") version "8.5.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false

    id("org.sonarqube") version "3.3"
    id("io.gitlab.arturbosch.detekt").version("1.18.1")
}
