package configuration

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import task.SourceType

internal fun Project.configureTest(sourceType: SourceType) {
    when (sourceType) {
        SourceType.SinglePlatform -> logger.warn(
            "test configuration for SourceType.SinglePlatform not implemented",
            IllegalArgumentException(),
        )

        SourceType.CommonMultiplatform -> {
            kotlinExtension.sourceSets.named("commonTest") {
                dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test-common")
                    implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
                }
            }
        }

        SourceType.AndroidMultiplatform -> {
            kotlinExtension.sourceSets.named("androidUnitTest") {
                dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test-junit")
                }
            }
        }

        SourceType.IosMultiplatform -> logger.warn(
            "This version of build logic does not yet support iOS test configuration",
            IllegalArgumentException(),
        )

        SourceType.JvmMultiplatform -> {
            kotlinExtension.sourceSets.named("jvmTest") {
                dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test-junit")
                }
            }
        }
    }
}
