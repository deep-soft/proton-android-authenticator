plugins {
    id("proton.android.authenticator.plugins.libraries.feature")
}

android {
    namespace = "proton.android.authenticator.features.home.master"
}

androidComponents {
    beforeVariants { variant ->
        variant.enableAndroidTest = false
    }
}

dependencies {
    implementation(projects.business.entries)
    implementation(projects.business.entryCodes)
    implementation(projects.business.settings)

    implementation(projects.business.backups)
    implementation(projects.business.appLock)
}
