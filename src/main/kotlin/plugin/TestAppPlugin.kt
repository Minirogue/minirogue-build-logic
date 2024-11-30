package plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.utils.KOTLIN_ANDROID_PLUGIN_ID
import configuration.applyUniversalConfigurations
import configuration.configureAndroidApp
import configuration.configureHilt
import ext.generateProjectNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project

public class TestAppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(AppPlugin::class.java)
                apply(KOTLIN_ANDROID_PLUGIN_ID)
            }
            applyUniversalConfigurations(useGradleChecker = false)
            configureAndroidApp()
            configureHilt()

            extensions.configure(ApplicationExtension::class.java) {
                defaultConfig {
                    applicationId = "com.${target.rootProject.name}.test.app"
                }
                namespace = generateProjectNamespace()
            }

            extensions.create(
                "minirogue",
                MinirogueTestAppExtension::class.java,
                target
            )
        }
    }
}

public open class MinirogueTestAppExtension(project: Project)
