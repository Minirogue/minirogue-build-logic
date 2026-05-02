package plugin

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.toolchains.foojay.FoojayToolchainsConventionPlugin

public class SettingsConventionPlugin : Plugin<Settings> {
    override fun apply(target: Settings): Unit = with(target) {
        with(plugins) {
            apply(FoojayToolchainsConventionPlugin::class.java)
        }
        dependencyResolutionManagement {
            repositories {
                google()
                mavenCentral()
            }
        }


        enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    }
}