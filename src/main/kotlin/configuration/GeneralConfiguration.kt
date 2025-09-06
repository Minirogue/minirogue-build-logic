package configuration

import ext.isMultiplatform
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import task.GradleCheckerTask
import task.MINIROGUE_TASK_GROUP
import task.SourceType

internal fun Project.applyUniversalConfigurations(useGradleChecker: Boolean = true) {
    if (useGradleChecker) configureGradleChecker()
    configureDetekt()
    configureGitHubConfigTask()
    configureCreateSrc(if (isMultiplatform()) SourceType.CommonMultiplatform else SourceType.SinglePlatform)
    tasks.withType(KotlinCompilationTask::class.java) {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }
}

private fun Project.configureGradleChecker() {
    tasks.register("checkGradleConfig", GradleCheckerTask::class.java) {
        group = MINIROGUE_TASK_GROUP
        description =
            "Checks the gradle file to ensure it follows a \"3-block\" format with only one plugin"
    }
}
