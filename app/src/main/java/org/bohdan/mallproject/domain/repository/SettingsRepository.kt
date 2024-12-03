package org.bohdan.mallproject.domain.repository

import android.content.Context

interface SettingsRepository {
    fun saveThemePreference(isDarkMode: Boolean)
    fun loadThemePreference(): Boolean
    fun changeTheme()

//    fun saveLanguagePreference(languageCode: String)
//    fun loadLanguagePreference(): String
////    fun changeLanguage(context: Context, languageCode: String): Context
//    fun changeLanguage(context: Context, languageCode: String)
}