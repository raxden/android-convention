package extension

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import java.util.Properties

fun BaseAppModuleExtension.defaultSetup(
    project: Project,
    catalog: VersionCatalog = project.getVersionCatalog(),
) {
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

    project.kotlin {
        jvmToolchain(jdkVersion = catalog.jdk.asInt())
    }

    kotlinOptions {
        jvmTarget = catalog.jdk.toString()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    project.kapt {
        correctErrorTypes = true
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

    project.kotlin {
        jvmToolchain(jdkVersion = catalog.jdk.asInt())
    }

    kotlinOptions {
        jvmTarget = catalog.jdk.toString()
    }

    // Allow references to generated code -> https://developer.android.com/training/dependency-injection/hilt-android#kts
    project.kapt {
        correctErrorTypes = true
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

fun CommonExtension<*, *, *, *>.roomSetup(
    project: Project
) {
    val schemasPath = "${project.projectDir}/schemas"
    defaultConfig {
        project.kapt {
            arguments {
                arg("room.schemaLocation", schemasPath)
            }
        }
    }
    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("debug")
            .assets
            .srcDirs(project.files(schemasPath))
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

private fun Project.kapt(configure: KaptExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("kapt", configure)

private fun Project.kotlin(configure: KotlinAndroidProjectExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("kotlin", configure)

private fun BaseAppModuleExtension.kotlinOptions(configure: KotlinJvmOptions.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("kotlinOptions", configure)

private fun LibraryExtension.kotlinOptions(configure: KotlinJvmOptions.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("kotlinOptions", configure)
