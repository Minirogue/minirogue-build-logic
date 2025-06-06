package plugin

import configuration.applyUniversalConfigurations
import configuration.configureCompose
import configuration.configureHilt
import configuration.configureKotlinMultiplatformAndroid
import configuration.configureKotlinMultiplatformIOS
import configuration.configureKotlinMultiplatformJvm
import configuration.configureMetro
import configuration.configureRoomMultiplatform
import configuration.configureSerialization
import configuration.configureViewBinding
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

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
    public fun android(): Unit = android {}
    public fun android(androidActions: Action<AndroidConfig>) {
        project.configureKotlinMultiplatformAndroid()
        androidActions.execute(AndroidConfig(project))
    }

    public fun ios(): Unit = project.configureKotlinMultiplatformIOS()

    public fun jvm(): Unit = jvm {}
    public fun jvm(jvmActions: Action<JvmConfig>) {
        project.configureKotlinMultiplatformJvm()
        jvmActions.execute(JvmConfig((project)))
    }

    public fun kotlinCompose(): Unit = project.configureCompose()
    public fun metro(): Unit = project.configureMetro()
    public fun room(): Unit = project.configureRoomMultiplatform()
    public fun serialization(): Unit = project.configureSerialization()

    @Deprecated("Explicit backing fields will be removed/revised in a future version of Kotlin")
    public fun explicitBackingFields(): Unit =
        project.extensions.configure(KotlinProjectExtension::class.java) {
            sourceSets.all {
                languageSettings.enableLanguageFeature("ExplicitBackingFields")
            }
        }
}

public class AndroidConfig(private val project: Project) {
    @Deprecated("Use multiplatform \"kotlinCompose()\" configuration")
    public fun composeUi(): Unit = project.configureCompose()
    public fun hilt(): Unit = project.configureHilt()
    public fun viewBinding(): Unit = project.configureViewBinding()
}

public class JvmConfig(private val project: Project)
