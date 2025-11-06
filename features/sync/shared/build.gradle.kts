plugins {
    id("proton.android.authenticator.plugins.libraries.android")
}

android {
    namespace = "proton.android.authenticator.features.sync.shared"
}

androidComponents {
    beforeVariants { variant ->
        variant.enableAndroidTest = false
    }
}
