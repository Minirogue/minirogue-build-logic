package plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.utils.KOTLIN_ANDROID_PLUGIN_ID
import configuration.applyUniversalConfigurations
import configuration.configureAndroidApp
import configuration.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(AppPlugin::class.java)
                apply(KOTLIN_ANDROID_PLUGIN_ID)
            }
            applyUniversalConfigurations(useGradleChecker = false)
            configureAndroidApp()

            extensions.create(
                "minirogue",
                MinirogueAndroidAppExtension::class.java,
                target,
            )
        }
    }
}

public open class MinirogueAndroidAppExtension(private val project: Project) {
    public fun kotlinCompose(): Unit = project.configureCompose()
}
