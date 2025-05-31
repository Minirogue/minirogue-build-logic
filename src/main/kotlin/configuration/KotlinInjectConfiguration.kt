package configuration

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import versions.KOTLININJECTANVIL_VERSION
import versions.KOTLININJECT_VERSION

internal fun Project.configureKotlinInjectAndAnvil() {
    with(pluginManager) {
        applyKsp()
    }

    kotlinExtension.sourceSets.named("commonMain") {
        dependencies.apply {
            add(
                "ksp",
                "me.tatarka.inject:kotlin-inject-compiler-ksp:$KOTLININJECT_VERSION",
            )
            add(
                "ksp",
                "software.amazon.lastmile.kotlin.inject.anvil:compiler:$KOTLININJECTANVIL_VERSION",
            )
        }
        dependencies {
            implementation("me.tatarka.inject:kotlin-inject-runtime:$KOTLININJECT_VERSION")
            implementation("software.amazon.lastmile.kotlin.inject.anvil:runtime:$KOTLININJECTANVIL_VERSION")
            implementation("software.amazon.lastmile.kotlin.inject.anvil:runtime-optional:$KOTLININJECTANVIL_VERSION")
        }
    }
}
