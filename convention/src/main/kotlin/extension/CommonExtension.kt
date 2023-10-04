package extension

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import java.util.Properties

fun BaseAppModuleExtension.defaultSetup(
    project: Project
) {
    val catalog = project.getVersionCatalog()

    compileSdk = catalog.compileSDK

    compileOptions {
        sourceCompatibility = catalog.sourceCompatibility
        targetCompatibility = catalog.targetCompatibility
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
        minSdk = catalog.minSDK
        targetSdk = catalog.targetSDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
            excludes.add("META-INF/*.kotlin_module")
        }
    }
}

private fun Project.getSigningConfigProperties(buildType: String): Properties {
    val properties = Properties()
    val propertiesFile = file("$rootDir/config/signing_$buildType.properties")
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use { properties.load(it) }
    } else {
        println("No signing config found for build type $buildType")
    }
    return properties
}

fun LibraryExtension.defaultSetup(
    project: Project,
    catalog: VersionCatalog = project.getVersionCatalog(),
) {

    compileSdk = catalog.compileSDK

    compileOptions {
        sourceCompatibility = catalog.sourceCompatibility
        targetCompatibility = catalog.targetCompatibility
    }

    defaultConfig {
        minSdk = catalog.minSDK
        targetSdk = catalog.targetSDK // needed for instrumental tests
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
//            excludes.add("META-INF/*.kotlin_module")
        }
    }
}

fun CommonExtension<*, *, *, *>.testsSetup() {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
}

fun CommonExtension<*, *, *, *>.composeSetup(
    project: Project,
    catalog: VersionCatalog = project.getVersionCatalog(),
) {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = catalog.composeCompiler
    }

    packagingOptions {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }
}

fun CommonExtension<*, *, *, *>.roomConfig(
    project: Project
) {
    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("debug")
            .assets
            .srcDirs(project.files("${project.projectDir}/schemas"))
    }
}

fun BaseAppModuleExtension.proguardSetup() {
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                "proguard-android-optimize.txt",
                "proguard-rules.pro"
            )
        }
    }
}

fun LibraryExtension.proguardSetup() {
    defaultConfig {
        consumerProguardFile("consumer-rules.pro")
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}
