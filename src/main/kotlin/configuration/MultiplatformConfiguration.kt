package configuration

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import task.SourceType

internal fun Project.configureKotlinMultiplatformAndroid() {
    configureAndroidLibrary()
    configureCreateSrc(SourceType.AndroidMultiplatform)
    extensions.configure(KotlinMultiplatformExtension::class.java) {
        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
        }
    }
}

internal fun Project.configureKotlinMultiplatformJvm() {
    configureJvm()
    configureCreateSrc(SourceType.JvmMultiplatform)
    extensions.configure(KotlinMultiplatformExtension::class.java) {
        jvm()
    }
}
