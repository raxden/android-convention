package task

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import java.io.File
import java.net.URL

open class DownloadGradleDependencies : DefaultTask() {

    @TaskAction
    fun execute() {
        println("  Downloading Gradle Dependencies...")

        downloadConventionsRepository()
    }

    private fun downloadConventionsRepository() {
        println("  ... downloading 'conventions' files from $GIT_CONVENTIONS_SOURCE...")

        val source = URL(GIT_CONVENTIONS_SOURCE)
        val destination = File(project.rootDir.path + "/build-logic/")
        val outputDir = downloadRepository(source, destination)

        println("  ... files downloaded to ${outputDir.absolutePath}!")
    }

    private fun downloadRepository(
        repository: URL,
        destination: File,
    ): File {
        val zipFile = File("${project.rootDir.path}/${repository.path.hashCode()}.zip")
        val outputDir = destination.also {
            it.mkdirs()
            it.deleteRecursively()
        }

        repository.downloadToFile(zipFile)

        project.copy {
            from(project.zipTree(zipFile))
            into(outputDir)
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }

        outputDir.listFiles()?.firstOrNull()?.run {
            listFiles()?.forEach { file -> file.renameTo(File(outputDir, file.name)) }
            deleteRecursively()
        }

        zipFile.delete()

        return outputDir
    }

    private fun URL.downloadToFile(output: File) {
        openStream().use { input ->
            output.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    companion object {

        private const val TASK_NAME = "downloadGradleDependencies"
        private const val GROUP_TASK_NAME = "dependencies"
        private const val GIT_CONVENTIONS_SOURCE =
            "https://github.com/raxden/android-convention/archive/refs/heads/master.zip"

        fun register(project: Project) {
            project.tasks.register<DownloadGradleDependencies>(TASK_NAME) {
                group = GROUP_TASK_NAME
            }
        }
    }
}
