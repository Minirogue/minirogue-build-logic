package configuration

import org.gradle.api.Project
import task.CreateSrcTask
import task.MINIROGUE_TASK_GROUP
import task.SourceType

internal fun Project.configureCreateSrc(srcType: SourceType) {
    val rootTask = tasks.findByName("createSrc") ?: tasks.register("createSrc").get()
    val newTask = tasks.register("createSrc$srcType", CreateSrcTask::class.java, srcType)
        .apply {
            configure {
                group = MINIROGUE_TASK_GROUP
                description = "Creates source directories consistent with module structure"
            }
        }
    rootTask.dependsOn(newTask)
}
