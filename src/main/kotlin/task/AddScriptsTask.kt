package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

internal open class AddScriptsTask : DefaultTask() {

    private val scriptsDirectory = project.rootProject.file("scripts")

    @get:OutputDirectory
    val createModuleDirectory = project.file("$scriptsDirectory${File.separator}create-module")

    @TaskAction
    fun copyScripts() {
        copyModuleCreationScript()
    }

    private fun copyModuleCreationScript() {
        listOf(
            "/scripts/create-module/createModule.main.kts",
            "/scripts/create-module/internal-template.gradle.kts",
            "/scripts/create-module/public-template.gradle.kts",
        ).forEach { resourcePath ->
            val inputStream = this::class.java.getResourceAsStream(resourcePath)
            requireNotNull(inputStream) { "$resourcePath not found in jar resources" }
            val outputStream = File(
                "${createModuleDirectory.absolutePath}${File.separator}${resourcePath.split("/").last()}",
            )
                .outputStream()
            try {
                inputStream.copyTo(outputStream)
            } finally {
                inputStream.close()
                outputStream.close()
            }
        }
    }
}
