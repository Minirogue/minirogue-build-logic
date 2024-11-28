package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CacheableTask
internal open class CreateDetektConfigTask : DefaultTask() {
    // TODO would be nice to share a single config file and not repeat this for every module
    @OutputFile
    val outputFile = project.file("build/minirogue/detekt-config.yml")

    @TaskAction
    fun writeDetektConfig() {
        outputFile.printWriter().use { out ->
            out.println("""
            config:
                validation: true
                warningsAsErrors: false
                excludes: ''
            
            naming:
                FunctionNaming:
                    ignoreAnnotated: ['Composable']
            style:
                UnusedImports:
                    active: true
                MagicNumber:
                    ignoreEnums: true
        """.trimIndent()
            )
        }
    }
}
