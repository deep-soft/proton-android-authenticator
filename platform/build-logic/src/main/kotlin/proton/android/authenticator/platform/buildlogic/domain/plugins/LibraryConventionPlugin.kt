package proton.android.authenticator.platform.buildlogic.domain.plugins

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.maybeCreate
import proton.android.authenticator.platform.buildlogic.domain.platform.configuration.PlatformAndroidConfig

internal abstract class LibraryConventionPlugin : ConventionPlugin() {

    protected fun Project.configureAndroidEnvironment() {
        extensions.configure<LibraryExtension> {
            compileSdk = PlatformAndroidConfig.COMPILE_SDK
            ndkVersion = PlatformAndroidConfig.NDK_VERSION

            defaultConfig {
                minSdk = PlatformAndroidConfig.MIN_SDK
            }

            compileOptions {
                sourceCompatibility = PlatformAndroidConfig.CompileJavaVersion
                targetCompatibility = PlatformAndroidConfig.CompileJavaVersion
            }

            lint {
                disable += PlatformAndroidConfig.LinterDisableOptions
            }

            testOptions {
                managedDevices {
                    allDevices {
                        maybeCreate<ManagedVirtualDevice>("pixel2api30").apply {
                            device = "Pixel 2"
                            apiLevel = 30
                            systemImageSource = "aosp-atd"
                        }
                        maybeCreate<ManagedVirtualDevice>("pixel6api34").apply {
                            device = "Pixel 6"
                            apiLevel = 34
                            systemImageSource = "aosp-atd"
                        }
                    }
                }
            }
        }
    }

}
