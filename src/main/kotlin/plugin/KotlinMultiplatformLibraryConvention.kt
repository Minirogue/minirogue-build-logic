package plugin

import configuration.applyUniversalConfigurations
import configuration.configureCompose
import configuration.configureKotlinMultiplatformAndroid
import configuration.configureKotlinMultiplatformIOS
import configuration.configureKotlinMultiplatformJvm
import configuration.configureMetro
import configuration.configureRoomMultiplatform
import configuration.configureSerialization
import org.gradle.api.Plugin
import org.gradle.api.Project

public class KotlinMultiplatformLibraryConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
            }
            applyUniversalConfigurations()

            extensions.create(
                "minirogue",
                MinirogueMultiplatformLibraryExtension::class.java,
                target,
            )
        }
    }
}

public open class MinirogueMultiplatformLibraryExtension(private val project: Project) {
    public fun android(): Unit = project.configureKotlinMultiplatformAndroid()
    public fun ios(): Unit = project.configureKotlinMultiplatformIOS()
    public fun jvm(): Unit = project.configureKotlinMultiplatformJvm()

    public fun kotlinCompose(): Unit = project.configureCompose()
    public fun metro(): Unit = project.configureMetro()
    public fun room(): Unit = project.configureRoomMultiplatform()
    public fun serialization(): Unit = project.configureSerialization()
}
