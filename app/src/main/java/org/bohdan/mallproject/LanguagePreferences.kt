package org.bohdan.mallproject

import android.content.Context

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
}
