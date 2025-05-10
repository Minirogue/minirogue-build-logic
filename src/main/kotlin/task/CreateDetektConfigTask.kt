package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CacheableTask
internal open class CreateDetektConfigTask : DefaultTask() {
    @OutputFile
    val outputFile = project.file("build/minirogue/detekt-config.yml")

    // TODO it would be nice to share the config with the one used for this project
    @TaskAction
    fun writeDetektConfig() {
        outputFile.printWriter().use { out ->
            out.println(
                """
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
                MaxLineLength:
                    active: false # overlaps with formatting>MaximumLineLength
                ModifierOrder:
                    active: false # overlaps with formatting>ModifierOrdering
                NewLineAtEndOfFile:
                    active: false # overlaps with formatting>FinalNewline
                    
            formatting:
                Filename:
                    active: false # overlaps with naming>MatchingDeclarationName
                TrailingCommaOnCallSite:
                    active: true
                TrailingCommaOnDeclarationSite:
                    active: true
                    
            complexity:
                LongParameterList:
                    active: false
                """.trimIndent(),
            )
        }
    }
}
