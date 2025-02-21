package configuration

import org.gradle.api.Project
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

internal fun Project.configureKtlint() {
    plugins.apply(KtlintPlugin::class.java)

    extensions.configure(KtlintExtension::class.java) {
        android.set(true)
        filter {
            exclude { it.file.path.contains("/generated/") }
        }
    }
}
