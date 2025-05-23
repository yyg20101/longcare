package com.ytone.longcare

import android.app.Application
import com.ytone.longcare.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var timberTree: Timber.Tree // For Hilt injection if needed, but static planting is common

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Coroutine Exception Handler
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable, "Uncaught coroutine exception")
        // Optionally: Restart the app or navigate to an error screen
        // handleCrash()
    }

    override fun onCreate() {
        super.onCreate()

        // Setup Global Uncaught Exception Handler
        setupGlobalExceptionHandler()

        // Plant Timber tree based on build type
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber.DebugTree planted.")
        } else {
            Timber.plant(ReleaseTree())
            Timber.d("ReleaseTree planted.")
        }

        // Example of using the application scope with the handler
        applicationScope.launch(coroutineExceptionHandler) {
            // Example coroutine work
            // For testing: throw RuntimeException("Test coroutine exception from MainApplication")
        }

        Timber.i("MainApplication onCreate finished.")
    }

    private fun setupGlobalExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.e(throwable, "Uncaught exception on thread: ${thread.name}")
            // Log to your crash reporting tool here in a real app
            // FirebaseCrashlytics.getInstance().recordException(throwable)

            // Optional: Custom crash handling logic (e.g., show a crash screen, restart app)
            // handleCrash(throwable)

            // It's important to call the original handler if you want the OS to handle it (e.g., force close)
            // or if you have other crash reporting SDKs that need to process the exception.
            defaultHandler?.uncaughtException(thread, throwable)
            // For a cleaner exit after logging, though defaultHandler might do this.
            // exitProcess(1) 
        }
    }

    // Example of a custom crash handling function
    // private fun handleCrash(throwable: Throwable) {
    //     // Navigate to a specific error activity
    //     // val intent = Intent(applicationContext, ErrorActivity::class.java)
    //     // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    //     // intent.putExtra("error_details", throwable.message)
    //     // startActivity(intent)
    //     // exitProcess(1) // Ensure the app closes
    // }
}

/**
 * A Timber Tree for release builds.
 * In a real application, this tree would typically:
 * 1. Filter out verbose or debug logs.
 * 2. Log important messages (warnings, errors) to a crash reporting service (e.g., Firebase Crashlytics).
 */
private class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == android.util.Log.VERBOSE || priority == android.util.Log.DEBUG) {
            return // Don't log verbose or debug messages in release
        }

        // Here you would integrate with your crash reporting tool
        // For example, if using Firebase Crashlytics:
        // val crashlytics = FirebaseCrashlytics.getInstance()
        // crashlytics.log(message) // Log the message
        // if (t != null && priority == android.util.Log.ERROR) {
        //    crashlytics.recordException(t) // Log the throwable for errors
        // }

        // For this subtask, we'll just log errors and warnings to Logcat
        if (priority == android.util.Log.ERROR) {
            if (t != null) {
                android.util.Log.e(tag, message, t)
            } else {
                android.util.Log.e(tag, message)
            }
        } else if (priority == android.util.Log.WARN) {
            if (t != null) {
                android.util.Log.w(tag, message, t)
            } else {
                android.util.Log.w(tag, message)
            }
        }
        // Info logs can also be logged if desired, or omitted
        // else if (priority == android.util.Log.INFO) {
        //     android.util.Log.i(tag, message)
        // }
    }
}
