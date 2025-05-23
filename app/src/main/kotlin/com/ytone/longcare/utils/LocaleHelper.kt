package com.ytone.longcare.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        // For API level 17 and above
        // return context.createConfigurationContext(config)

        // For older APIs, update resources directly (deprecated but needed for wider compatibility if not using createConfigurationContext)
        resources.updateConfiguration(config, resources.displayMetrics)
        return context
    }

    /**
     * A more modern approach to setting the locale that returns a new Context.
     * This is generally preferred for newer Android versions (API 17+).
     * The Activity or Application context should be updated with the returned context.
     */
    fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale) // Set layout direction based on locale
        return context.createConfigurationContext(configuration)
    }

    /**
     * Call this method in your Activity's attachBaseContext method.
     * e.g.:
     * override fun attachBaseContext(newBase: Context) {
     *     super.attachBaseContext(LocaleHelper.onAttach(newBase))
     * }
     *
     * And you'd need a way to persist and retrieve the selected language preference.
     */
    fun onAttach(context: Context): Context {
        // Replace "en" with your logic to get the persisted language preference
        val preferredLanguage = getPersistedLanguagePreference(context) // e.g., "en", "zh"
        return updateLocale(context, preferredLanguage)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        return updateLocale(context, defaultLanguage)
    }

    // Example of how you might persist/retrieve language preference (e.g., using SharedPreferences)
    // This is a placeholder and would need a proper implementation.
    private fun getPersistedLanguagePreference(context: Context): String {
        // In a real app, you'd fetch this from SharedPreferences or another storage
        val prefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return prefs.getString("AppLanguage", "en") ?: "en" // Default to English
    }

    // Example of how you might persist language preference
    // This is a placeholder and would need a proper implementation.
    fun persistLanguagePreference(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        prefs.edit().putString("AppLanguage", languageCode).apply()
    }

    /**
     * To be called from an Activity to recreate it after locale change.
     * Make sure to persist the language choice before calling this.
     */
    fun recreateActivity(activity: Activity) {
        activity.recreate()
    }
}
