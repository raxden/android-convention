import extension.libs
import extension.versions

plugins {
    id("com.android.library")
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

    defaultConfig {
        minSdk = libs.versions.minSdk
        targetSdk = libs.versions.targetSdk // needed for instrumental tests
    }

    buildTypes {
        getByName("debug") {
            // Proguard configuration
            isMinifyEnabled = false

            // Tests configuration
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
//            excludes.add("META-INF/*.kotlin_module")
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
