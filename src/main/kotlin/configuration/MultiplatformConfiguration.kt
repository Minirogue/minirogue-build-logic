package configuration

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import task.SourceType

internal fun Project.configureKotlinMultiplatformAndroid() {
    configureAndroidMultiplatformLibrary()
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
