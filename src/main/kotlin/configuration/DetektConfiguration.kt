package configuration

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import task.CreateDetektConfigTask

private const val CREATE_DETEKT_CONFIG_TASK = "createDetektConfig"

internal fun Project.configureDetekt() {
    plugins.apply(DetektPlugin::class.java)

    tasks.register(CREATE_DETEKT_CONFIG_TASK, CreateDetektConfigTask::class.java)
    tasks.withType(Detekt::class.java) { dependsOn(CREATE_DETEKT_CONFIG_TASK) }

    extensions.configure(DetektExtension::class.java) {
        source.setFrom("src/main", "src/androidMain", "src/commonMain", "src/jvmMain")
        config.setFrom(files(tasks.getByName(CREATE_DETEKT_CONFIG_TASK)))
        buildUponDefaultConfig = true
    }
}
