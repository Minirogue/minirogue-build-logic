package plugin

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.Parameter
import org.junit.jupiter.params.ParameterizedClass
import org.junit.jupiter.params.provider.EnumSource
import utils.GradleTestVersion
import java.io.File

@ParameterizedClass
@EnumSource(GradleTestVersion::class)
class AndroidAppConventionPluginTest {

    @Parameter
    lateinit var gradleVersion: GradleTestVersion

    @field:TempDir
    lateinit var testProjectDir: File

    private lateinit var settingsFile: File
    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        settingsFile = File(testProjectDir, "settings.gradle")
        buildFile = File(testProjectDir, "build.gradle")
        // delete
        File("testProjectDir${File.separator}$LOCAL_BUILD_CACHE_DIRECTORY").deleteRecursively()
    }

    @Test
    fun `can assemble jvm only`() {
        // Arrange
        val task = "assemble"
        writeBuildFile(jvm = true, android = false)
        writeSettingsFile()

        // Act
        val result = runBuild(listOf(task))

        // Assert
        result.tasks.forEach {
            assertThat(it.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }

    private fun runBuild(arguments: List<String>): BuildResult =
        GradleRunner.create()
            .withPluginClasspath()
            .withGradleVersion(gradleVersion.version)
            .withProjectDir(testProjectDir)
            .withArguments(arguments)
            .build()

    private fun writeFile(destination: File, content: String) {
        destination.bufferedWriter().use { writer ->
            writer.write(content)
        }
    }

    private fun onFileOutput(
        expectedFilePath: String = DEFAULT_OUTPUT_PATH,
        assertion: (String) -> Unit,
    ) {
        val file = File("$testProjectDir${File.separator}$expectedFilePath")
        assertWithMessage("File not found at $expectedFilePath").that(file.exists()).isTrue()
        file.bufferedReader().use { reader ->
            // trimIndent will standardize newline characters
            reader.readText().trimIndent().also {
                assertion(it)
            }
        }
    }

    private fun writeSettingsFile(
        projectName: String = "testproject",
    ) = writeFile(
        settingsFile,
        """
            dependencyResolutionManagement {
                repositories {
                    mavenCentral()
                }
            }
            
            rootProject.name = "$projectName" 
            
            buildCache {
                local.directory = "$LOCAL_BUILD_CACHE_DIRECTORY"
            }
        """.trimMargin(),
    )

    private fun writeBuildFile(
        implementationDependencies: List<String> = emptyList(),
        compileOnlyDependencies: List<String> = emptyList(),
        testImplementationDependencies: List<String> = emptyList(),
        testCompileOnlyDependencies: List<String> = emptyList(),
        apiDependencies: List<String> = emptyList(),
        jvm: Boolean = false,
        android: Boolean = false,
    ) = writeFile(
        buildFile,
        """
            plugins {
                id("$PLUGIN_NAME")
            }
            
            ${getExtensionBlock(jvm, android)}

            dependencies {
                ${implementationDependencies.joinToString(separator = "\r\n") { "implementation(\"$it\")" }}
                ${apiDependencies.joinToString(separator = "\r\n") { "api(\"$it\")" }}
                ${compileOnlyDependencies.joinToString(separator = "\r\n") { "compileOnly(\"$it\")" }}
                ${testImplementationDependencies.joinToString(separator = "\r\n") { "testImplementation(\"$it\")" }}
                ${testCompileOnlyDependencies.joinToString(separator = "\r\n") { "testCompileOnly(\"$it\")" }}
            }
        """.trimIndent(),
    )

    private fun getExtensionBlock(jvm: Boolean = false, android: Boolean = false) =
        """
        minirogue {
            platforms {
                ${if (jvm) "jvm()" else ""}
                ${if (android) "android()" else ""}
            }
        }
        """.trimIndent()

    companion object {
        private val DEFAULT_OUTPUT_PATH =
            "build${File.separator}reports${File.separator}project-report.md"
        private const val LOCAL_BUILD_CACHE_DIRECTORY = "build-cache"
        private const val PLUGIN_NAME = "minirogue.multiplatform.library"
    }
}