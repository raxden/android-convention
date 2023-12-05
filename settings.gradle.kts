dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            val tomlFilePath = listOf(
                "${rootProject.projectDir.parentFile}/gradle/libraries.versions.toml",
                "${rootProject.projectDir.parentFile}/build-logic/gradle/libraries.versions.toml",
            )
            for (path in tomlFilePath) {
                val file = File(path)
                if (file.exists()) {
                    from(files(file))
                    break
                }
            }
        }
    }
}
