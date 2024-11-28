package plugin

import configuration.applyUniversalConfigurations
import configuration.configureJvm
import configuration.configureSerialization
import org.gradle.api.Plugin
import org.gradle.api.Project

public class JvmAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("application")
                apply("org.jetbrains.kotlin.jvm")
            }
            applyUniversalConfigurations(useGradleChecker = false)
            configureJvm()

            extensions.create("minirogue", MinirogueJvmAppExtension::class.java, target)
        }
    }
}

public open class MinirogueJvmAppExtension(private val project: Project) {
    public fun serialization(): Unit = project.configureSerialization()
}
