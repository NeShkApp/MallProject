package org.bohdan.mallproject.data.sharedpreferences

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemePreferences {

    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_IS_DARK_MODE = "is_dark_mode"

    fun saveThemePreference(context: Context, isDarkMode: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_IS_DARK_MODE, isDarkMode).apply()
    }

    fun isDarkMode(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_DARK_MODE, false)
    }

    fun setAppTheme(context: Context) {
        val isDarkMode = isDarkMode(context)
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
