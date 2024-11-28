package plugin

import convention.applyUniversalConfigurations
import convention.configureGradleChecker
import convention.configureJvm
import convention.configureSerialization
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmAppConventionPlugin : Plugin<Project> {
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

open class MinirogueJvmAppExtension(private val project: Project) {
    fun serialization() = project.configureSerialization()
}
