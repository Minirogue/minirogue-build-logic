package configuration

import dagger.hilt.android.plugin.HILT_VERSION
import dagger.hilt.android.plugin.HiltGradlePlugin
import ext.isMultiplatform
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal fun Project.configureHilt() {
    with(pluginManager) {
        applyKsp()
        apply(HiltGradlePlugin::class.java)
    }

    if (isMultiplatform()) {
        kotlinExtension.sourceSets.named("androidMain") {
            dependencies.add("implementation", "com.google.dagger:hilt-android:$HILT_VERSION")
            dependencies.add("kspAndroid", "com.google.dagger:hilt-android-compiler:$HILT_VERSION")
        }
    } else {
        with(dependencies) {
            add("implementation", "com.google.dagger:hilt-android:$HILT_VERSION")
            add("ksp", "com.google.dagger:hilt-android-compiler:$HILT_VERSION")
        }
    }
}
