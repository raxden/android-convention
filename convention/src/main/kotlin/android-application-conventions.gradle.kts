import extension.getSigningConfigProperties
import extension.libs
import extension.versions

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {

    compileSdk = libs.versions.compileSdk

    compileOptions {
        sourceCompatibility = libs.versions.sourceCompatibility
        targetCompatibility = libs.versions.targetCompatibility
    }

    signingConfigs {
        getByName("debug") {
            storeFile = project.file("${project.rootDir}/config/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            project.getSigningConfigProperties("release").run {
                storeFile = project.file("${project.rootDir}/${getProperty("storeFile")}")
                storePassword = getProperty("storePassword")
                keyAlias = getProperty("keyAlias")
                keyPassword = getProperty("keyPassword")
            }
        }
    }

    defaultConfig {
        minSdk = libs.versions.minSdk
        targetSdk = libs.versions.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")

            // Proguard configuration
            isMinifyEnabled = false

            // Tests configuration
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")

            // Proguard configuration
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                "proguard-android-optimize.txt",
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
            excludes.add("META-INF/*.kotlin_module")
        }
    }

    project.kotlin {
        jvmToolchain(jdkVersion = libs.versions.jdk.asInt())
    }

    kotlinOptions {
        jvmTarget = libs.versions.jdk.toString()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    project.kapt {
        correctErrorTypes = true
    }
}
