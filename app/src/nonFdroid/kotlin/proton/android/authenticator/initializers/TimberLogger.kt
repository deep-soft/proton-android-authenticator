package proton.android.authenticator.initializers

import me.proton.core.util.android.sentry.TimberLogger
import me.proton.core.util.kotlin.CoreLogger

fun initSentryLogger(coreLogger: CoreLogger) {
    coreLogger.set(TimberLogger)
}