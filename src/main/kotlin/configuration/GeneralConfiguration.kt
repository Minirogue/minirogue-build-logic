package configuration

import ext.isMultiplatform
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import task.GradleCheckerTask
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
    tasks.register("checkGradleConfig", GradleCheckerTask::class.java)
}
