package configuration

import org.gradle.api.Project
import task.DummyAssembleDebugTask
import task.MINIROGUE_TASK_GROUP

/**
 * This creates an "assembleDebug" task that just depends on the "assemble" task.
 * This is useful for ensuring CI assembly of apps that have no debug configuration.
 */
internal fun Project.configureDummyJvmCiTasks() {
    tasks.register("assembleDebug", DummyAssembleDebugTask::class.java) {
        group = MINIROGUE_TASK_GROUP
        description = "A dummy \"debug\" variant of the assemble task for consistent CI " +
            "configuration across platforms"
        dependsOn("assemble")
    }
    tasks.register("jvmTest", DummyAssembleDebugTask::class.java) {
        group = MINIROGUE_TASK_GROUP
        description = "A dummy \"jvm\" variant of the test task for consistent CI configuration " +
            "across platforms"
        dependsOn("test")
    }
}
