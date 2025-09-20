package configuration

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import task.SourceType

internal fun Project.configureKotlinMultiplatformAndroid() {
    configureAndroidLibrary()
    extensions.configure(KotlinMultiplatformExtension::class.java) {
        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
        }
    }
    configureCreateSrc(SourceType.AndroidMultiplatform)
    configureTest(SourceType.AndroidMultiplatform)
}

internal fun Project.configureKotlinMultiplatformJvm() {
    configureJvm()
    extensions.configure(KotlinMultiplatformExtension::class.java) {
        jvm()
    }
    configureCreateSrc(SourceType.JvmMultiplatform)
    configureTest(SourceType.JvmMultiplatform)
}

internal fun Project.configureKotlinMultiplatformIOS() {
    extensions.configure(KotlinMultiplatformExtension::class.java) {
        listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
            it.binaries.framework {
                baseName = (project.parent?.name ?: "") + project.name
                isStatic = true
            }
        }
    }
    configureCreateSrc(SourceType.IosMultiplatform)
//    configureTest(SourceType.IosMultiplatform) TODO
}
