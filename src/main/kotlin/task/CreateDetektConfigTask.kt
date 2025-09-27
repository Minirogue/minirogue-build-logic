package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import kotlin.jvm.java

@CacheableTask
internal open class CreateDetektConfigTask : DefaultTask() {
    @OutputFile
    val outputFile = project.file("build/minirogue/detekt-config.yml")

    @TaskAction
    fun writeDetektConfig() {
        val inputStream = this::class.java.getResourceAsStream("/detekt-config.yml")
        requireNotNull(inputStream) { "detekt-config.yml not found in jar resources" }
        val outputStream = outputFile.outputStream()
        try {
            inputStream.copyTo(outputStream)
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }
}
