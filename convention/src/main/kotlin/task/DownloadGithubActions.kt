package task

import extension.downloadRepository
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import java.io.File
import java.net.URL

open class DownloadGithubActions : DefaultTask() {

    @TaskAction
    fun execute() {
        downloadGithubActionsRepository()
    }

    private fun downloadGithubActionsRepository() {
        val separator = "─".repeat(SEPARATOR_LENGTH)
        println()
        println("  ┌$separator")
        println("  │  Downloading Github Actions")
        println("  │  $GIT_GITHUB_ACTIONS_SOURCE")
        println("  ├$separator")

        val source = URL(GIT_GITHUB_ACTIONS_SOURCE)
        val destination = File(project.rootDir.path + "/.github/")
        val outputDir = project.downloadRepository(source, destination)

        println("  ├$separator")
        println("  │  Saved to ${outputDir.absolutePath}")
        println("  └$separator")
        println()
    }

    companion object {

        private const val TASK_NAME = "downloadGithubActions"
        private const val GROUP_TASK_NAME = "dependencies"
        private const val GIT_GITHUB_ACTIONS_SOURCE =
            "https://github.com/raxden/android-github-actions/archive/refs/heads/master.zip"

        private const val SEPARATOR_LENGTH = 85

        fun register(project: Project) {
            project.tasks.register<DownloadGithubActions>(TASK_NAME) {
                group = GROUP_TASK_NAME
            }
        }
    }
}
