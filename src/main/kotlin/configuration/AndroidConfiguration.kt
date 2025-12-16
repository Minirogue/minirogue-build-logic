package configuration

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import ext.generateProjectNamespace
import ext.generateResourcePrefix
import ext.getDateAsVersionName
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.file.File
import task.CreateVersionCodeFileTask
import task.GetGitCommitNumberTask
import task.MINIROGUE_TASK_GROUP
import versions.JAVA_VERSION
import java.io.IOException

private const val MIN_SDK = 23
private const val COMPILE_SDK = 36
private const val TARGET_SDK = 36

internal fun Project.configureAndroidLibrary() {
    with(pluginManager) { apply(LibraryPlugin::class.java) }
    extensions.configure(LibraryExtension::class.java) {
        namespace = generateProjectNamespace()
        resourcePrefix = generateResourcePrefix()
        configureAndroidCommon(this)
    }
}

internal fun Project.configureAndroidApp() {
    extensions.configure(ApplicationExtension::class.java) {
        configureAndroidCommon(this)
        configureCreateAndroidVersionCodeTask() // TODO add end-user configurability
        defaultConfig {
            targetSdk = TARGET_SDK
            versionCode = getVersionCodeFromPropertyFile()
            versionName = getDateAsVersionName()
            namespace = generateProjectNamespace()
        }
        buildTypes {
            release {
                isDebuggable = false
                isMinifyEnabled = true
                proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
                proguardFiles.add(project.file("proguard-rules.pro"))
            }
            debug {
                applicationIdSuffix = ".debug"
            }
        }
    }
}

private fun Project.configureAndroidCommon(commonExtension: CommonExtension<*, *, *, *, *, *>) =
    with(commonExtension) {
        compileSdk = COMPILE_SDK
        defaultConfig {
            minSdk = MIN_SDK
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.toVersion(JAVA_VERSION)
            targetCompatibility = JavaVersion.toVersion(JAVA_VERSION)
        }
        lint {
            baseline = file("lint-baseline.xml")
        }
    }

private fun Project.configureCreateAndroidVersionCodeTask() {
    tasks.register("getGitCommitNumber", GetGitCommitNumberTask::class.java) {
        group = MINIROGUE_TASK_GROUP
        description = "Gets number of git commits in current branch's history."
    }
    // TODO add end-user configuration for source of version code
    tasks.register(
        "createAndroidVersionCode", // TODO extract to constant name
        CreateVersionCodeFileTask::class.java,
    ) {
        dependsOn("getGitCommitNumber")
        versionCodeSource.set(
            tasks.named("getGitCommitNumber", GetGitCommitNumberTask::class.java)
                .get().numberOfCommitsInBranchHistory,
        )

        group = MINIROGUE_TASK_GROUP
        description = "Creates versionCode file that is used for Android app"
    }
}

// TODO would like this to be verified as up-to-date at configuration time, but that would violate
//  guidelines about configuration
private fun Project.getVersionCodeFromPropertyFile(): Int = try {
    val versionFile =
        layout.buildDirectory.file("minirogue${File.separator}versionCode") // TODO extract to configurable location
    versionFile.get().asFile.readText().trim().toIntOrNull()
        ?: 1.also { logger.warn("versionCode not properly formatted in $versionFile, defaulting to 1") }
} catch (ioException: IOException) {
    logger.warn(
        "Couldn't read versionCode from createAndroidVersionCode task. Ensure it has been run at least once since the last clean build. Defaulting to version code 1",
        ioException,
    )
    1
}
