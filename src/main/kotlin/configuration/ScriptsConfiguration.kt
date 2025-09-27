package configuration

import org.gradle.api.Project
import task.AddScriptsTask
import task.MINIROGUE_TASK_GROUP

private const val ADD_SCRIPTS_TASK = "addScripts"

internal fun Project.configureAddScriptsTask() {
    if (rootProject.tasks.none { it.name == ADD_SCRIPTS_TASK }) {
        // Creates single instance of this task in root project
        rootProject.tasks.register(ADD_SCRIPTS_TASK, AddScriptsTask::class.java) {
            group = MINIROGUE_TASK_GROUP
            description = "Adds commonly useful scripts to the scripts directory."
        }
    }
}
