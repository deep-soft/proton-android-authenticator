plugins {
    id("proton.android.authenticator.plugins.libraries.business")
}

android {
    namespace = "proton.android.authenticator.business.logs"
}

androidComponents {
    beforeVariants { variant ->
        variant.enableAndroidTest = false
    }
}
