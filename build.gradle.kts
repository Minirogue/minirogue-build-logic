import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.detekt)
}

group = "org.minirogue"
version = "0.0.13"

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
    autoCorrect = true
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.compose.compilerGradlePlugin)
    implementation(libs.compose.hotReloadGradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.room.gradlePlugin)
    implementation(libs.serialization.gradlePlugin)

    detektPlugins(libs.detekt.formatting)
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
val readmeFile = rootProject.file("README.md")
tasks.register("generatePluginVersionSource").configure {
    val versionCatalogFile =
        layout.projectDirectory.file("gradle${File.separator}libs.versions.toml")
    inputs.file(versionCatalogFile)
    outputs.dir(generatedVersionSourceDir)
    inputs.file(readmeFile)
    outputs.file(readmeFile)
    doLast {
        val versionLines = extractVersionLines(versionCatalogFile.asFile)
        generatedVersionSourceDir.get().file("Versions.kt").asFile.apply {
            parentFile.mkdirs()
            writeText(
                buildString {
                    appendLine("// Generated file. Do not edit!")
                    appendLine("package versions")
                    appendLine()
                    versionLines.forEach { appendLine(convertVersionLineToKotlinVersionLine(it)) }
                }
            )
        }
        val readmeLines = readmeFile.readLines()
        readmeFile.writeText(buildString {
            readmeLines.forEach { readmeLine ->
                appendLine(
                    if (readmeLine.startsWith("-") && readmeLine.contains("=")) {
                        updateReadmeVersionLine(readmeLine, versionLines)
                    } else readmeLine
                )
            }
        })
    }
}
sourceSets["main"].kotlin { srcDir(generatedVersionSourceDir) }
tasks.withType<KotlinCompile>().configureEach { dependsOn("generatePluginVersionSource") }
tasks.withType<Jar>().configureEach { dependsOn("generatePluginVersionSource") }

tasks.register("checkReadme").configure {
    inputs.file(readmeFile)
    dependsOn("generatePluginVersionSource")
    doLast {
        val gitStatus = providers.exec {
            commandLine("git", "status")
        }.standardOutput.asText.get()
        if (gitStatus.contains("README.md")) throw GradleException("README.md not up-to-date, please run ./gradlew generatePluginVersionSource (automatically run on most build jobs)")
    }
}

fun convertVersionLineToKotlinVersionLine(versionLine: String): String = buildString {
    append("internal const val ")
    append(
        versionLine
            .uppercase()
            .trim()
            .replaceFirst(" ", "_VERSION ")
    )
}

fun updateReadmeVersionLine(readmeVersionLine: String, versionLines: List<String>): String {
    for (versionLine in versionLines) {
        if (readmeVersionLine.contains(versionLine.split("=").first().trim(), ignoreCase = true)) {
            return readmeVersionLine.split("=").first()
                .trim() + " = " + versionLine.split("=")[1].trim().trim("\"".first())
        }
    }
    return readmeVersionLine
}

fun extractVersionLines(file: File): List<String> {
    val fileLines = file.readLines()
    var isInVersionsSection = false
    val listOfVersionLines = mutableListOf<String>()
    for (line in fileLines) {
        if (line.startsWith("[")) {
            isInVersionsSection = line.contains("[versions]")
        }
        if (isInVersionsSection && line.contains("=")) {
            listOfVersionLines.add(line.split("#").first()) // remove comments
        }
    }
    return listOfVersionLines
}
