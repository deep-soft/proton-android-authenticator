package proton.android.authenticator.initializers



import me.proton.core.util.kotlin.CoreLogger
import me.proton.core.util.kotlin.Logger
import org.jetbrains.annotations.NonNls
import timber.log.Timber

// Replication of the TimberLogger available in the me.proton.core.util.android.sentry.TimberLogger
// class in the utilAndroidSentry core module, but we cannot import it on fdroid in order not to
// include the Sentry SDK in the build.
@Suppress("TooManyFunctions")
private object TimberLogger : Logger {

    override fun e(tag: String, message: String) {
        Timber.tag(tag).e(message)
    }

    override fun e(tag: String, e: Throwable) {
        Timber.tag(tag).e(e)
    }

    override fun e(
        tag: String,
        e: Throwable,
        @NonNls message: String
    ) {
        Timber.tag(tag).e(e, message)
    }

    override fun w(tag: String, message: String) {
        Timber.tag(tag).w(message)
    }

    override fun w(tag: String, e: Throwable) {
        Timber.tag(tag).w(e)
    }

    override fun w(
        tag: String,
        e: Throwable,
        @NonNls message: String
    ) {
        Timber.tag(tag).w(e, message)
    }

    override fun i(tag: String, @NonNls message: String) {
        Timber.tag(tag).i(message)
    }

    override fun i(
        tag: String,
        e: Throwable,
        message: String
    ) {
        Timber.tag(tag).i(e, message)
    }

    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun d(
        tag: String,
        e: Throwable,
        message: String
    ) {
        Timber.tag(tag).d(e, message)
    }

    override fun v(tag: String, message: String) {
        Timber.tag(tag).v(message)
    }

    override fun v(
        tag: String,
        e: Throwable,
        message: String
    ) {
        Timber.tag(tag).v(e, message)
    }
}

fun initSentryLogger(coreLogger: CoreLogger) {
    coreLogger.set(TimberLogger)
}