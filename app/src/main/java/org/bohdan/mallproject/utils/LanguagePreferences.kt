package org.bohdan.mallproject.utils

import android.content.Context
import java.util.Locale

object LanguagePreferences {

    private const val PREFS_NAME = "language_prefs"
    private const val KEY_LANGUAGE_CODE = "language_code"

    fun saveLanguage(context: Context, languageCode: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_LANGUAGE_CODE, languageCode).apply()
    }

    fun getLanguage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_LANGUAGE_CODE, "en") ?: "en"
    }

    fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
