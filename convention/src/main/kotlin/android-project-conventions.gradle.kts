import task.DownloadGradleDependencies

plugins {
    id("nl.neotech.plugin.rootcoverage")
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    // version found will be used. Override to stay on the same version.
    config = files("/config/detekt/detekt.yml")
    // Builds the AST in parallel. Rules are always executed in parallel. Can lead to speedups in larger projects.
    parallel = true
    // Specify the base path for file paths in the formatted reports.
    basePath = rootProject.projectDir.toString()
}

rootCoverage {
    // The default build variant for every module
    buildVariant = "debug"

    // Class & package exclude patterns
    excludes = listOf(
        "**/di/*",
        "**/BuildConfig*",
        "**/databinding/*",
        "**/*_*.class",
        "**/*_Impl*.class",
        "**/App.class",
        "**/*Activity.*",
        "**/*Fragment.*",
        "**/*Adapter.*",
        "**/*.compose.*",
    )

    // Since 1.1 generateHtml is by default true
    generateCsv = false
    generateHtml = true
    generateXml = true

    // Since 1.2: Same as executeTests except that this only affects the instrumented Android tests
    executeAndroidTests = false

    // Since 1.2: Same as executeTests except that this only affects the unit tests
    executeUnitTests = false

    // Since 1.2: When true include results from instrumented Android tests into the coverage report
    includeAndroidTestResults = true

    // Since 1.2: When true include results from unit tests into the coverage report
    includeUnitTestResults = true

    // Since 1.4: Sets jacoco.includeNoLocationClasses, so you don't have to. Helpful when using Robolectric
    // which usually requires this attribute to be true
    includeNoLocationClasses = true
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    // https://docs.gradle.org/current/userguide/project_report_plugin.html#sec:project_reports_tasks
    apply(plugin = "project-report")

    dependencies {
        // This rule set provides wrappers for rules implemented by ktlint - https://ktlint.github.io/.
        // https://detekt.dev/docs/rules/formatting/dawd
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
    }
}

tasks {
    DownloadGradleDependencies.register(project)
}
