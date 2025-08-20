plugins {
    id("proton.android.authenticator.plugins.libraries.feature")
}

android {
    namespace = "proton.android.authenticator.features.imports.scan"
}

dependencies {
    implementation(projects.business.entries)
    implementation(projects.features.imports.shared)
}
