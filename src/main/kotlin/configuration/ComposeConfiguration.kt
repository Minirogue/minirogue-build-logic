package configuration

import com.android.build.api.dsl.CommonExtension
import ext.isMultiplatform
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal fun Project.configureCompose(desktopMainClass: String? = null) {
    pluginManager.apply(ComposeCompilerGradleSubplugin::class.java)
    pluginManager.apply(ComposePlugin::class.java)
    extensions.findByType(CommonExtension::class.java)?.also {
        it.buildFeatures {
            compose = true
        }
    }
    val composeDependencies = ComposePlugin.Dependencies(this)
    if (isMultiplatform()) {
        kotlinExtension.sourceSets.named("commonMain") {
            dependencies {
                implementation(composeDependencies.foundation)
                implementation(composeDependencies.material3)
                implementation(composeDependencies.runtime)
                implementation(ComposePlugin.CommonComponentsDependencies.resources)
            }
        }
    } else {
        dependencies.apply {
            add("implementation", composeDependencies.foundation)
            add("implementation", composeDependencies.material3)
            add("implementation", composeDependencies.runtime)
            add("implementation", ComposePlugin.CommonComponentsDependencies.resources)
        }
    }

    if (desktopMainClass != null) {
        dependencies.add("implementation", ComposePlugin.DesktopDependencies.currentOs)
        extensions.getByType(ComposeExtension::class.java).extensions
            .getByType(DesktopExtension::class.java).application.mainClass = desktopMainClass
    }
}
