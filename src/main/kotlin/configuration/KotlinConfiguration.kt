package configuration

import ext.JAVA_LIB_VERSION
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile


public fun Project.configureJvm() {
    extensions.configure(JavaPluginExtension::class.java) {
        sourceCompatibility = JavaVersion.toVersion(JAVA_LIB_VERSION)
        targetCompatibility = JavaVersion.toVersion(JAVA_LIB_VERSION)
    }
    tasks.withType(KotlinJvmCompile::class.java).configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(JAVA_LIB_VERSION))
        }
    }
}
