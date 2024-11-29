package configuration

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin

internal fun Project.configureCompose() {
    pluginManager.apply(ComposeCompilerGradleSubplugin::class.java)
    extensions.findByType(CommonExtension::class.java)?.also {
        it.buildFeatures {
            compose = true
        }
        with(dependencies) {
            add("implementation", platform("androidx.compose:compose-bom:$COMPOSE_BOM_VERSION"))
            add("implementation", "androidx.activity:activity-compose")
            add("implementation", "androidx.compose.material3:material3")
//            add("implementation", libs.findLibrary("compose.uiToolingPreview").get())
//            add("debugImplementation", libs.findLibrary("compose.uiTooling").get())
        }
    }
}
