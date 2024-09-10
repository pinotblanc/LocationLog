// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.ktx) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.gradle.secrets) apply false
}

buildscript {

    repositories {

        google()
        mavenCentral()
    }

    dependencies {

        // to hide api keys in local.properties
        classpath(libs.secrets.gradle.plugin)
    }
}