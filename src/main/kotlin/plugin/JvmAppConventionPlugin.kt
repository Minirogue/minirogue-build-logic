package plugin

import configuration.applyUniversalConfigurations
import configuration.configureCompose
import configuration.configureDummyJvmCiTasks
import configuration.configureJvm
import configuration.configureJvmApp
import configuration.configureSerialization
import org.gradle.api.Plugin
import org.gradle.api.Project

public class JvmAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }
            applyUniversalConfigurations(useGradleChecker = false)
            configureJvm()
            configureDummyJvmCiTasks()

            extensions.create("minirogue", MinirogueJvmAppExtension::class.java, target)
        }
    }
}

public open class MinirogueJvmAppExtension(private val project: Project) {
    public fun composeApp(mainClass: String, useHotReload: Boolean = false): Unit =
        project.configureCompose(desktopMainClass = mainClass, useHotReload = useHotReload)

    public fun jvmApp(mainClass: String): Unit = project.configureJvmApp(mainClass)
    public fun serialization(): Unit = project.configureSerialization()
}
