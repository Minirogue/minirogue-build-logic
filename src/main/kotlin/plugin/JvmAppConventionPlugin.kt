package plugin

import configuration.applyUniversalConfigurations
import configuration.configureCompose
import configuration.configureJvm
import configuration.configureSerialization
import org.gradle.api.Plugin
import org.gradle.api.Project

public class JvmAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // apply("application") // TODO have this as a configuration if needed as alternative to composeApp
                apply("org.jetbrains.kotlin.jvm")
            }
            applyUniversalConfigurations(useGradleChecker = false)
            configureJvm()

            extensions.create("minirogue", MinirogueJvmAppExtension::class.java, target)
        }
    }
}

public open class MinirogueJvmAppExtension(private val project: Project) {
    public fun composeApp(mainClass: String): Unit = project.configureCompose(mainClass)
    public fun serialization(): Unit = project.configureSerialization()
}
