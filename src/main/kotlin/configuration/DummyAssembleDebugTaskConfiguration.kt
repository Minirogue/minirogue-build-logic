package configuration

import org.gradle.api.Project
import task.DummyAssembleDebugTask

/**
 * This creates an "assembleDebug" task that just depends on the "assemble" task.
 * This is useful for ensuring CI assembly of apps that have no debug configuration.
 */
internal fun Project.configureDummyAssembleDebugTask() {
    tasks.register("assembleDebug", DummyAssembleDebugTask::class.java) {
        dependsOn("assemble")
    }
}