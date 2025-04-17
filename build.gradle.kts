import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.detekt)
}

group = "org.minirogue"
version = "0.0.9"

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

// Add versions to this library's source code
val generatedVersionSourceDir =
    layout.buildDirectory.dir("generated${File.separator}source${File.separator}versions")
tasks.register("generatePluginVersionSource").configure {
    val versionCatalogFile =
        layout.projectDirectory.file("gradle${File.separator}libs.versions.toml")
    inputs.file(versionCatalogFile)
    outputs.dir(generatedVersionSourceDir)
    doLast {
        val versionLines = extractVersionLines(versionCatalogFile.asFile)
        generatedVersionSourceDir.get().file("Versions.kt").asFile.apply {
            parentFile.mkdirs()
            writeText(
                buildString {
                    appendLine("// Generated file. Do not edit!")
                    appendLine("package versions")
                    appendLine()
                    versionLines.forEach { appendLine(it) }
                }
            )
        }
    }
}
sourceSets["main"].kotlin { srcDir(generatedVersionSourceDir) }
tasks.withType<KotlinCompile>().configureEach { dependsOn("generatePluginVersionSource") }
tasks.withType<Jar>().configureEach { dependsOn("generatePluginVersionSource") }

fun extractVersionLines(file: File): List<String> {
    val fileLines = file.readLines()
    var isInVersionsSection = false
    val listOfVersionLines = mutableListOf<String>()
    for (line in fileLines) {
        if (line.startsWith("[")) {
            isInVersionsSection = line.contains("[versions]")
        }
        if (isInVersionsSection && line.contains("=")) {
            listOfVersionLines.add(
                buildString {
                    append("internal const val ")
                    append(
                        line.split("#").first() // remove comments
                            .uppercase()
                            .trim()
                            .replaceFirst(" ", "_VERSION ")
                    )
                }
            )
        }
    }
    return listOfVersionLines
}
