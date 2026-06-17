dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            val tomlFilePath = listOf(
                "${rootProject.projectDir.parentFile}/gradle/libs.versions.toml",
                "./gradle/libs.versions.toml",
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
include(":convention")
