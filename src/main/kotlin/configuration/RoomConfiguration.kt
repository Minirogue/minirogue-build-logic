package configuration

import androidx.room.gradle.RoomExtension
import androidx.room.gradle.RoomGradlePlugin
import ext.ROOM_VERSION
import ext.isMultiplatform
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal fun Project.configureRoom() {
    with(pluginManager) {
        applyKsp()
        apply(RoomGradlePlugin::class.java)
    }

    extensions.configure(RoomExtension::class.java) {
        schemaDirectory("$projectDir/schemas")
    }
    if (isMultiplatform()) {
        kotlinExtension.sourceSets.named("androidMain") {
            with(dependencies) {
                add("implementation", "androidx.room:room-ktx:$ROOM_VERSION")
                add("implementation", "androidx.room:room-runtime:$ROOM_VERSION")
                add("kspAndroid", "androidx.room:room-compiler:$ROOM_VERSION")
            }
        }
    } else {
        with(dependencies) {
            add("implementation", "androidx.room:room-ktx:$ROOM_VERSION")
            add("implementation", "androidx.room:room-runtime:$ROOM_VERSION")
            add("ksp", "androidx.room:room-compiler:$ROOM_VERSION")
        }
    }
}
