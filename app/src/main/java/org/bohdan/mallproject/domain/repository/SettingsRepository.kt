package org.bohdan.mallproject.domain.repository

interface SettingsRepository {
    fun saveThemePreference(isDarkMode: Boolean)
    fun loadThemePreference(): Boolean
}