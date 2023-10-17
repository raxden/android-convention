import extension.defaultSetup
import extension.proguardSetup
import extension.testsSetup

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {

    defaultSetup(project)
    proguardSetup()
    testsSetup()
}
