plugins {
    `kotlin-dsl`
    `maven-publish`
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.detekt)
}

group = "org.minirogue"
version = "0.0.8"

kotlin {
    explicitApiWarning()
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

detekt {
    config.setFrom(files("detekt-config.yml"))
    buildUponDefaultConfig = true
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.compose.compilerGradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.room.gradlePlugin)
    implementation(libs.serialization.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("multiplatformLibrary") {
            id = "minirogue.multiplatform.library"
            implementationClass = "plugin.KotlinMultiplatformLibraryConvention"
        }
        register("testApp") {
            id = "minirogue.test.app"
            implementationClass = "plugin.TestAppPlugin"
        }
        register("androidApp") {
            id = "minirogue.android.app"
            implementationClass = "plugin.AndroidAppConventionPlugin"
        }
        register("jvmApp") {
            id = "minirogue.jvm.app"
            implementationClass = "plugin.JvmAppConventionPlugin"
        }
    }
}
