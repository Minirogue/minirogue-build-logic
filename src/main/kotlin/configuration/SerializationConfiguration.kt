package configuration

import ext.isMultiplatform
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin
import versions.KOTLIN_VERSION

internal fun Project.configureSerialization() {
    with(pluginManager) {
        apply(SerializationGradleSubplugin::class.java)
    }
    if (isMultiplatform()) {
        kotlinExtension.sourceSets.named("commonMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$KOTLIN_VERSION")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$KOTLIN_VERSION")
            }
        }
    } else {
        with(dependencies) {
            add(
                "implementation",
                "org.jetbrains.kotlinx:kotlinx-serialization-core:$KOTLIN_VERSION"
            )
            add(
                "implementation",
                "org.jetbrains.kotlinx:kotlinx-serialization-json:$KOTLIN_VERSION"
            )
        }
    }
}
