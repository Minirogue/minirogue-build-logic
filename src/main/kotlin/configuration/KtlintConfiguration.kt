package configuration

import org.gradle.api.Project
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

internal fun Project.configureKtlint() {
    plugins.apply(KtlintPlugin::class.java)
}
