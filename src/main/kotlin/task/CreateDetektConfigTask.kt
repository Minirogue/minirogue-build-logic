package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
internal open class CreateDetektConfigTask : DefaultTask() {

    @InputFile
    val detektConfigurationYaml: File =
        this::class.java.getResource("/detekt-config.yml")?.toURI()?.let { File(it) }
            ?: error("detekt-config.yml not found in jar resources")

    @OutputFile
    val outputFile = project.file("build/minirogue/detekt-config.yml")

    @TaskAction
    fun writeDetektConfig() {
        val inputStream = detektConfigurationYaml.inputStream()
        val outputStream = outputFile.outputStream()
        try {
            inputStream.copyTo(outputStream)
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }
}
