plugins {
    id("proton.android.authenticator.plugins.libraries.business")
}

android {
    namespace = "proton.android.authenticator.business.keys"
}

androidComponents {
    beforeVariants { variant ->
        variant.enableAndroidTest = false
    }
}

dependencies {
    compileOnly(files("../../../proton-libs/gopenpgp/gopenpgp.aar"))

    implementation(libs.core.accountManager)
}
