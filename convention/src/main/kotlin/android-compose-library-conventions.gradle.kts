import extension.libs

plugins {
    id("android-library-conventions")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {

    buildFeatures {
        compose = true
    }

    packaging {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }

    dependencies {
        implementation(libs.findLibrary("kotlinx-serialization-json").get())
    }
}
