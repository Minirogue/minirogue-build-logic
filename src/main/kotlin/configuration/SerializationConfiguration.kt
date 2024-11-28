package configuration

import ext.isMultiplatform
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlinx.serialization.gradle.SerializationGradleSubplugin

// TODO find clean way to keep in sync with version catalog and be able to use dependabot
private const val SERIALIZATION_VERSION = "1.7.3"

internal fun Project.configureSerialization() {
    with(pluginManager) {
        apply(SerializationGradleSubplugin::class.java)
    }
    if (isMultiplatform()) {
        kotlinExtension.sourceSets.named("commonMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$SERIALIZATION_VERSION")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$SERIALIZATION_VERSION")
            }
        }
    } else {
        with(dependencies) {
            add(
                "implementation",
                "org.jetbrains.kotlinx:kotlinx-serialization-core:$SERIALIZATION_VERSION"
            )
            add(
                "implementation",
                "org.jetbrains.kotlinx:kotlinx-serialization-json:$SERIALIZATION_VERSION"
            )
        }
    }
}
