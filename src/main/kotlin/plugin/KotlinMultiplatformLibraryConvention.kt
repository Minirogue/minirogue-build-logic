package plugin

import configuration.applyUniversalConfigurations
import configuration.configureCompose
import configuration.configureKotlinMultiplatformAndroid
import configuration.configureKotlinMultiplatformIOS
import configuration.configureKotlinMultiplatformJvm
import configuration.configureMetro
import configuration.configureRoomMultiplatform
import configuration.configureSerialization
import configuration.configureTest
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import task.SourceType

public class KotlinMultiplatformLibraryConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
            }
            applyUniversalConfigurations()
            configureTest(SourceType.CommonMultiplatform)

            extensions.create(
                "minirogue",
                MinirogueMultiplatformLibraryExtension::class.java,
                target,
            )
        }
    }
}

public open class MinirogueMultiplatformLibraryExtension(private val project: Project) {
    public fun platforms(action: Action<PlatformConfig>) {
        action.execute(PlatformConfig(project))
        project.extensions.configure(KotlinMultiplatformExtension::class.java) {
            applyDefaultHierarchyTemplate() // adds iosMain source set
        }
    }

    public fun kotlinCompose(): Unit = project.configureCompose()
    public fun metro(): Unit = project.configureMetro()
    public fun room(): Unit = project.configureRoomMultiplatform()
    public fun serialization(): Unit = project.configureSerialization()
}

public class PlatformConfig(private val project: Project) {
    public fun android(): Unit = project.configureKotlinMultiplatformAndroid()
    public fun ios(): Unit = project.configureKotlinMultiplatformIOS()
    public fun jvm(): Unit = project.configureKotlinMultiplatformJvm()
}
