package org.bohdan.mallproject

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Оновлюємо мову
        val languageCode = LanguagePreferences.getLanguage(newBase)
        val updatedContext = updateLocale(newBase, languageCode)

        // Оновлюємо тему (світла чи темна)
        val isDarkMode = ThemePreferences.isDarkMode(newBase)  // Перевіряємо збережену тему
        setAppTheme(isDarkMode)

        super.attachBaseContext(updatedContext)  // Завершуємо ініціалізацію
    }

    private fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)  // Задаємо нову локаль

        return context.createConfigurationContext(config)  // Повертаємо новий контекст з оновленою конфігурацією
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        // Встановлюємо тему додатка
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)  // Застосовуємо обрану тему
    }
}

