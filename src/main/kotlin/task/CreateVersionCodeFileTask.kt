package task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

internal abstract class CreateVersionCodeFileTask : DefaultTask() {
    @get:InputFile
    abstract val versionCodeSource: RegularFileProperty

    @get:OutputFile
    val outputFile = project.layout.buildDirectory.file("minirogue/versionCode")

    @TaskAction
    fun copyScripts() {
        versionCodeSource.get().asFile.copyTo(target = outputFile.get().asFile, overwrite = true)
    }
}
