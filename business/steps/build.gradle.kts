plugins {
    id("proton.android.authenticator.plugins.libraries.business")
}

android {
    namespace = "proton.android.authenticator.business.steps"
}

androidComponents {
    beforeVariants { variant ->
        variant.enableAndroidTest = false
    }
}
