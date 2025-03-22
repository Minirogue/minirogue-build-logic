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
import org.gradle.process.internal.ExecException
import versions.JAVA_VERSION

private const val MIN_SDK = 21
private const val COMPILE_SDK = 35
private const val TARGET_SDK = 35

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
        defaultConfig {
            targetSdk = TARGET_SDK
            versionCode = getVersionCodeFromGitHistory()
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

private fun Project.getVersionCodeFromGitHistory(): Int = try {
    providers.exec {
        commandLine("git", "rev-list", "--count", "HEAD")
    }.standardOutput.asText.get().trim().toInt()
        .also { logger.info("version code for $project is $it") }
} catch (e: ExecException) {
    logger.warn("No git history available, so $project version code set to 1", e)
    1
}
