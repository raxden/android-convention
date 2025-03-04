package extension

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import java.util.Properties

internal fun Project.getSigningConfigProperties(buildType: String): Properties {
    val properties = Properties()
    val propertiesFile = file("$rootDir/config/signing_$buildType.properties")
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use { properties.load(it) }
    } else {
        println("No signing config found for build type $buildType")
    }
    return properties
}

fun LibraryExtension.roomSetup(
    project: Project,
    schemasPath: String = "${project.projectDir}/schemas"
) {
    defaultConfig {
        project.ksp {
            arg("room.schemaLocation", schemasPath)
        }
    }
    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("debug")
            .assets
            .srcDirs(project.files(schemasPath))
    }
}

fun BaseAppModuleExtension.roomSetup(
    project: Project,
    schemasPath: String = "${project.projectDir}/schemas"
) {
    defaultConfig {
        project.ksp {
            arg("room.schemaLocation", schemasPath)
        }
    }
    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("debug")
            .assets
            .srcDirs(project.files(schemasPath))
    }
}

private fun Project.ksp(configure: KspExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("ksp", configure)
