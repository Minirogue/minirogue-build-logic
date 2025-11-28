package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
internal open class AddScriptsTask : DefaultTask() {
    @get:InputFiles
    val scriptsFromResource: List<File> = listOf(
        "/scripts/create-module/createModule.main.kts",
        "/scripts/create-module/internal-template.gradle.kts",
        "/scripts/create-module/public-template.gradle.kts",
    ).mapNotNull { resourcePath ->
        this::class.java.getResource(resourcePath)?.toURI()?.let { File(it) }.also {
            requireNotNull(it) { "$resourcePath not found in jar resources" }
        }
    }

    @get:OutputDirectory
    val createModuleDirectory =
        project.file("${project.rootProject.file("scripts")}${File.separator}create-module")

    @TaskAction
    fun copyScripts() {
        copyModuleCreationScript()
    }

    private fun copyModuleCreationScript() {
        scriptsFromResource.forEach { resourceScript ->
            val inputStream = resourceScript.inputStream()
            val outputStream = File(
                "${createModuleDirectory.absolutePath}${File.separator}${resourceScript.name}",
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
