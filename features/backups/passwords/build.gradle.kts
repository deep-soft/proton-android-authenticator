plugins {
    id("proton.android.authenticator.plugins.libraries.feature")
}

android {
    namespace = "proton.android.authenticator.features.backups.passwords"
}

dependencies {
    implementation(libs.core.crypto)

    implementation(projects.business.backups)
    implementation(projects.shared.crypto)
}
