import extension.defaultSetup
import extension.jdk
import extension.getVersionCatalog
import extension.proguardSetup
import extension.testsSetup

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {

    defaultSetup(project)
    proguardSetup()
    testsSetup()

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
