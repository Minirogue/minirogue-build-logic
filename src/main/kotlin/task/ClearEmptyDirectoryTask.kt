package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Recursively clear all empty directories
 */
internal open class ClearEmptyDirectoryTask : DefaultTask() {

    @TaskAction
    fun clearEmptyDirectories() {
        Runtime.getRuntime().exec("find . -type d -empty -delete")
    }
}
