plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.minirogue.buildlogic"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
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
