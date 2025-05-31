package configuration

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import versions.KOTLININJECTANVIL_VERSION
import versions.KOTLININJECT_VERSION

internal fun Project.configureKotlinInjectAndAnvil(
    kspJvm: Boolean = false,
    kspAndroid: Boolean = false,
    //  kspiOS: Boolean = false,
) {
    with(pluginManager) {
        applyKsp()
    }
    val kspConfigurations = buildSet {
        add("kspCommonMainMetadata")
        if (kspJvm) add("kspJvm")
        if (kspAndroid) add("kspAndroid")
        // if (kspiOS) add ("kspIOS") // TODO need to verify this one
    }

    kotlinExtension.sourceSets.named("commonMain") {
        kspConfigurations.forEach { kspConfig ->
            dependencies.apply {
                add(
                    kspConfig,
                    "me.tatarka.inject:kotlin-inject-compiler-ksp:$KOTLININJECT_VERSION",
                )
                add(
                    kspConfig,
                    "software.amazon.lastmile.kotlin.inject.anvil:compiler:$KOTLININJECTANVIL_VERSION",
                )
            }
        }
        dependencies {
            implementation("me.tatarka.inject:kotlin-inject-runtime:$KOTLININJECT_VERSION")
            implementation("software.amazon.lastmile.kotlin.inject.anvil:runtime:$KOTLININJECTANVIL_VERSION")
            implementation("software.amazon.lastmile.kotlin.inject.anvil:runtime-optional:$KOTLININJECTANVIL_VERSION")
        }
    }
}
