package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.konan.file.File
import javax.inject.Inject

internal open class CreateGitHubConfigTask @Inject constructor() : DefaultTask() {

    @OutputFile
    val dependabotYamlFile = project.rootProject.file(".github${File.separator}dependabot.yml")

    @OutputFile
    val buildAndChecksWorkflowFile =
        project.rootProject.file(".github${File.separator}workflows${File.separator}checks.yml")

    @TaskAction
    fun createFiles() {
        createStandardBuildWorkflow()
        createDependabotYaml()
    }

    private fun createStandardBuildWorkflow() {
        buildAndChecksWorkflowFile.printWriter().use { out ->
            out.apply {
                println("name: Build, Analyze, and Test")
                println("on:")
                println("  push:")
                println("    branches: [ \"main\" ]")
                println("  pull_request:")
                println("    branches: [ \"main\" ]")
                println("jobs:")
                println("  build:")
                println("    runs-on: ubuntu-latest")
                println("    permissions:")
                println("      contents: read")
                println("    steps:")
                println("      - uses: actions/checkout@v4")
                println("      - name: Set up JDK 17")
                println("        uses: actions/setup-java@v4")
                println("        with:")
                println("          java-version: '17'")
                println("          distribution: 'temurin'")
                println("      - uses: actions/cache@v4")
                println("        with:")
                println("          path: |")
                println("            ~/.gradle/caches")
                println("            ~/.gradle/wrapper")
                println("          key: \${{ runner.os }}-gradle-\${{ github.run_id }}")
                println("          restore-keys: |")
                println("            \${{ runner.os }}-gradle-")
                println("      - name: Gradle build and checks")
                println("        run: ./gradlew assembleDebug testDebugUnitTest testJvm detekt lint --continue && ./gradlew --stop")
            }
        }
    }

    private fun createDependabotYaml() {
        dependabotYamlFile.printWriter().use { out ->
            out.println(
                """
                version: 2
                updates:
                  - package-ecosystem: "gradle" # See documentation for possible values
                    directory: "/" # Location of package manifests
                    schedule:
                      interval: "daily"
                    target-branch: "main"
                    reviewers:
                      - Minirogue
            """.trimIndent()
            )
        }
    }
}
