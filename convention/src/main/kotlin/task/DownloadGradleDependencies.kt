package task

import extension.downloadTo
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
        downloadConventionsRepository()
    }

    private fun downloadConventionsRepository() {
        println("Downloading conventions from $GIT_CONVENTIONS_SOURCE")

        val source = URL(GIT_CONVENTIONS_SOURCE)
        val destination = File(project.rootDir.path + "/build-logic/")
        val outputDir = downloadRepository(source, destination)

        println("Conventions downloaded to ${outputDir.absolutePath}")
    }

    private fun downloadRepository(
        repository: URL,
        destination: File,
    ): File {
        val zipFile = File("${project.rootDir.path}/${repository.path.hashCode()}.zip")
        val outputDir = destination.also {
            it.deleteRecursively()
            it.mkdirs()
        }

        repository.downloadTo(zipFile) { percent, mb, totalMb ->
            if (percent != null && totalMb != null) {
                val filled = percent / PROGRESS_STEP_PERCENT
                val bar = "█".repeat(filled) + "░".repeat(PROGRESS_BAR_SEGMENTS - filled)
                println("  [$bar] $percent% (%.2f / %.2f MB)".format(mb, totalMb))
            } else {
                println("  Downloading... %.2f MB".format(mb))
            }
        }

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

    companion object {

        private const val TASK_NAME = "downloadGradleDependencies"
        private const val GROUP_TASK_NAME = "dependencies"
        private const val GIT_CONVENTIONS_SOURCE =
            "https://github.com/raxden/android-convention/archive/refs/heads/master.zip"

        private const val PROGRESS_STEP_PERCENT = 5
        private const val PROGRESS_BAR_SEGMENTS = 20

        fun register(project: Project) {
            project.tasks.register<DownloadGradleDependencies>(TASK_NAME) {
                group = GROUP_TASK_NAME
            }
        }
    }
}
