package model

import extension.findVersionOrThrow
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.jvm.toolchain.JavaLanguageVersion

class Versions(
    versionCatalog: VersionCatalog
) {
    val composeCompiler: String = versionCatalog.findVersionOrThrow("composeCompiler")
    val minSdk: Int = versionCatalog.findVersionOrThrow("minSdk").toInt()
    val compileSdk: Int = versionCatalog.findVersionOrThrow("compileSdk").toInt()
    val targetSdk: Int = versionCatalog.findVersionOrThrow("targetSdk").toInt()
    val sourceCompatibility: JavaVersion = JavaVersion.toVersion(
        versionCatalog.findVersionOrThrow("sourceCompatibility")
    )
    val targetCompatibility: JavaVersion = JavaVersion.toVersion(
        versionCatalog.findVersionOrThrow("targetCompatibility")
    )
    val jdk: JavaLanguageVersion = JavaLanguageVersion.of(
        versionCatalog.findVersionOrThrow("jdk")
    )
}
