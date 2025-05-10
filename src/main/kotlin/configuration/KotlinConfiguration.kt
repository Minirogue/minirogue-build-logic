package configuration

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import versions.JAVA_VERSION

public fun Project.configureJvm() {
    extensions.configure(JavaPluginExtension::class.java) {
        sourceCompatibility = JavaVersion.toVersion(JAVA_VERSION)
        targetCompatibility = JavaVersion.toVersion(JAVA_VERSION)
    }
    tasks.withType(KotlinJvmCompile::class.java).configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(JAVA_VERSION))
        }
    }
}
