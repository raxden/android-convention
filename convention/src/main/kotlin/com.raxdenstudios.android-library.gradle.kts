import extension.androidConfig
import extension.proguardConfig
import extension.testsConfig
import gradle.kotlin.dsl.accessors._262d95018d1666636cc93fe101450074.kotlin

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {

    androidConfig(project)
    proguardConfig()
    testsConfig()

    kotlin {
        jvmToolchain(AppConfig.javaLanguageVersion.asInt())
    }

    kotlinOptions {
        jvmTarget = AppConfig.javaLanguageVersion.toString()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    kapt {
        correctErrorTypes = true
    }
}
