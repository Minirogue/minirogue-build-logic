package configuration

import org.gradle.api.Project
import task.CreateSrcTask
import task.SourceType

internal fun Project.configureCreateSrc(srcType: SourceType) {
    val rootTask = tasks.findByName("createSrc") ?: tasks.register("createSrc").get()
    val newTask = tasks.register("createSrc$srcType", CreateSrcTask::class.java, srcType)
    rootTask.dependsOn(newTask)
}
