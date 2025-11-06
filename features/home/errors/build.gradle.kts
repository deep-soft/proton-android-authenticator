plugins {
    id("proton.android.authenticator.plugins.libraries.feature")
}

android {
    namespace = "proton.android.authenticator.features.home.errors"
}

androidComponents {
    beforeVariants { variant ->
        variant.enableAndroidTest = false
    }
}
