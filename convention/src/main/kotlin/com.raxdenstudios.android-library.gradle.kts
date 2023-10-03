import extension.androidConfig
import extension.jdk
import extension.getVersionCatalog
import extension.proguardConfig
import extension.testsConfig

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

    val catalog = project.getVersionCatalog()

    kotlin {
        jvmToolchain(jdkVersion = catalog.jdk.asInt())
    }

    kotlinOptions {
        jvmTarget = catalog.jdk.toString()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    kapt {
        correctErrorTypes = true
    }
}
