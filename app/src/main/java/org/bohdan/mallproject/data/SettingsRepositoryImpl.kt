package org.bohdan.mallproject.data

import android.content.SharedPreferences
import org.bohdan.mallproject.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override fun saveThemePreference(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean("isDarkMode", isDarkMode).apply()
    }

    override fun loadThemePreference(): Boolean {
        return sharedPreferences.getBoolean("isDarkMode", false)
    }
}