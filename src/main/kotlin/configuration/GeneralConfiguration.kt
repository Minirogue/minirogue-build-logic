package configuration

import ext.isMultiplatform
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import task.GradleCheckerTask
import task.MINIROGUE_TASK_GROUP
import task.SourceType

internal fun Project.applyUniversalConfigurations(useGradleChecker: Boolean = true) {
    if (useGradleChecker) configureGradleChecker()
    configureDetekt()
    configureGitHubConfigTask()
    configureCreateSrc(if (isMultiplatform()) SourceType.CommonMultiplatform else SourceType.SinglePlatform)
    tasks.withType(KotlinCompile::class.java).all {
        compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
    }
}

private fun Project.configureGradleChecker() {
    tasks.register("checkGradleConfig", GradleCheckerTask::class.java) {
        group = MINIROGUE_TASK_GROUP
        description =
            "Checks the gradle file to ensure it follows a \"3-block\" format with only one plugin"
    }
}
