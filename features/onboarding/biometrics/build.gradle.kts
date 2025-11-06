plugins {
    id("proton.android.authenticator.plugins.libraries.feature")
}

android {
    namespace = "proton.android.authenticator.features.onboarding.biometrics"
}

androidComponents {
    beforeVariants { variant ->
        variant.enableAndroidTest = false
    }
}

dependencies {
    implementation(projects.business.appLock)
    implementation(projects.business.biometrics)
    implementation(projects.business.settings)
    implementation(projects.business.steps)
}
