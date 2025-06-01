package configuration

import androidx.room.gradle.RoomExtension
import androidx.room.gradle.RoomGradlePlugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import versions.ROOM_VERSION

internal fun Project.configureRoomMultiplatform() {
    with(pluginManager) {
        applyKsp()
        apply(RoomGradlePlugin::class.java)
    }

    extensions.configure(RoomExtension::class.java) {
        schemaDirectory("$projectDir/schemas")
    }
    kotlinExtension.sourceSets.named("commonMain") {
        if (kotlinExtension.sourceSets.any { it.name == "androidMain" }) {
            dependencies.add("kspAndroid", "androidx.room:room-compiler:$ROOM_VERSION")
        }
        if (kotlinExtension.sourceSets.any { it.name == "jvmMain" }) {
            dependencies.add("kspJvm", "androidx.room:room-compiler:$ROOM_VERSION")
        }
        dependencies.add("kspCommonMainMetadata", "androidx.room:room-compiler:$ROOM_VERSION")
        dependencies {
            implementation("androidx.room:room-ktx:$ROOM_VERSION")
            implementation("androidx.room:room-runtime:$ROOM_VERSION")
        }
    }
}
