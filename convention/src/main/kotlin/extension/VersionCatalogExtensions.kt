package extension

import extension.Type.Bundles
import extension.Type.Libraries
import extension.Type.Plugins
import extension.Type.Versions
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.getByType

enum class Type {
    Versions,
    Libraries,
    Bundles,
    Plugins,
}

fun Project.getVersionCatalog(
    name: String = "libs",
): VersionCatalog = extensions
    .getByType<VersionCatalogsExtension>()
    .named(name)

fun VersionCatalog.get(
    type: Type = Versions,
    alias: String,
): String = when (type) {
    Versions -> getVersion(alias)
    Libraries -> TODO()
    Bundles -> TODO()
    Plugins -> TODO()
}

private fun VersionCatalog.getVersion(alias: String) =
    findVersion(alias).get().toString()

internal val VersionCatalog.composeCompiler: String
    get() = getVersion("composeCompiler")

internal val VersionCatalog.minSDK: Int
    get() = getVersion("minSdk").toInt()

internal val VersionCatalog.compileSDK: Int
    get() = getVersion("compileSdk").toInt()

internal val VersionCatalog.targetSDK: Int
    get() = getVersion("targetSdk").toInt()

internal val VersionCatalog.sourceCompatibility: JavaVersion
    get() = JavaVersion.toVersion(getVersion("sourceCompatibility"))

internal val VersionCatalog.targetCompatibility: JavaVersion
    get() = JavaVersion.toVersion(getVersion("targetCompatibility"))

internal val VersionCatalog.jdk: JavaLanguageVersion
    get() = JavaLanguageVersion.of(getVersion("jdk"))
