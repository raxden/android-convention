package extension

import extension.Type.*
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
    get() = getVersion("sourceCompatibility").toJavaVersion()

internal val VersionCatalog.targetCompatibility: JavaVersion
    get() = getVersion("targetCompatibility").toJavaVersion()

internal val VersionCatalog.jdk: JavaLanguageVersion
    get() = JavaLanguageVersion.of(getVersion("jdk"))

@Suppress("CyclomaticComplexMethod", "UnstableApiUsage")
private fun String.toJavaVersion() : JavaVersion = when (this) {
    "1.6" -> JavaVersion.VERSION_1_6
    "1.7" -> JavaVersion.VERSION_1_7
    "1.8" -> JavaVersion.VERSION_1_8
    "9" -> JavaVersion.VERSION_1_9
    "10" -> JavaVersion.VERSION_1_10
    "11" -> JavaVersion.VERSION_11
    "12" -> JavaVersion.VERSION_12
    "13" -> JavaVersion.VERSION_13
    "14" -> JavaVersion.VERSION_14
    "15" -> JavaVersion.VERSION_15
    "16" -> JavaVersion.VERSION_16
    "17" -> JavaVersion.VERSION_17
    "18" -> JavaVersion.VERSION_18
    "19" -> JavaVersion.VERSION_19
    "20" -> JavaVersion.VERSION_20
    "21" -> JavaVersion.VERSION_21
    "22" -> JavaVersion.VERSION_22
    "23" -> JavaVersion.VERSION_23
    "24" -> JavaVersion.VERSION_24
    else -> JavaVersion.VERSION_1_8
}
