package plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.utils.KOTLIN_ANDROID_PLUGIN_ID
import configuration.AddScriptsTaskConfiguration
import configuration.UniversalConfiguration
import configuration.applyUniversalConfigurations
import configuration.configureAndroidApp
import configuration.configureCompose
import configuration.configureMetro
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty

public class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(AppPlugin::class.java)
                apply(KOTLIN_ANDROID_PLUGIN_ID)
            }

            val extension = extensions.create(
                "minirogue",
                MinirogueAndroidAppExtension::class.java,
                target,
            )

            applyUniversalConfigurations(extension.universalConfiguration)
            configureAndroidApp()
        }
    }
}

public open class MinirogueAndroidAppExtension(private val project: Project) {
    // TODO replace with by-task DSL
    public val scriptsDirectory: DirectoryProperty = project.objects.directoryProperty()

    internal val universalConfiguration = UniversalConfiguration(
        useGradleCheckerTask = false,
        addScriptsTaskConfiguration = AddScriptsTaskConfiguration(scriptsDirectory),
    )

    public fun kotlinCompose(): Unit = project.configureCompose()
    public fun metro(): Unit = project.configureMetro()
}
