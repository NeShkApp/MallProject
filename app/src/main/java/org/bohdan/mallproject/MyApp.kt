package org.bohdan.mallproject

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import com.stripe.android.Stripe
import dagger.hilt.android.HiltAndroidApp
import org.bohdan.mallproject.domain.repository.SettingsRepository
import org.bohdan.mallproject.domain.usecase.settings.LoadThemePreferenceUseCase
import org.bohdan.mallproject.domain.usecase.settings.SaveThemePreferenceUseCase
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        applyConfiguration()
    }

    private fun applyConfiguration() {
        // Тема
        val isDarkMode = ThemePreferences.isDarkMode(this)
        Log.d("MyApp", "START THEME: $isDarkMode")
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)

        // Мова
        val languageCode = LanguagePreferences.getLanguage(this)
        Log.d("MyApp", "START LANG: $languageCode")
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        // Оновлюємо ресурси з новою мовною конфігурацією
        resources.updateConfiguration(config, resources.displayMetrics)
    }



}
