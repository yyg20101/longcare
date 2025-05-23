package com.ytone.longcare

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class MainApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Coroutine Exception Handler
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // Optionally: Restart the app or navigate to an error screen
        // handleCrash()
    }

    override fun onCreate() {
        super.onCreate()

        // Setup Global Uncaught Exception Handler
        setupGlobalExceptionHandler()

        // Example of using the application scope with the handler
        applicationScope.launch(coroutineExceptionHandler) {
            // Example coroutine work
            // For testing: throw RuntimeException("Test coroutine exception from MainApplication")
        }

    }

    private fun setupGlobalExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
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

