package configuration

import ext.DETEKT_VERSION
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.jetbrains.compose.internal.utils.registerOrConfigure
import task.CreateDetektConfigTask

private const val CREATE_DETEKT_CONFIG_TASK = "createDetektConfig"

internal fun Project.configureDetekt() {
    plugins.apply(DetektPlugin::class.java)
    // Add formatting, which is a wrapper around ktlint
    dependencies.add(
        "detektPlugins",
        "io.gitlab.arturbosch.detekt:detekt-formatting:$DETEKT_VERSION"
    )

    if (rootProject.tasks.none { it.name == CREATE_DETEKT_CONFIG_TASK }) {
        // Creates single instance of this task in root project, then all projects' detekt tasks can
        //   depend on the single config file produced by this single task
        rootProject.tasks.register(CREATE_DETEKT_CONFIG_TASK, CreateDetektConfigTask::class.java)
    }
    tasks.withType(Detekt::class.java) { dependsOn(rootProject.tasks.getByName(CREATE_DETEKT_CONFIG_TASK)) }

    extensions.configure(DetektExtension::class.java) {
        source.setFrom("src/main", "src/androidMain", "src/commonMain", "src/jvmMain")
        config.setFrom(files(rootProject.tasks.getByName(CREATE_DETEKT_CONFIG_TASK)))
        buildUponDefaultConfig = true
        autoCorrect = true
    }
}
