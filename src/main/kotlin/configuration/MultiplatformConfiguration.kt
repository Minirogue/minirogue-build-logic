package configuration

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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
    tasks.withType(KotlinCompile::class.java).all {
        compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

internal fun Project.configureKotlinMultiplatformJvm() {
    configureJvm()
    configureCreateSrc(SourceType.JvmMultiplatform)
    extensions.configure(KotlinMultiplatformExtension::class.java) {
        jvm()
    }
}

internal fun Project.configureKotlinMultiplatformIOS() {
    configureCreateSrc(SourceType.IosMultiplatform)
    extensions.configure(KotlinMultiplatformExtension::class.java) {
        listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
            it.binaries.framework {
                baseName = (project.parent?.name ?: "") + project.name
                isStatic = true
            }
        }
    }
}
