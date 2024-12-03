package org.bohdan.mallproject.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaFormat.KEY_LANGUAGE
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.repository.SettingsRepository
import java.util.Locale
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : SettingsRepository {


    override fun saveThemePreference(isDarkMode: Boolean) {
        Log.d("SettingsRepositoryImpl", "saveThemePreference::Saving theme preference: $isDarkMode")
        sharedPreferences.edit().putBoolean(KEY_THEME, isDarkMode).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun loadThemePreference(): Boolean {
        val isDarkMode = sharedPreferences.getBoolean(KEY_THEME, false)
        Log.d("SettingsRepositoryImpl", "loadThemePreference::isDarkMode: $isDarkMode")
        return isDarkMode
    }

    override fun changeTheme(){
        val currentTheme = loadThemePreference()
        Log.d("SettingsRepositoryImpl", "changeTheme::Current theme before change: $currentTheme")
        val newTheme = !currentTheme
        saveThemePreference(newTheme)
        Log.d("SettingsRepositoryImpl", "changeTheme::New theme after change: $newTheme")
    }

//    override fun saveLanguagePreference(languageCode: String) {
//        Log.d("SettingsRepository", "Saving language preference: $languageCode")
//        sharedPreferences.edit().putString(KEY_LANGUAGE, languageCode).apply()
//    }
//
//    override fun loadLanguagePreference(): String {
//        val languageCode = sharedPreferences.getString(KEY_LANGUAGE, "en") ?: "en"
//        Log.d("SettingsRepository", "Loaded language preference: $languageCode")
//        return languageCode
//    }
//
//    override fun changeLanguage(context: Context, languageCode: String) {
//        Log.d("SettingsRepository", "Changing language to: $languageCode")
//        saveLanguagePreference(languageCode)
//
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        Log.d("SettingsRepository", "Locale set to: ${locale.language}")
//
//        val config = context.resources.configuration
//        config.setLocale(locale)
//
//        Log.d("SettingsRepository", "Configuration before update: ${config.locale}")
//        context.resources.updateConfiguration(config, context.resources.displayMetrics)
//        Log.d("SettingsRepository", "Configuration after update: ${config.locale}")
//
//    }

//    override fun changeLanguage(context: Context, languageCode: String): Context {
//        saveLanguagePreference(languageCode)
//
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//
//        val config = context.resources.configuration
//        config.setLocale(locale)
//        config.setLayoutDirection(locale)
//
//        return context.createConfigurationContext(config)
//    }


    companion object {
        const val KEY_THEME = "isDarkMode"
        const val KEY_LANGUAGE = "languageCode"
    }
}